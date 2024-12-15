package pl.kafara.voting.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.kafara.voting.exceptions.user.OAuthGenericException;
import pl.kafara.voting.users.dto.OAuth.google.GoogleProfileData;
import pl.kafara.voting.users.dto.OAuth.google.GoogleTokenResponse;

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

    public GoogleTokenResponse fetchGoogleTokenResponse(String code) {
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
