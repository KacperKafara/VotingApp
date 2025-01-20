package pl.kafara.voting.vote.api.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import pl.kafara.voting.model.vote.*;
import pl.kafara.voting.vote.api.mappers.*;
import pl.kafara.voting.vote.api.model.*;
import pl.kafara.voting.vote.api.repositories.LastVotingsUpdateRepository;
import pl.kafara.voting.vote.api.repositories.EnvoyRepository;
import pl.kafara.voting.vote.repositories.ParliamentaryClubRepository;
import pl.kafara.voting.vote.api.repositories.SittingRepository;
import pl.kafara.voting.vote.repositories.PrintRepository;
import pl.kafara.voting.vote.repositories.VotingRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.NEVER)
@DependsOn("entityManagerFactory")
@ConditionalOnProperty(name = "sejm.sync", havingValue = "true", matchIfMissing = true)
public class ApiService {

    @Value("${sejm.term}")
    private String termValue;

    @Value("${sejm.current-term}")
    private String currentTerm;

    @Value("${sejm.api.url}")
    private String sejmApiUrl;

    private final List<String> terms = new ArrayList<>();

    private final EnvoyRepository envoyRepository;
    private final SittingRepository sittingRepository;
    private final ParliamentaryClubRepository parliamentaryClubRepository;
    private final VotingRepository votingRepository;
    private final RestClient restClient;
    private final LastVotingsUpdateRepository lastVotingsUpdateRepository;
    private final PrintRepository printRepository;

    @PostConstruct
    public void init() {
        int termNumber = Integer.parseInt(termValue.replace("term", ""));
        if (termNumber < 8) {
            throw new IllegalArgumentException("Term number must be greater than 7");
        }
        int currentTermNumber = Integer.parseInt(currentTerm.replace("term", ""));
        for (int i = termNumber; i <= currentTermNumber; i++) {
            terms.add("term" + i);
        }
    }

    public void updateParliamentaryClubList() {
        for (String term : terms) {
            List<ParliamentaryClubAPI> parliamentaryClubs = restClient.get()
                    .uri(term + "/clubs")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (parliamentaryClubs == null)
                throw new NullPointerException("parliamentaryClubs is null");

            for (ParliamentaryClubAPI parliamentaryClubAPI : parliamentaryClubs) {
                Optional<ParliamentaryClub> parliamentaryClubOptional = parliamentaryClubRepository.findByTermAndShortName(term, parliamentaryClubAPI.getId());
                if (parliamentaryClubOptional.isEmpty())
                    parliamentaryClubRepository.save(ParliamentaryClubMapper.mapToParliamentaryClub(parliamentaryClubAPI, term));
                else
                    parliamentaryClubRepository.save(ParliamentaryClubMapper.update(parliamentaryClubOptional.get(), parliamentaryClubAPI));
            }
        }
    }

    public void updateEnvoyList() {
        for (String term : terms) {
            List<EnvoyAPI> envoys = restClient.get()
                    .uri(term + "/MP")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (envoys == null)
                throw new NullPointerException("envoys is null");

            ParliamentaryClub savedClub;
            for (EnvoyAPI envoyAPI : envoys) {
                Optional<ParliamentaryClub> parliamentaryClubOptional = parliamentaryClubRepository.findByTermAndShortName(term, envoyAPI.getClub());
                if (parliamentaryClubOptional.isEmpty()) {
                    ParliamentaryClubAPI parliamentaryClub = restClient.get()
                            .uri(term + "/clubs/" + envoyAPI.getClub())
                            .retrieve()
                            .body(ParliamentaryClubAPI.class);

                    if (parliamentaryClub == null)
                        continue;

                    savedClub = parliamentaryClubRepository.save(ParliamentaryClubMapper.mapToParliamentaryClub(parliamentaryClub, term));
                } else {
                    savedClub = parliamentaryClubOptional.get();
                }
                Optional<Envoy> envoyOptional = envoyRepository.findByInTermNumberAndTerm(envoyAPI.getId(), term);
                if (envoyOptional.isEmpty())
                    envoyRepository.save(EnvoyMapper.update(envoyAPI, savedClub));
                else
                    envoyRepository.save(EnvoyMapper.update(envoyOptional.get(), envoyAPI, savedClub));
            }
        }
    }

    public void updateSittingList() {
        for (String term : terms) {
            List<SittingAPI> sittingsApi = restClient.get()
                    .uri(term + "/proceedings")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (sittingsApi == null)
                throw new NullPointerException("sittings is null");

            sittingsApi.removeIf(sitting -> sitting.getNumber() == 0);

            for (SittingAPI sittingAPI : sittingsApi) {
                Optional<Sitting> sittingOptional = sittingRepository.findByNumberAndTerm(sittingAPI.getNumber(), term);
                if (sittingOptional.isEmpty()) {
                    sittingRepository.save(SittingMapper.toEntity(sittingAPI, term));
                }
            }
        }
    }

    public void updateVotingList() {
        LastVotingsUpdate lastVotingsUpdate = lastVotingsUpdateRepository.findById(1L).orElse(null);
        List<Sitting> sittings;
        if (lastVotingsUpdate != null)
            sittings = sittingRepository.findByNumber(lastVotingsUpdate.getLastSitting().getNumber(), lastVotingsUpdate.getLastSitting().getTerm());
        else {
            lastVotingsUpdate = new LastVotingsUpdate(1L, null);
            sittings = sittingRepository.findAll();
        }

        for (Sitting sitting : sittings) {
            log.info("Updating votings for sitting: " + sitting.getNumber() + " in term: " + sitting.getTerm());
            int iterator = 0;
            List<Object> objects = restClient.get()
                    .uri(sitting.getTerm() + "/votings/" + sitting.getNumber())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (objects == null)
                throw new NullPointerException("objects is null");

            int count = objects.size();
            while (count > 0) {
                ResponseEntity<VotingAPI> votingResult;

                try {
                    votingResult = restClient.get()
                            .uri(sitting.getTerm() + "/votings/" + sitting.getNumber() + "/" + ++iterator)
                            .retrieve()
                            .toEntity(VotingAPI.class);
                } catch (Exception e) {
                    continue;
                }

                VotingAPI voting = votingResult.getBody();

                if (voting == null)
                    break;

                Optional<Voting> isVoting = votingRepository.getVotingFiltered(voting.getSittingDay(), voting.getVotingNumber(), sitting);
                if (isVoting.isPresent()) {
                    count--;
                    continue;
                }

                Voting votingEntity;
                if (voting.getVotingOptions() != null)
                    votingEntity = VotingMapper.update(voting, sitting, voting.getVotingOptions());
                else
                    votingEntity = VotingMapper.update(voting, sitting);
                votingEntity.setPrints(updatePrints(voting.getTitle(), sitting.getTerm()));
                votingEntity.setVotes(updateVotes(voting.getVotes(), votingEntity, sitting.getTerm()));
                try {
                    votingRepository.saveAndFlush(votingEntity);
                } catch (Throwable e) {
                    log.error("Error while saving voting: {} {}", votingEntity.getSittingDay(), votingEntity.getVotingNumber());
                }
                count--;
            }
            lastVotingsUpdate.setLastSitting(sitting);
        }
        lastVotingsUpdateRepository.save(lastVotingsUpdate);
    }

    protected List<Print> updatePrints(String title, String term) {
        List<String> prints = ApiUtils.extractPrints(title);
        List<Print> printsList = new ArrayList<>();
        for (String print : prints) {
            PrintAPI printApi;
            try {
                printApi = restClient.get()
                        .uri(term + "/prints/" + print)
                        .retrieve()
                        .body(PrintAPI.class);
            } catch (Exception ignored) {
                continue;
            }
            if (printApi == null)
                continue;
            String url = sejmApiUrl + term + "/prints/" + print + "/" + print + ".pdf";
            Optional<Print> printOptional = printRepository.findByNumberAndTerm(print, term);
            if (printOptional.isEmpty()) {
                try {
                    Print savedPrint = printRepository.save(new Print(printApi.getNumber(), printApi.getTitle(), url, term));
                    printsList.add(savedPrint);
                } catch (Throwable ignored) {
                    log.error("Error while saving print: {} {}", printApi.getNumber(), printApi.getTitle());
                }

            } else {
                printsList.add(printOptional.get());
            }
        }
        return printsList;
    }

    protected List<Vote> updateVotes(List<VoteAPI> votes, Voting voting, String term) {
        List<Vote> votesList = new ArrayList<>();
        for (VoteAPI voteAPI : votes) {
            Optional<Envoy> envoyOptional = envoyRepository.findByInTermNumberAndTerm(voteAPI.getMP(), term);
            if (envoyOptional.isEmpty())
                continue;

            Envoy envoy = envoyOptional.get();

            if (!voting.getKind().equals(VotingKind.ON_LIST))
                votesList.add(VoteMapper.update(voteAPI, voting, envoy));
            else {
                Optional<String> keyForYes = voteAPI.getListVotes()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().equals("YES"))
                        .map(Map.Entry::getKey)
                        .findFirst();

                if (keyForYes.isEmpty()) {
                    votesList.add(VoteMapper.update(voteAPI, voting, envoy));
                    continue;
                }

                voting.getVotingOptions()
                        .stream()
                        .filter(votingOption -> votingOption.getOptionIndex() == Integer.parseInt(keyForYes.get()))
                        .findFirst()
                        .ifPresent(votingOption -> votesList.add(VoteMapper.update(voteAPI, voting, envoy, votingOption)));
            }
        }

        return votesList;
    }
}
