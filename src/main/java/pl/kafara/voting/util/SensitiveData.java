package pl.kafara.voting.util;

import java.util.StringJoiner;

public record SensitiveData(
        String data
) {
    @Override
    public String toString() {
        return new StringJoiner(", ", SensitiveData.class.getSimpleName() + "[", "]")
                .add("data='**********'")
                .toString();
    }
}
