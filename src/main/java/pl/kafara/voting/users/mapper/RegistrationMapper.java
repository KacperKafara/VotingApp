package pl.kafara.voting.users.mapper;

import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.RegistrationRequest;

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
}
