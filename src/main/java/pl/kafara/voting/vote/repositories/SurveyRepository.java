package pl.kafara.voting.vote.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.model.vote.survey.Survey;
import pl.kafara.voting.model.vote.survey.SurveyKind;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface SurveyRepository extends JpaRepository<Survey, UUID> {

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Optional<Survey> findFirstByOrderByCreatedAtDesc();
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Page<Survey> findAllByTitleContains(Pageable pageable, String title);
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    Page<Survey> findAllByTitleContainsAndSurveyKind(Pageable pageable, String title, SurveyKind kind);
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    @Query("SELECT s FROM Survey s WHERE s.endDate > CURRENT_TIMESTAMP")
    List<Survey> findAllByEndDateAfterNow();
}
