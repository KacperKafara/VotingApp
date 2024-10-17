package pl.kafara.voting.vote.api.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import pl.kafara.voting.vote.api.mappers.VotingMapper;
import pl.kafara.voting.vote.api.model.EnvoyAPI;
import pl.kafara.voting.vote.api.model.VotingAPI;
import pl.kafara.voting.vote.repositories.EnvoyRepository;
import pl.kafara.voting.vote.repositories.ParliamentaryClubRepository;
import pl.kafara.voting.vote.repositories.SittingRepository;
import pl.kafara.voting.vote.repositories.VotingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
@ConditionalOnProperty(name = "spring.tests", havingValue = "false", matchIfMissing = true)
public class ApiService {

    @Value("${sejm.term}")
    private String term;

    private final EnvoyRepository envoyRepository;
    private final SittingRepository sittingRepository;
    private final ParliamentaryClubRepository parliamentaryClubRepository;
    private final VotingRepository votingRepository;
    private final RestClient restClient;

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
        List<Sitting> sittings = sittingRepository.findAll();

        for (Sitting sitting : sittings) {
            int iterator = 1;
            boolean isEnd = false;
            while (true) {
                ResponseEntity<VotingAPI> votingResult;

                try {
                    votingResult = restClient.get()
                            .uri(term + "/votings/" + sitting.getNumber() + "/" + iterator)
                            .retrieve()
                            .toEntity(VotingAPI.class);
                } catch (HttpClientErrorException.NotFound e) {
                    iterator++;
                    if (!isEnd) {
                        isEnd = true;
                        continue;
                    } else {
                        break;
                    }
                }

                VotingAPI voting = votingResult.getBody();

                if (voting == null)
                    break;

                Optional<Voting> isVoting = votingRepository.getVotingFiltered(voting.getSittingDay(), voting.getVotingNumber(), sitting);
                if (isVoting.isPresent())
                    continue;

                Voting votingEntity = VotingMapper.update(voting, sitting, voting.getVotingOptions());

                votingRepository.save(votingEntity);

                iterator++;
            }
        }
    }
}
