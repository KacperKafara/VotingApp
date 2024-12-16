package pl.kafara.voting.users.mapper;

import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.OAuth.google.FillDataDTO;
import pl.kafara.voting.users.dto.OAuth.google.GoogleProfileData;
import pl.kafara.voting.users.dto.OAuth.google.profileData.GoogleEmailAddress;
import pl.kafara.voting.users.dto.OAuth.google.profileData.GoogleName;
import pl.kafara.voting.users.dto.OAuth.google.profileData.GooglePhoneNumber;
import pl.kafara.voting.users.dto.RegistrationRequest;

import java.time.LocalDateTime;
import java.util.Objects;

public class RegistrationMapper {
    private RegistrationMapper() {
    }

    public static User mapToUser(RegistrationRequest registrationRequest) {
        return new User(
                registrationRequest.firstName(),
                registrationRequest.lastName(),
                registrationRequest.phoneNumber(),
                registrationRequest.birthDate(),
                registrationRequest.username(),
                registrationRequest.email(),
                registrationRequest.language()
        );
    }

    public static User googleProfileDataToUser(GoogleProfileData googleProfileData, String subject) {
        String firstName = googleProfileData.names() != null ? googleProfileData.names()
                .stream()
                .filter(name -> name.metadata().primary())
                .findFirst()
                .map(GoogleName::givenName)
                .orElse(null) : null;

        String lastName = googleProfileData.names() != null ? googleProfileData.names()
                .stream()
                .filter(name -> name.metadata().primary())
                .findFirst()
                .map(GoogleName::familyName)
                .orElse(null) : null;

        String phoneNumber = googleProfileData.phoneNumbers() != null ? googleProfileData.phoneNumbers()
                .stream()
                .filter(number -> number.metadata().primary())
                .findFirst()
                .map(GooglePhoneNumber::canonicalForm)
                .orElse(null) : null;

        String email = googleProfileData.emailAddresses() != null ? googleProfileData.emailAddresses()
                .stream()
                .filter(emailAddress -> emailAddress.metadata().primary())
                .findFirst()
                .map(GoogleEmailAddress::value)
                .orElse(null) : null;

        String username = email != null ? email.split("@")[0] : null;

        LocalDateTime birthDate = googleProfileData.birthDates() != null ? googleProfileData.birthDates()
                .stream()
                .filter(bd -> bd.date().day() != null &&
                        bd.date().month() != null &&
                        bd.date().year() != null)
                .findFirst()
                .map(data -> LocalDateTime.of(data.date().year(), data.date().month(), data.date().day(), 0, 0))
                .orElse(null) : null;

        User user = new User(
                firstName,
                lastName,
                phoneNumber,
                birthDate,
                username,
                email,
                "pl"
        );
        user.setOAuthId(subject);
        return user;
    }

    public static User fillDataDTOToUser(FillDataDTO fillData) {
        return new User(
                fillData.firstName(),
                fillData.lastName(),
                fillData.phoneNumber(),
                fillData.birthDate(),
                fillData.username(),
                fillData.email(),
                "pl"
        );
    }
}
