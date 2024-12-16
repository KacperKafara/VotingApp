package pl.kafara.voting.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kafara.voting.exceptions.exceptionCodes.UserExceptionCodes;
import pl.kafara.voting.exceptions.messages.UserMessages;
import pl.kafara.voting.exceptions.user.OAuthGenericException;
import pl.kafara.voting.exceptions.user.OAuthVerificationException;
import pl.kafara.voting.users.dto.OAuth.google.GoogleProfileData;
import pl.kafara.voting.users.dto.OAuth.google.GoogleTokenResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class OAuthUtils {
    @Value("${spring.security.oauth2.client.registration.google.token-uri}")
    String googleOAuth2TokenUrl;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleOAuthClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String googleOAuthClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String googleOAuthRedirectUri;

    public GoogleTokenResponse fetchGoogleTokenResponse(String code) throws GeneralSecurityException, IOException, OAuthVerificationException {
        RestClient restClient = RestClient.create();
        String url = UriComponentsBuilder
            .fromUriString(googleOAuth2TokenUrl)
            .queryParam("client_id", googleOAuthClientId)
            .queryParam("client_secret", googleOAuthClientSecret)
            .queryParam("code", code)
            .queryParam("grant_type", "authorization_code")
            .queryParam("redirect_uri", googleOAuthRedirectUri)
            .build().toUriString();

        ResponseEntity<GoogleTokenResponse> response = restClient.post()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(GoogleTokenResponse.class);

        if (response.getStatusCode().isError()) {
            throw new OAuthGenericException("Error while fetching Google token response");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleOAuthClientId))
                .setIssuer("https://accounts.google.com")
                .build();

        GoogleIdToken idToken = verifier.verify(response.getBody().idToken());
        if (idToken == null) {
            throw new OAuthVerificationException(UserMessages.OAUTH_TOKEN_VERIFICATION_FAILED, UserExceptionCodes.OAUTH_TOKEN_VERIFICATION_FAILED);
        }

        return response.getBody();
    }

    public GoogleProfileData fetchGoogleProfileData(String accessToken) {
        RestClient restClient = RestClient.create();
        String url = UriComponentsBuilder
            .fromUriString("https://people.googleapis.com/v1/people/me")
            .queryParam("personFields", "phoneNumbers,birthdays,emailAddresses,genders,names")
            .build().toUriString();

        ResponseEntity<GoogleProfileData> response = restClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(GoogleProfileData.class);

        if (response.getStatusCode().isError()) {
            throw new OAuthGenericException("Error while fetching Google profile data");
        }

        return response.getBody();
    }
}
