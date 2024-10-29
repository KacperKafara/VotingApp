package pl.kafara.voting.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Component
@Slf4j
public class JwsService {
    @Value("${security.jws.token.secret-key:secret-value}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UUID id, Long version) {
        LocalDateTime now = LocalDateTime.now();
        return JWT.create()
                .withIssuer("VotingApp")
                .withClaim("id", id.toString())
                .withClaim("version", version)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean verifySignature(String token, UUID id, Long version) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return !(decodedJWT.getClaim("id").asString().equals(id.toString()) && decodedJWT.getClaim("version").asLong().equals(version) && decodedJWT.getIssuer().equals("VotingApp"));
    }
}
