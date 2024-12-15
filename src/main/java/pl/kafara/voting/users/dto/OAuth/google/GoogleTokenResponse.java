package pl.kafara.voting.users.dto.OAuth.google;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

public record GoogleTokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("expires_in")
        int expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken,
        String scope,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("id_token")
        String idToken
) {
//        @Override
//        public String toString() {
//                return new StringJoiner(", ", GoogleTokenResponse.class.getSimpleName() + "[", "]")
//                        .add("expiresIn=" + expiresIn)
//                        .toString();
//        }
}
