package pl.kafara.voting.users.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kafara.voting.exceptions.NotFoundException;
import pl.kafara.voting.exceptions.messages.GenericMessages;
import pl.kafara.voting.exceptions.user.AccountNotActiveException;
import pl.kafara.voting.model.users.GenderEnum;
import pl.kafara.voting.model.users.User;
import pl.kafara.voting.users.dto.LoginResponse;
import pl.kafara.voting.users.dto.OAuth.google.GoogleProfileData;
import pl.kafara.voting.users.dto.OAuth.google.GoogleTokenResponse;
import pl.kafara.voting.users.dto.OAuth.google.OAuth2UrlResponse;
import pl.kafara.voting.users.dto.OAuth.google.profileData.GoogleGender;
import pl.kafara.voting.users.mapper.RegistrationMapper;
import pl.kafara.voting.users.services.AuthenticationService;
import pl.kafara.voting.users.services.EmailService;
import pl.kafara.voting.users.services.UserService;
import pl.kafara.voting.util.OAuthUtils;
import pl.kafara.voting.util.SensitiveData;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class OAuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    private final OAuthUtils oAuthUtils;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleOAuthClientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String googleOAuthRedirectUri;
    @Value("${spring.security.oauth2.client.registration.google.auth-uri}")
    String googleOAuth2Url;

    @PreAuthorize("permitAll()")
    @GetMapping("/oauth/google")
    public ResponseEntity<OAuth2UrlResponse> googleOAuth() {
        String url = UriComponentsBuilder
                .fromUriString(googleOAuth2Url)
                .queryParam("client_id", googleOAuthClientId)
                .queryParam("redirect_uri", googleOAuthRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email https://www.googleapis.com/auth/user.gender.read https://www.googleapis.com/auth/user.birthday.read https://www.googleapis.com/auth/user.phonenumbers.read")
                .queryParam("access_type", "offline")
                .queryParam("state", "standard_oauth")
                .queryParam("prompt", "consent")
                .build().toUriString();
        return ResponseEntity.ok(new OAuth2UrlResponse(url));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/oauth/google/token")
    public ResponseEntity<?> googleOAuthToken(@RequestParam String code) throws NotFoundException, NoSuchAlgorithmException {
        GoogleTokenResponse response = oAuthUtils.fetchGoogleTokenResponse(code);
        DecodedJWT jwt = JWT.decode(response.idToken());
        String subject = jwt.getSubject();

        if (userService.getUserByOAuthId(subject) != null) {
            try {
                Map<String, SensitiveData> data = authenticationService.authenticate(new SensitiveData(subject));
                if (data.containsKey("username")) {
                    return ResponseEntity.ok(data);
                }
                LoginResponse loginResponse = new LoginResponse(data.get("token").data(), data.get("refreshToken").data());
                return ResponseEntity.ok(loginResponse);
            } catch (AccountNotActiveException e) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
            }
        }

        GoogleProfileData googleProfileData = oAuthUtils.fetchGoogleProfileData(response.accessToken());
        String gender = googleProfileData.genders()
                .stream()
                .filter(g -> g.metadata().primary())
                .findFirst()
                .map(GoogleGender::value)
                .orElse(null);
        GenderEnum genderEnum;
        if (gender != null && (!gender.equals("male") && !gender.equals("female")))
            genderEnum = GenderEnum.OTHER;
        else if (gender != null)
            genderEnum = GenderEnum.valueOf(gender.toUpperCase());
        else
            genderEnum = null;

        User user = RegistrationMapper.googleProfileDataToUser(googleProfileData, subject);

        SensitiveData token = authenticationService.register(user, genderEnum);
        if (token == null || token.data().isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, GenericMessages.SOMETHING_WENT_WRONG);
        emailService.sendAccountVerificationEmail(user.getEmail(), token, user.getFirstName(), user.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
