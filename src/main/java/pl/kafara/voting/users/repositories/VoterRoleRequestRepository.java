package pl.kafara.voting.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.VoterRoleRequest;

import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface VoterRoleRequestRepository extends JpaRepository<VoterRoleRequest, UUID> {
}
