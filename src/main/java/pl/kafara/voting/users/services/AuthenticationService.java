package pl.kafara.voting.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kafara.voting.exceptions.ApplicationBaseException;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.exceptions.user.InvalidLoginDataException;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.LoginRequest;
import pl.kafara.voting.users.repositories.UserRepository;
import pl.kafara.voting.util.JwtService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${security.max-failed-attempts:3}")
    private int maxFailedAttempts;

    @PreAuthorize("permitAll()")
    public String authenticate(LoginRequest loginRequest) throws NotFoundException, AccountNotActiveException, InvalidLoginDataException {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!user.isVerified()) throw new AccountNotActiveException("User not verified");
        if(user.isBlocked()) throw new AccountNotActiveException("User blocked");

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            throw new InvalidLoginDataException("Invalid password");
        }

        user.setFailedLoginAttempts(0);
        userRepository.save(user);

        return jwtService.createToken(user.getUsername(), user.getId(), user.getRoles());
    }
}
