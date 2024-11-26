package pl.kafara.voting.vote.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kafara.voting.model.vote.ParliamentaryClub;
import pl.kafara.voting.vote.services.ParliamentaryClubService;

import java.util.List;

@RestController
@RequestMapping("/parliamentary-clubs")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class ParliamentaryClubController {
    private final ParliamentaryClubService parliamentaryClubService;

    @GetMapping
    @PreAuthorize("hasRole('VOTER')")
    public ResponseEntity<List<String>> getAllExceptIndependent() {
        List<ParliamentaryClub> parliamentaryClubs = parliamentaryClubService.getAllExceptIndependent();
        List<String> ids = parliamentaryClubs.stream().map(ParliamentaryClub::getId).toList();

        return ResponseEntity.ok(ids);
    }
}
