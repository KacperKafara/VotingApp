package pl.kafara.voting.vote.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.Print;

import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface PrintRepository extends JpaRepository<Print, String> {

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @NonNull Optional<Print> findById(@NonNull String id);

    @Transactional(propagation = Propagation.REQUIRED)
    @NonNull Print save(@NonNull Print print);
}
