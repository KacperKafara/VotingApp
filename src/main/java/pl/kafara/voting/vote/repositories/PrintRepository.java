package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kafara.voting.model.vote.Print;

@Repository
public interface PrintRepository extends JpaRepository<Print, String> {
}
