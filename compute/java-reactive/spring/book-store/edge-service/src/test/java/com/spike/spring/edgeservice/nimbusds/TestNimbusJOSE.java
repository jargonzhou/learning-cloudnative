package com.spike.spring.edgeservice.nimbusds;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class TestNimbusJOSE {

    // https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-rsa-signature
    @Test
    public void testRSASignedJWT() {
        try {
            // RSA key pair
            RSAKey rsaKey = new RSAKeyGenerator(2048)
                    .keyID("issuer")
                    .generate();

            RSAKey rsaPublicKey = rsaKey.toPublicJWK();

            // sign
            JWSSigner signer = new RSASSASigner(rsaKey);
            Date now = new Date();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer("https://openid.net")
                    .subject("alice")
                    .audience(Arrays.asList("https://app-one.com", "https://app-two.com"))
                    .expirationTime(new Date(now.getTime() + 1000 * 60 * 10)) // expires in 10 minutes
                    .notBeforeTime(now)
                    .issueTime(now)
                    .jwtID(UUID.randomUUID().toString())
                    .claim("x-auth", System.currentTimeMillis())
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                    claims);
            signedJWT.sign(signer);
            String s = signedJWT.serialize();
            System.out.println("s=" + s);

            // verify
            SignedJWT signedJWTOut = SignedJWT.parse(s);
            JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);
            boolean result = signedJWTOut.verify(verifier);
            Assertions.assertThat(result).isTrue();
            System.out.println(signedJWTOut.getJWTClaimsSet());

        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
