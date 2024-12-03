package pl.kafara.voting.util.filteringCriterias;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VotingListFilteringCriteria extends FilteringCriteria {
    private final String title;
    private final Long sitting;
    private final boolean wasActive;
}
