package pl.kafara.voting.users.dto.OAuth.google.profileData;

public record GoogleName(
        Metadata metadata,
        String familyName,
        String givenName
) {
}
