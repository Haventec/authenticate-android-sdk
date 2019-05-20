package com.haventec.authenticate.android.sdk.helpers;

import com.haventec.authenticate.android.sdk.api.exceptions.AuthenticateError;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

public class TokenHelper {

    public static String getUserUuidFromJWT(String jwtToken) {
        if ( jwtToken == null || jwtToken.length() <= 0 ) {
            return null;
        }

        try {
            return getJwtClaims(jwtToken).getStringClaimValue(Constants.JWT_CLAIM_USERUUID);
        } catch (MalformedClaimException e) {
            throw new HaventecAuthenticateException(AuthenticateError.JWT_MALFORMED_CLAIMS_ERROR, e);
        }
    }

    private static JwtClaims getJwtClaims(String jwtToken) {
        JwtConsumerBuilder jwtConsumerBuilder = new JwtConsumerBuilder();

        if ( jwtConsumerBuilder == null ) {
            throw new HaventecAuthenticateException(AuthenticateError.CONSUMER_BUILDER_IS_NULL);
        }

        JwtConsumer jwtConsumer = jwtConsumerBuilder.setDisableRequireSignature()
                .setSkipDefaultAudienceValidation()
                .setSkipSignatureVerification()
                .setSkipAllValidators()
                .build();
        try {
            return jwtConsumer.processToClaims(jwtToken);
        } catch (InvalidJwtException e) {
            throw new HaventecAuthenticateException(AuthenticateError.JWT_INVALID_ERROR, e);
        }
    }


}
