package pl.kafara.voting.util.filteringCriterias;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
public class VotingListFilteringCriteria extends FilteringCriteria {
    private final String title;
    private final UUID sitting;
    private final boolean wasActive;
}
