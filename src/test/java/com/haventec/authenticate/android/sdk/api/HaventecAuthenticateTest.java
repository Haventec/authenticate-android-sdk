package com.haventec.authenticate.android.sdk.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class HaventecAuthenticateTest {


    @Test
    public void testAuthenticate() throws JSONException {

        // Can't add much because the functionality requires a context, but any standalone functionality can be tested here

        String strData = "{\"name\": \"fred\"}";
        JSONObject jsonObject = new JSONObject(strData);

        String deviceUuid = jsonObject.getString("deviceUuid");

        assertNull(deviceUuid);
    }

}
