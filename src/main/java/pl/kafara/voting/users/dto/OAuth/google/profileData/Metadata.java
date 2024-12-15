package pl.kafara.voting.users.dto.OAuth.google.profileData;

public record Metadata(
        boolean primary,
        boolean sourcePrimary,
        boolean verified,
        Source source
) {
}
