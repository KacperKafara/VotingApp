package pl.kafara.voting.util.filteringCriterias;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UsersFilteringCriteria extends FilteringCriteria {
    private final String username;
    private final String role;
}
