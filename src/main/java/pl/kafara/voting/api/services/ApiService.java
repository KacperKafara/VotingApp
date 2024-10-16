package pl.kafara.voting.api.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import pl.kafara.voting.api.mappers.EnvoyMapper;
import pl.kafara.voting.api.mappers.VotingMapper;
import pl.kafara.voting.api.model.EnvoyAPI;
import pl.kafara.voting.api.model.VotingAPI;
import pl.kafara.voting.api.repositories.EnvoyRepository;
import pl.kafara.voting.api.repositories.ParliamentaryClubRepository;
import pl.kafara.voting.api.repositories.SittingRepository;
import pl.kafara.voting.api.repositories.VotingRepository;
import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.model.vote.Sitting;
import pl.kafara.voting.model.vote.Voting;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
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
            while (true) {
                ResponseEntity<VotingAPI> votingResult;

                try {
                    votingResult = restClient.get()
                            .uri(term + "/votings/" + sitting.getNumber() + "/" + iterator)
                            .retrieve()
                            .toEntity(VotingAPI.class);
                } catch (HttpClientErrorException.NotFound e) {
                    break;
                }

                VotingAPI voting = votingResult.getBody();

                Optional<Voting> isVoting = votingRepository.getVotingFiltered(voting.getSittingDay(), voting.getVotingNumber(), sitting);
                if (isVoting.isPresent())
                    continue;
                Voting votingEntity = VotingMapper.update(voting, sitting);
                votingRepository.save(votingEntity);

                iterator++;
            }
        }
    }
}
