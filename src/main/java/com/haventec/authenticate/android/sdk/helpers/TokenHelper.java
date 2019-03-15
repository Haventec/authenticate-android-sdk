package com.haventec.authenticate.android.sdk.helpers;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

public class TokenHelper {

    public static String getUserUuidFromJWT(String jwtToken) throws InvalidJwtException, MalformedClaimException {
        if ( jwtToken == null || jwtToken.length() <= 0 ) {
            return null;
        }
        
        return getJwtClaims(jwtToken).getStringClaimValue("userUUID");
    }

    private static JwtClaims getJwtClaims(String jwtToken) throws InvalidJwtException {
        JwtConsumerBuilder noCheckBuiler = new JwtConsumerBuilder();
        JwtConsumer noCheckCon = noCheckBuiler.setDisableRequireSignature()
                .setSkipDefaultAudienceValidation()
                .setSkipSignatureVerification()
                .setSkipAllValidators()
                .build();
        return noCheckCon.processToClaims(jwtToken);
    }


}
