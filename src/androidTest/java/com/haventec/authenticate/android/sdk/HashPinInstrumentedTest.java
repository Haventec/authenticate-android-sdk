package com.haventec.authenticate.android.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HashPinInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.haventec.authenticate.android.sdk.test", appContext.getPackageName());

        try {
            HaventecAuthenticate.initialiseStorage(appContext, "testuser", false);
            String hashPin = HaventecAuthenticate.hashPin("123 4");
            Assert.assertTrue(isValidPin(hashPin));
        } catch (HaventecAuthenticateException e) {
            fail();
        }
    }

    @Test
    public void useAppContextNewSalt() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.haventec.authenticate.android.sdk.test", appContext.getPackageName());

        try {
            HaventecAuthenticate.initialiseStorage(appContext, "testuser", true);
            String hashPin = HaventecAuthenticate.hashPin("123 4");
            Assert.assertTrue(isValidPin(hashPin));
        } catch (HaventecAuthenticateException e) {
            fail();
        }
    }


    private static final Integer SHA512HASH_BASE64_LEN = 88;
    private static final String VALID_BASE64  = "^[A-Za-z0-9+\\/=]{1,}$";

    /**
     * Confirm that the pin is the correct length and only numbers
     *
     * @param pin The pin to validate
     * @return true if it meets the requirements
     */
    public static boolean isValidPin(String pin) {
        return (pin != null && isValidBase64(pin) && pin.length() == SHA512HASH_BASE64_LEN);
    }

    /**
     * Check the string conforms to base64 encoding standards
     *
     * @param strBase64 The incoming string
     * @return true is the string is the correct format
     */
    public static boolean isValidBase64(String strBase64) {
        return (strBase64 != null && strBase64.matches(VALID_BASE64) && (strBase64.length() % 4 == 0));
    }
}
