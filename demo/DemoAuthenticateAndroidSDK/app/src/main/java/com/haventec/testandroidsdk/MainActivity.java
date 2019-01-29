package com.haventec.testandroidsdk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
import com.haventec.authenticate.android.sdk.api.exceptions.HaventecAuthenticateException;
import com.haventec.testandroidsdk.model.DeviceDetails;
import com.haventec.testandroidsdk.model.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String applicationUuid = "c09dd8ef-823b-44e5-aa55-e48c39f1b32d";
    private String apiKey = "53517059-8785-4288-87ed-d2002ca0b937";
    private String haventecUsername = "justin";
    private String haventecEmail = "justin.crosbie@haventec.com";
    private String pinCode = "1234";
    private String serverUrl = "https://api.haventec.com/authenticate/v1-2";

    private byte[] salt;
    private String deviceUuid;
    private String authKey;
    private String activationToken;
    private String accessToken;

    private UserDetails userDetails;
    private List<DeviceDetails> devices;

    TextView titleView;
    TextView userUuidView;
    TextView lastLoginView;
    TextView dateCreatedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        titleView = findViewById(R.id.title);
        userUuidView = findViewById(R.id.userUuid);
        lastLoginView = findViewById(R.id.lastLogin);
        dateCreatedView = findViewById(R.id.dateCreated);

        try {
            salt = HaventecAuthenticate.generateSalt();
        } catch (HaventecAuthenticateException e) {
            e.printStackTrace();
        }

        addDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDevice() {

        String jsonString = "{"
                + "\"applicationUuid\": \"" + applicationUuid + "\","
                + "\"username\": \"" + haventecUsername + "\","
                + "\"email\": \"" + haventecEmail + "\","
                + "\"deviceName\": \"Android Device\""
                + "}";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonString);

        System.out.println(jsonString);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .addHeader("x-api-key", apiKey)
                .url(serverUrl + "/self-service/device")
                .post(body)
                .build();

        okhttp3.Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String jsonBodyStr = response.body().string();
                System.out.println(jsonBodyStr);

                try {
                    JSONObject reader = new JSONObject(jsonBodyStr);
                    deviceUuid = reader.getString("deviceUuid");
                    activationToken = reader.getString("activationToken");

                    System.out.println("ActivationToken = " + activationToken);
                    activateDevice();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void activateDevice() {

        try {
            String hashedPin = HaventecAuthenticate.hashPin(pinCode, salt);

            String jsonString = "{"
                    + "\"applicationUuid\": \"" + applicationUuid + "\","
                    + "\"username\": \"" + haventecUsername + "\","
                    + "\"deviceUuid\": \"" + deviceUuid + "\","
                    + "\"hashedPin\": \"" + hashedPin + "\","
                    + "\"activationToken\": \"" + activationToken + "\""
                    + "}";

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonString);

            System.out.println(jsonString);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("Content-type", "application/json")
                    .addHeader("x-api-key", apiKey)
                    .url(serverUrl + "/authentication/activate/device")
                    .post(body)
                    .build();

            okhttp3.Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonBodyStr = response.body().string();
                    System.out.println(jsonBodyStr);

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    try {
                        JSONObject reader = new JSONObject(jsonBodyStr);
                        authKey = reader.getString("authKey");

                        JSONObject tokenJsonObj = reader.getJSONObject("accessToken");
                        accessToken = tokenJsonObj.getString("token");

                        System.out.println("accessToken = " + accessToken);

                        getCurrentUser();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (HaventecAuthenticateException e) {
            e.printStackTrace();
        }
    }


    private void getCurrentUser() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .addHeader("x-api-key", apiKey)
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(serverUrl + "/user/current")
                .get()
                .build();

        okhttp3.Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBodyStr = response.body().string();
                System.out.println(jsonBodyStr);

                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                try {
                    JSONObject reader = new JSONObject(jsonBodyStr);
                    userDetails = new UserDetails(reader);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                        titleView.setText("Hello, " + userDetails.getUsername());
                        userUuidView.setText("Your userUuid is " + userDetails.getUserUuid());
                        lastLoginView.setText("Your lastLogin is " + sdf.format(new Date(userDetails.getLastLogin())));
                        dateCreatedView.setText("Your record was created on " + sdf.format(new Date(userDetails.getDateCreated())));
                        }
                    });

//                    getUserDevices();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

//    private void getUserDevices() {
//
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .addHeader("Content-type", "application/json")
//                .addHeader("x-api-key", apiKey)
//                .addHeader("Authorization", "Bearer " + accessToken)
//                .url(serverUrl + "/user/" + userDetails.getUserUuid() + "/device")
//                .get()
//                .build();
//
//        okhttp3.Call call = client.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException throwable) {
//                throwable.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String jsonBodyStr = response.body().string();
//                System.out.println(jsonBodyStr);
//
//                if (!response.isSuccessful())
//                    throw new IOException("Unexpected code " + response);
//
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++) {
//                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
//
//                try {
//                    JSONObject reader = new JSONObject(jsonBodyStr);
//                    JSONArray devicesJSONArray = reader.getJSONArray("devices");
//
//                    devices = new ArrayList<>();
//                    for ( int i=0; devicesJSONArray!=null && i<devicesJSONArray.length(); i++ ) {
//                        devices.add(new DeviceDetails(devicesJSONArray.getJSONObject(i)));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//    }
}
