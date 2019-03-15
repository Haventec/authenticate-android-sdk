package com.haventec.authenticate.android.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StorageInstrumentedTests {

    String addDeviceResponseJson = "{\"userEmail\":\"justincrosbie@gmail.com\",\"activationToken\":\"493552\",\"deviceUuid\":\"c4acafff-f4be-4d06-b7f6-ab3f16deb50b\",\"mobileNumber\":null,\"responseStatus\":{\"status\":\"SUCCESS\",\"message\":\"Created\",\"code\":\"\"}}";
    String activateDeviceResponseJson = "{\"responseStatus\":{\"status\":\"SUCCESS\",\"message\":\"Changed\",\"code\":\"\"},\"authKey\":\"OX4xNhyVoY7lFzmOLmfFo1PbgZYz2oN4THu5/CgDikwg3epdy5a3cIqn2Xk8sHqG3YyQricznA7RZINwxmC2llcmppwn9gx9C0MSmGld7Fs/WtDWRqHQzW5kvBPkyYoArON4cdP5kga4Bbi97Jx4aR/w0EQ6sxD8gL35kM6wdA39oxzeTt5lhBqLzhXshxOBd4cUVQtBCGV9fFM0YPmMDa76kQtiP6ed2PdPJ/sowBpAGBgxiFyxGoPg1PqQ4FJEq0P4rhYwR02WU3sS6nqg4Ql/nrCj1bWl97kHHFhrAZJxEaQwMoffQzY1XfjhS2zKCWjYpHLeZ7zvZi8caR0T/gjCYaBx9egdM3wzkyftIRbpLo4iGJj9HUbjKitjFqL1Q7jiqQTXYwJins8XVmh/007jft2K3l7tLCI8M0wsXQqhP5i7kf6jS6UIhtuI5vlx6LWyw4ywOJjEuQxRrbS8GQ==\",\"accessToken\":{\"type\":\"JWT\",\"token\":\"eyJhbGciOiJFUzM4NCJ9.eyJpc3MiOiJ0ZXN0IGFwcCIsImV4cCI6MTU0OTk0ODk2NiwiaWF0IjoxNTQ5OTQ0NDY2LCJuYmYiOjE1NDk5NDQzNDYsInN1YiI6ImpjaGFwaTE4XzIiLCJyb2xlIjpbIkhUX0FOX1VTRVIiXSwiYXBwbGljYXRpb25VVUlEIjoiYzA5ZGQ4ZWYtODIzYi00NGU1LWFhNTUtZTQ4YzM5ZjFiMzJkIiwidXNlclVVSUQiOiIzZTIzM2JiYS02NDlhLTRlMjgtYTE0ZS1kYTIyYjIxOTIyNzMiLCJqdGkiOiIyMmc1Z0Q0X3ctc2t0Z1J3V19pVm9BIn0.MSD3kQdVGsAvS3RiRJN5QOP_h6va4Ww6YdHXkS-uls4qxbqcBoTCskL06GWgA5gIY3dqakqlwd9kAYACG4QpphvP0Zu5FEc7_eDhoWE7UesrtJmB5Me8VVlPoxkpDl3t\"}}";
    String addDeviceResponseJson2 = "{\"userEmail\":\"justincrosbie2@gmail.com\",\"activationToken\":\"493554\",\"deviceUuid\":\"c4acafff-f4be-4d06-b7f6-ab3f16deb51a\",\"mobileNumber\":null,\"responseStatus\":{\"status\":\"SUCCESS\",\"message\":\"Created\",\"code\":\"\"}}";
    String activateDeviceResponseJson2 = "{\"responseStatus\":{\"status\":\"SUCCESS\",\"message\":\"Changed\",\"code\":\"\"},\"authKey\":\"AX4xNhyVoY7lFzmOLmfFo1PbgZYz2oN4THu5/CgDikwg3epdy5a3cIqn2Xk8sHqG3YyQricznA7RZINwxmC2llcmppwn9gx9C0MSmGld7Fs/WtDWRqHQzW5kvBPkyYoArON4cdP5kga4Bbi97Jx4aR/w0EQ6sxD8gL35kM6wdA39oxzeTt5lhBqLzhXshxOBd4cUVQtBCGV9fFM0YPmMDa76kQtiP6ed2PdPJ/sowBpAGBgxiFyxGoPg1PqQ4FJEq0P4rhYwR02WU3sS6nqg4Ql/nrCj1bWl97kHHFhrAZJxEaQwMoffQzY1XfjhS2zKCWjYpHLeZ7zvZi8caR0T/gjCYaBx9egdM3wzkyftIRbpLo4iGJj9HUbjKitjFqL1Q7jiqQTXYwJins8XVmh/007jft2K3l7tLCI8M0wsXQqhP5i7kf6jS6UIhtuI5vlx6LWyw4ywOJjEuQxRrbS8GA==\",\"accessToken\":{\"type\":\"JWT\",\"token\":\"eyJhbGciOiJFUzM4NCJ9.eyJpc3MiOiJIYXZlbnRlYyIsImV4cCI6MTU1MjYyMjA3NywiaWF0IjoxNTUyNjE4NDc3LCJuYmYiOjE1NTI2MTgzNTcsInN1YiI6ImFkbWluIiwicm9sZSI6WyJIVF9BRE1JTiJdLCJhcHBsaWNhdGlvblVVSUQiOiI0ZGRlMmNlNS1iYzY4LTRhNmUtOGQyYS1jNzZkNzY5NTM4MWYiLCJ1c2VyVVVJRCI6ImVhYmJlNDBlLWM0NWMtNGI1NS1iYjM2LWNkN2QwZmMwNjcxYyIsImp0aSI6Ii1OZXU1US01MmFyXzhBXzcwcUZibmcifQ.wtgpkjqt-yNdXASu6iWo-oZSqIlN_D_ALRDCNY5APhOCEqlLNmPh8lBi56gr_6s9zfWHzKPo-wF_3vV-SfyLMPGUmIPpefDYNDoaVhmfvbElvFveRR1btIjGq_5zKMyt\"}}";

    String deviceUuid1 = "c4acafff-f4be-4d06-b7f6-ab3f16deb50b";
    String authKey1 = "OX4xNhyVoY7lFzmOLmfFo1PbgZYz2oN4THu5/CgDikwg3epdy5a3cIqn2Xk8sHqG3YyQricznA7RZINwxmC2llcmppwn9gx9C0MSmGld7Fs/WtDWRqHQzW5kvBPkyYoArON4cdP5kga4Bbi97Jx4aR/w0EQ6sxD8gL35kM6wdA39oxzeTt5lhBqLzhXshxOBd4cUVQtBCGV9fFM0YPmMDa76kQtiP6ed2PdPJ/sowBpAGBgxiFyxGoPg1PqQ4FJEq0P4rhYwR02WU3sS6nqg4Ql/nrCj1bWl97kHHFhrAZJxEaQwMoffQzY1XfjhS2zKCWjYpHLeZ7zvZi8caR0T/gjCYaBx9egdM3wzkyftIRbpLo4iGJj9HUbjKitjFqL1Q7jiqQTXYwJins8XVmh/007jft2K3l7tLCI8M0wsXQqhP5i7kf6jS6UIhtuI5vlx6LWyw4ywOJjEuQxRrbS8GQ==";
    String accessToken1 = "eyJhbGciOiJFUzM4NCJ9.eyJpc3MiOiJ0ZXN0IGFwcCIsImV4cCI6MTU0OTk0ODk2NiwiaWF0IjoxNTQ5OTQ0NDY2LCJuYmYiOjE1NDk5NDQzNDYsInN1YiI6ImpjaGFwaTE4XzIiLCJyb2xlIjpbIkhUX0FOX1VTRVIiXSwiYXBwbGljYXRpb25VVUlEIjoiYzA5ZGQ4ZWYtODIzYi00NGU1LWFhNTUtZTQ4YzM5ZjFiMzJkIiwidXNlclVVSUQiOiIzZTIzM2JiYS02NDlhLTRlMjgtYTE0ZS1kYTIyYjIxOTIyNzMiLCJqdGkiOiIyMmc1Z0Q0X3ctc2t0Z1J3V19pVm9BIn0.MSD3kQdVGsAvS3RiRJN5QOP_h6va4Ww6YdHXkS-uls4qxbqcBoTCskL06GWgA5gIY3dqakqlwd9kAYACG4QpphvP0Zu5FEc7_eDhoWE7UesrtJmB5Me8VVlPoxkpDl3t";
    String deviceUuid2 = "c4acafff-f4be-4d06-b7f6-ab3f16deb51a";
    String authKey2 = "AX4xNhyVoY7lFzmOLmfFo1PbgZYz2oN4THu5/CgDikwg3epdy5a3cIqn2Xk8sHqG3YyQricznA7RZINwxmC2llcmppwn9gx9C0MSmGld7Fs/WtDWRqHQzW5kvBPkyYoArON4cdP5kga4Bbi97Jx4aR/w0EQ6sxD8gL35kM6wdA39oxzeTt5lhBqLzhXshxOBd4cUVQtBCGV9fFM0YPmMDa76kQtiP6ed2PdPJ/sowBpAGBgxiFyxGoPg1PqQ4FJEq0P4rhYwR02WU3sS6nqg4Ql/nrCj1bWl97kHHFhrAZJxEaQwMoffQzY1XfjhS2zKCWjYpHLeZ7zvZi8caR0T/gjCYaBx9egdM3wzkyftIRbpLo4iGJj9HUbjKitjFqL1Q7jiqQTXYwJins8XVmh/007jft2K3l7tLCI8M0wsXQqhP5i7kf6jS6UIhtuI5vlx6LWyw4ywOJjEuQxRrbS8GA==";
    String accessToken2 = "eyJhbGciOiJFUzM4NCJ9.eyJpc3MiOiJIYXZlbnRlYyIsImV4cCI6MTU1MjYyMjA3NywiaWF0IjoxNTUyNjE4NDc3LCJuYmYiOjE1NTI2MTgzNTcsInN1YiI6ImFkbWluIiwicm9sZSI6WyJIVF9BRE1JTiJdLCJhcHBsaWNhdGlvblVVSUQiOiI0ZGRlMmNlNS1iYzY4LTRhNmUtOGQyYS1jNzZkNzY5NTM4MWYiLCJ1c2VyVVVJRCI6ImVhYmJlNDBlLWM0NWMtNGI1NS1iYjM2LWNkN2QwZmMwNjcxYyIsImp0aSI6Ii1OZXU1US01MmFyXzhBXzcwcUZibmcifQ.wtgpkjqt-yNdXASu6iWo-oZSqIlN_D_ALRDCNY5APhOCEqlLNmPh8lBi56gr_6s9zfWHzKPo-wF_3vV-SfyLMPGUmIPpefDYNDoaVhmfvbElvFveRR1btIjGq_5zKMyt";

    String badJson = "uh-oh";

    String testUserName1 = "testuser1";
    String testUserName2 = "testuser2";

    // Derived from the accessTokens above
    String userUuid1 = "3e233bba-649a-4e28-a14e-da22b2192273";
    String userUuid2 = "eabbe40e-c45c-4b55-bb36-cd7d0fc0671c";


    @Before
    public void setup() {
        destroyTestUserPreferenceData();
    }

    @After
    public void teardown() {
        destroyTestUserPreferenceData();
    }

    @Test
    public void testInitialiseStorage() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Assert.assertNotNull(HaventecAuthenticate.getDeviceName());
        Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());

        try {
            HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

            // First, test that values get set appropriately

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

        } catch (HaventecAuthenticateException e) {
            fail();
        }
    }

    @Test
    public void testUpdateStorage() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        try {
            HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

            // First, test that values get set appropriately

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(addDeviceResponseJson));

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertEquals(deviceUuid1, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(activateDeviceResponseJson));

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertEquals(deviceUuid1, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertEquals(authKey1, HaventecAuthenticate.getAuthKey());
            Assert.assertEquals(accessToken1, HaventecAuthenticate.getAccessToken());
            Assert.assertEquals(userUuid1, HaventecAuthenticate.getUserUuid());
        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testClearAccessToken() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        try {
            HaventecAuthenticate.initialiseStorage(appContext, testUserName1);
            HaventecAuthenticate.updateStorage(appContext, new JSONObject(addDeviceResponseJson));
            HaventecAuthenticate.updateStorage(appContext, new JSONObject(activateDeviceResponseJson));

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertEquals(deviceUuid1, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertEquals(authKey1, HaventecAuthenticate.getAuthKey());
            Assert.assertEquals(accessToken1, HaventecAuthenticate.getAccessToken());
            Assert.assertEquals(userUuid1, HaventecAuthenticate.getUserUuid());

            HaventecAuthenticate.clearAccessToken();

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertEquals(deviceUuid1, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertEquals(authKey1, HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

        } catch (JSONException e) {
            fail();
        }
    }

    @Test
    public void testUpdateStorage_Fail_Bad_Json() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

        try {
            HaventecAuthenticate.updateStorage(appContext, new JSONObject(badJson));
            fail();
        } catch (JSONException e) {
        }
    }

    @Test
    public void testHashPin_AlwaysProducesSameHashForPIN() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

        String hashPIN1 = HaventecAuthenticate.hashPin("1234");
        String hashPIN2 = HaventecAuthenticate.hashPin("1234");

        assertEquals(hashPIN1, hashPIN2);

        HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

        String hashPIN3 = HaventecAuthenticate.hashPin("1234");

        assertEquals(hashPIN1, hashPIN3);

        HaventecAuthenticate.initialiseStorage(appContext, testUserName2);

        String hashPIN4 = HaventecAuthenticate.hashPin("1234");

        assertNotEquals(hashPIN1, hashPIN4);

        HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

        String hashPIN5 = HaventecAuthenticate.hashPin("1234");

        assertEquals(hashPIN1, hashPIN5);
    }


    @Test
    public void testTwoUsersSwitch() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        try {
            HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

            // First, test that values get set appropriately

            Assert.assertEquals(testUserName1, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(addDeviceResponseJson));

            Assert.assertEquals(deviceUuid1, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(activateDeviceResponseJson));

            Assert.assertEquals(deviceUuid1, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertEquals(authKey1, HaventecAuthenticate.getAuthKey());
            Assert.assertEquals(accessToken1, HaventecAuthenticate.getAccessToken());
            Assert.assertEquals(userUuid1, HaventecAuthenticate.getUserUuid());

            String firstUsername = HaventecAuthenticate.getUsername();
            String firstAuthKey = HaventecAuthenticate.getAuthKey();
            String firstDeviceUuid = HaventecAuthenticate.getDeviceUuid();
            String firstAccessToken = HaventecAuthenticate.getAccessToken();
            String firstUserUuid = HaventecAuthenticate.getUserUuid();

            //
            // Now test onboarding a second user
            //

            HaventecAuthenticate.initialiseStorage(appContext, testUserName2);

            Assert.assertEquals(testUserName2, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(addDeviceResponseJson2));
            HaventecAuthenticate.updateStorage(appContext, new JSONObject(activateDeviceResponseJson2));

            Assert.assertEquals(deviceUuid2, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertEquals(authKey2, HaventecAuthenticate.getAuthKey());
            Assert.assertEquals(accessToken2, HaventecAuthenticate.getAccessToken());
            Assert.assertEquals(userUuid2, HaventecAuthenticate.getUserUuid());

            String secondUsername = HaventecAuthenticate.getUsername();
            String secondAuthKey = HaventecAuthenticate.getAuthKey();
            String secondDeviceUuid = HaventecAuthenticate.getDeviceUuid();
            String secondAccessToken = HaventecAuthenticate.getAccessToken();
            String secondUserUuid = HaventecAuthenticate.getUserUuid();

            Assert.assertNotEquals(firstDeviceUuid, secondDeviceUuid);
            Assert.assertNotEquals(firstAuthKey, secondAuthKey);
            Assert.assertNotEquals(firstAccessToken, secondAccessToken);
            Assert.assertNotEquals(firstUsername, secondUsername);
            Assert.assertNotEquals(firstUserUuid, secondUserUuid);

            //
            // Now test that switching back to the first user retains the context, so we haven't lost any data
            //

            HaventecAuthenticate.initialiseStorage(appContext, testUserName1);

            Assert.assertEquals(firstDeviceUuid, HaventecAuthenticate.getDeviceUuid());

            Assert.assertEquals(firstAuthKey, HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertEquals(firstDeviceUuid, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(android.os.Build.MODEL, HaventecAuthenticate.getDeviceName());
            Assert.assertEquals(firstUsername, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getUserUuid());

        } catch (HaventecAuthenticateException e) {
            fail();
        } catch (JSONException e) {
            fail();
        }
    }

    private void destroyTestUserPreferenceData() {

        Context context = InstrumentationRegistry.getTargetContext();

        SharedPreferences.Editor editorGlobal = context.getSharedPreferences(context.getString(R.string.haventec_preference_file_key), Context.MODE_PRIVATE).edit();

        SharedPreferences.Editor editor1 = context.getSharedPreferences(context.getString(R.string.haventec_preference_file_key) + "_" + testUserName1, Context.MODE_PRIVATE).edit();
        SharedPreferences.Editor editor2 = context.getSharedPreferences(context.getString(R.string.haventec_preference_file_key) + "_" + testUserName2, Context.MODE_PRIVATE).edit();

        editor1.clear();
        editor1.commit();

        editor2.clear();
        editor2.commit();

        editorGlobal.clear();
        editorGlobal.commit();
    }
}
