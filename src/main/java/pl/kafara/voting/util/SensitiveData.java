package pl.kafara.voting.util;

import java.util.StringJoiner;

public record SensitiveData(
        String data
) {

    public SensitiveData {
        if (data == null)
            throw new IllegalArgumentException("data cannot be null");
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SensitiveData.class.getSimpleName() + "[", "]")
                .add("data='**********'")
                .toString();
    }
}
