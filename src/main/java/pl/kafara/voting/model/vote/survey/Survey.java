package pl.kafara.voting.model.vote.survey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kafara.voting.model.AbstractEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "surveys")
public class Survey extends AbstractEntity implements Serializable {

    @Column(name = "title",nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private SurveyKind surveyKind;

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER)
    private List<UserVoteSurvey> userVotes = new ArrayList<>();
}
