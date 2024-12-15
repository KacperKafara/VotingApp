package pl.kafara.voting.users.dto.OAuth.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.kafara.voting.users.dto.OAuth.google.profileData.*;

import java.util.List;

public record GoogleProfileData(
        List<GoogleName> names,
        List<GoogleGender> genders,
        @JsonProperty("birthdays")
        List<GoogleBirthDate> birthDates,
        List<GoogleEmailAddress> emailAddresses,
        List<GooglePhoneNumber> phoneNumbers
) {
}
