package pl.kafara.voting.util.filteringCriterias;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SurveysFilteringCriteria extends FilteringCriteria {
    private final String title;
    private final String kind;
}
