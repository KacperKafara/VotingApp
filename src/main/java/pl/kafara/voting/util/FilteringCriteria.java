package pl.kafara.voting.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class FilteringCriteria {
    private Pageable pageable;
    private String username;
    private String role;
}
