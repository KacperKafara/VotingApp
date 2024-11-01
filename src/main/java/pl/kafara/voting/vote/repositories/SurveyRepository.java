package pl.kafara.voting.vote.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.survey.Survey;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface SurveyRepository extends JpaRepository<Survey, UUID> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    @Query("SELECT s FROM Survey s ORDER BY s.createdAt DESC")
    Optional<Survey> findLatest();
}
