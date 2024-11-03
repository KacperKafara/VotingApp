package pl.kafara.voting.util.filteringCriterias;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@Getter
@SuperBuilder
public abstract class FilteringCriteria {
    private final Pageable pageable;
}
