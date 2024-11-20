package pl.kafara.voting.users.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.users.RoleRequestResolution;
import pl.kafara.voting.model.users.VoterRoleRequest;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface VoterRoleRequestRepository extends JpaRepository<VoterRoleRequest, UUID> {
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Page<VoterRoleRequest> getAllByUser_UsernameContainsAndResolutionEquals(Pageable pageable, String username, RoleRequestResolution requestResolution);

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<VoterRoleRequest> getVoterRoleRequestByUser_Id(UUID id);
}
