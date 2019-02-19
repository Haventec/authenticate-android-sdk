package com.haventec.authenticate.android.sdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

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
    String activateDeviceResponseJson2 = "{\"responseStatus\":{\"status\":\"SUCCESS\",\"message\":\"Changed\",\"code\":\"\"},\"authKey\":\"AX4xNhyVoY7lFzmOLmfFo1PbgZYz2oN4THu5/CgDikwg3epdy5a3cIqn2Xk8sHqG3YyQricznA7RZINwxmC2llcmppwn9gx9C0MSmGld7Fs/WtDWRqHQzW5kvBPkyYoArON4cdP5kga4Bbi97Jx4aR/w0EQ6sxD8gL35kM6wdA39oxzeTt5lhBqLzhXshxOBd4cUVQtBCGV9fFM0YPmMDa76kQtiP6ed2PdPJ/sowBpAGBgxiFyxGoPg1PqQ4FJEq0P4rhYwR02WU3sS6nqg4Ql/nrCj1bWl97kHHFhrAZJxEaQwMoffQzY1XfjhS2zKCWjYpHLeZ7zvZi8caR0T/gjCYaBx9egdM3wzkyftIRbpLo4iGJj9HUbjKitjFqL1Q7jiqQTXYwJins8XVmh/007jft2K3l7tLCI8M0wsXQqhP5i7kf6jS6UIhtuI5vlx6LWyw4ywOJjEuQxRrbS8GA==\",\"accessToken\":{\"type\":\"JWT\",\"token\":\"eyJhbGciOiJFUzM4NCJ9.eyJpc3MiOiJ0ZXN0IGFwcCIsImV4cCI6MTU0OTk0ODk2NiwiaWF0IjoxNTQ5OTQ0NDY2LCJuYmYiOjE1NDk5NDQzNDYsInN1YiI6ImpjaGFwaTE4XzIiLCJyb2xlIjpbIkhUX0FOX1VTRVIiXSwiYXBwbGljYXRpb25VVUlEIjoiYzA5ZGQ4ZWYtODIzYi00NGU1LWFhNTUtZTQ4YzM5ZjFiMzJkIiwidXNlclVVSUQiOiIzZTIzM2JiYS02NDlhLTRlMjgtYTE0ZS1kYTIyYjIxOTIyNzMiLCJqdGkiOiIyMmc1Z0Q0X3ctc2t0Z1J3V19pVm9BIn0.MSD3kQdVGsAvS3RiRJN5QOP_h6va4Ww6YdHXkS-uls4qxbqcBoTCskL06GWgA5gIY3dqakqlwd9kAYACG4QpphvP0Zu5FEc7_eDhoWE7UesrtJmB5Me8VVlPoxkpDl3x\"}}";

    @Test
    public void basicStorageTests() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        String testUserName = UUID.randomUUID().toString();
        String testUserName2 = UUID.randomUUID().toString();

        try {
            HaventecAuthenticate.initialiseStorage(appContext, testUserName);

            // First, test that values get set appropriately

            Assert.assertEquals(testUserName, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(addDeviceResponseJson));

            Assert.assertNotNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(activateDeviceResponseJson));

            Assert.assertNotNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertNotNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNotNull(HaventecAuthenticate.getAccessToken());

            String firstUsername = HaventecAuthenticate.getUsername();
            String firstAuthKey = HaventecAuthenticate.getAuthKey();
            String firstDeviceUuid = HaventecAuthenticate.getDeviceUuid();
            String firstAccessToken = HaventecAuthenticate.getAccessToken();

            //
            // Now test onboarding a second user
            //

            HaventecAuthenticate.initialiseStorage(appContext, testUserName2);

            Assert.assertEquals(testUserName2, HaventecAuthenticate.getUsername());
            Assert.assertNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());

            HaventecAuthenticate.updateStorage(appContext, new JSONObject(addDeviceResponseJson2));
            HaventecAuthenticate.updateStorage(appContext, new JSONObject(activateDeviceResponseJson2));

            Assert.assertNotNull(HaventecAuthenticate.getAuthKey());
            Assert.assertNotNull(HaventecAuthenticate.getDeviceUuid());
            Assert.assertNotNull(HaventecAuthenticate.getAccessToken());

            String secondUsername = HaventecAuthenticate.getUsername();
            String secondAuthKey = HaventecAuthenticate.getAuthKey();
            String secondDeviceUuid = HaventecAuthenticate.getDeviceUuid();
            String secondAccessToken = HaventecAuthenticate.getAccessToken();

            Assert.assertNotEquals(firstDeviceUuid, secondDeviceUuid);
            Assert.assertNotEquals(firstAuthKey, secondAuthKey);
            Assert.assertNotEquals(firstAccessToken, secondAccessToken);
            Assert.assertNotEquals(firstUsername, secondUsername);

            //
            // Now test that switching back to the first user retains the context, so we haven't lost any data
            //

            HaventecAuthenticate.initialiseStorage(appContext, testUserName);

            Assert.assertEquals(firstDeviceUuid, HaventecAuthenticate.getDeviceUuid());

            Assert.assertEquals(firstAuthKey, HaventecAuthenticate.getAuthKey());
            Assert.assertNull(HaventecAuthenticate.getAccessToken());
            Assert.assertEquals(firstDeviceUuid, HaventecAuthenticate.getDeviceUuid());
            Assert.assertEquals(firstUsername, HaventecAuthenticate.getUsername());

        } catch (HaventecAuthenticateException e) {
            fail();
        } catch (JSONException e) {
            fail();
        }
    }
}
