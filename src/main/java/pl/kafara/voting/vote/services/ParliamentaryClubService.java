package pl.kafara.voting.vote.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.vote.repositories.ParliamentaryClubRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ParliamentaryClubService {
    private final ParliamentaryClubRepository parliamentaryClubRepository;

    //TODO: Poprawić
    @Value("${sejm.current-term}")
    private String currentTerm;

    @PreAuthorize("hasRole('VOTER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ParliamentaryClub> getAllExceptIndependent() {
        return parliamentaryClubRepository.findAllByShortNameNotAndTerm("niez.", currentTerm);
    }
}
