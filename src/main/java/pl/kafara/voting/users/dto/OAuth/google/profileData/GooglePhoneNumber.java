package pl.kafara.voting.users.dto.OAuth.google.profileData;

public record GooglePhoneNumber(
        Metadata metadata,
        String canonicalForm,
        String value
) {
}
