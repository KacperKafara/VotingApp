package pl.kafara.voting.util.filteringCriterias;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RoleRequestFilteringCriteria extends FilteringCriteria {
    private final String username;
}
