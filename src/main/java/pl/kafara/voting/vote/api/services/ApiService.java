package pl.kafara.voting.vote.api.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import pl.kafara.voting.model.vote.*;
import pl.kafara.voting.vote.api.mappers.EnvoyMapper;
import pl.kafara.voting.vote.api.mappers.VoteMapper;
import pl.kafara.voting.vote.api.mappers.VotingMapper;
import pl.kafara.voting.vote.api.model.*;
import pl.kafara.voting.vote.api.repositories.LastVotingsUpdateRepository;
import pl.kafara.voting.vote.api.repositories.EnvoyRepository;
import pl.kafara.voting.vote.repositories.ParliamentaryClubRepository;
import pl.kafara.voting.vote.api.repositories.SittingRepository;
import pl.kafara.voting.vote.repositories.PrintRepository;
import pl.kafara.voting.vote.repositories.VotingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.NEVER)
@ConditionalOnProperty(name = "spring.tests", havingValue = "false", matchIfMissing = true)
@DependsOn(value = {"flywayConfig", "loggingAspect"})
public class ApiService {

    @Value("${sejm.term}")
    private String term;

    @Value("${sejm.api.url}")
    private String sejmApiUrl;

    private final EnvoyRepository envoyRepository;
    private final SittingRepository sittingRepository;
    private final ParliamentaryClubRepository parliamentaryClubRepository;
    private final VotingRepository votingRepository;
    private final RestClient restClient;
    private final LastVotingsUpdateRepository lastVotingsUpdateRepository;
    private final PrintRepository printRepository;

    @PostConstruct
    public void init() {
        updateParliamentaryClubList();
        updateEnvoyList();
        updateSittingList();
        updateVotingList();
    }

    @Scheduled(cron = "0 5 0 */2 * *")
    public void updateParliamentaryClubList() {
        List<ParliamentaryClub> parliamentaryClubs = restClient.get()
                .uri(term + "/clubs")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (parliamentaryClubs == null)
            throw new NullPointerException("parliamentaryClubs is null");

        parliamentaryClubRepository.saveAll(parliamentaryClubs);
    }

    @Scheduled(cron = "0 10 0 */2 * *")
    public void updateEnvoyList() {
        List<EnvoyAPI> envoys = restClient.get()
                .uri(term + "/MP")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (envoys == null)
            throw new NullPointerException("envoys is null");

        ParliamentaryClub savedClub;
        for (EnvoyAPI envoyAPI : envoys) {
            Optional<ParliamentaryClub> parliamentaryClubOptional = parliamentaryClubRepository.findById(envoyAPI.getClub());
            if (parliamentaryClubOptional.isEmpty()) {
                ParliamentaryClub parliamentaryClub = restClient.get()
                        .uri(term + "/clubs/" + envoyAPI.getClub())
                        .retrieve()
                        .body(ParliamentaryClub.class);

                if (parliamentaryClub == null)
                    continue;

                savedClub = parliamentaryClubRepository.save(parliamentaryClub);
            } else {
                savedClub = parliamentaryClubOptional.get();
            }
            envoyRepository.save(EnvoyMapper.update(envoyAPI, savedClub));
        }
    }

    @Scheduled(cron = "0 15 0 */2 * *")
    public void updateSittingList() {
        List<Sitting> sittings = restClient.get()
                .uri(term + "/proceedings")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (sittings == null)
            throw new NullPointerException("sittings is null");

        sittings.removeIf(sitting -> sitting.getNumber() == 0);

        sittingRepository.saveAll(sittings);
    }

    @Scheduled(cron = "0 20 0 */2 * *")
    public void updateVotingList() {
        LastVotingsUpdate lastVotingsUpdate = lastVotingsUpdateRepository.findById(1L);
        List<Sitting> sittings;
        if (lastVotingsUpdate != null)
            sittings = sittingRepository.findByNumber(lastVotingsUpdate.getLastSitting().getNumber());
        else {
            lastVotingsUpdate = new LastVotingsUpdate(1L, null);
            sittings = sittingRepository.findAll();
        }

        for (Sitting sitting : sittings) {
            int iterator = 0;
            List<Object> objects = restClient.get()
                    .uri(term + "/votings/" + sitting.getNumber())
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
                            .uri(term + "/votings/" + sitting.getNumber() + "/" + ++iterator)
                            .retrieve()
                            .toEntity(VotingAPI.class);
                } catch (HttpClientErrorException.NotFound e) {
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
                votingEntity.setPrints(updatePrints(voting.getTitle()));
                votingEntity.setVotes(updateVotes(voting.getVotes(), votingEntity));
                votingRepository.saveAndFlush(votingEntity);
                count--;

            }
            lastVotingsUpdate.setLastSitting(sitting);
        }
        lastVotingsUpdateRepository.save(lastVotingsUpdate);
    }

    protected List<Print> updatePrints(String title) {
        List<String> prints = ApiUtils.extractPrints(title);
        List<Print> printsList = new ArrayList<>();
        for(String print : prints) {
            PrintAPI printApi = restClient.get()
                    .uri(term + "/prints/" + print)
                    .retrieve()
                    .body(PrintAPI.class);
            if(printApi == null)
                continue;
            String url = sejmApiUrl + term + "/prints/" + print + "/" + print + ".pdf";
            Optional<Print> printOptional = printRepository.findById(print);
            if (printOptional.isEmpty()) {
                Print savedPrint = printRepository.save(new Print(printApi.getNumber(), printApi.getTitle(), url));
                printsList.add(savedPrint);
            } else {
                printsList.add(printOptional.get());
            }
        }
        return printsList;
    }

    protected List<Vote> updateVotes(List<VoteAPI> votes, Voting voting) {
        List<Vote> votesList = new ArrayList<>();
        for (VoteAPI voteAPI : votes) {
            Optional<Envoy> envoyOptional = envoyRepository.findById(voteAPI.getMP());
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
