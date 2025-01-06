package pl.kafara.voting.vote.api.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Envoy;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface EnvoyRepository extends JpaRepository<Envoy, Long> {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @NonNull Envoy save(@NonNull Envoy envoy);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @NonNull Optional<Envoy> findById(@NonNull Long id);
}
