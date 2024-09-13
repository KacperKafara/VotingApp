package pl.kafara.voting.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.tokens.AccountVerificationToken;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface AccountVerificationTokenRepository extends JpaRepository<AccountVerificationToken, UUID> {
    void deleteByUserId(UUID userId);
    Optional<AccountVerificationToken> findByToken(String token);
}
