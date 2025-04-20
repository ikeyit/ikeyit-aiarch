package com.ikeyit.account.interfaces.api.auth.authsession;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public class AuthTokenGenerator {

    private final String claimName;

    private final JwtEncoder jwtEncoder;

    public AuthTokenGenerator(JwtEncoder jwtEncoder) {
        this(jwtEncoder, "asid");
    }

    public AuthTokenGenerator(JwtEncoder jwtEncoder, String claimName) {
        this.jwtEncoder = jwtEncoder;
        this.claimName = claimName;
    }

    public String generateToken(AuthSession authSession) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuedAt(authSession.getCreatedAt())
            .expiresAt(authSession.getExpiresAt())
            .subject(authSession.getPrincipal().getId().toString())
            .claim(claimName, authSession.getId())
            .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
