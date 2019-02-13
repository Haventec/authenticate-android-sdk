package com.haventec.testandroidsdk;

import android.content.Context;
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
import com.haventec.authenticate.android.sdk.models.HaventecData;
import com.haventec.testandroidsdk.model.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String applicationUuid;
    private String apiKey;
    private String haventecUsername;
    private String haventecEmail;
    private String pinCode;
    private String serverUrl;

    private UserDetails userDetails;

    private final Context thisActivity = this;
    private HaventecData haventecData;

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

        Properties p = new Properties();
        try {
            p.load(getBaseContext().getAssets().open("app.properties"));

            serverUrl = p.getProperty("serverUrl");
            applicationUuid = p.getProperty("applicationUuid");
            apiKey = p.getProperty("apiKey");
            haventecUsername = p.getProperty("username");
            haventecEmail = p.getProperty("email");
            pinCode = p.getProperty("pinCode");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HaventecAuthenticate.initialiseStorage(thisActivity, haventecUsername);
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

        OkHttpClient client = new OkHttpClient();

        // This demo App is calling directly Haventec Authenticate but a production App
        //should instead send the request to the backend of your application
        //Then the backend can add the x-api-key. The x-api-key is sensitive information and
        //therefore must be secured in the the backend of your App. Please do not expose your apiKey!
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

                String jsonBodyStr = response.body().string();

                try {
                    JSONObject jsonData = new JSONObject(jsonBodyStr);

                    String activationToken = jsonData.getString("activationToken");

                    try {
                        HaventecAuthenticate.updateStorage(thisActivity, jsonData);
                    } catch (HaventecAuthenticateException e) {
                        e.printStackTrace();
                    }

                    activateDevice(activationToken);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void activateDevice(String activationToken) {

        try {
            try {
                haventecData = HaventecAuthenticate.getData(thisActivity);
            } catch (HaventecAuthenticateException e) {
                e.printStackTrace();
            }

            String hashedPin = HaventecAuthenticate.hashPin(thisActivity, pinCode);

            String jsonString = "{"
                    + "\"applicationUuid\": \"" + applicationUuid + "\","
                    + "\"username\": \"" + haventecData.getUsername() + "\","
                    + "\"deviceUuid\": \"" + haventecData.getDeviceUuid() + "\","
                    + "\"hashedPin\": \"" + hashedPin + "\","
                    + "\"activationToken\": \"" + activationToken + "\""
                    + "}";

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonString);

            OkHttpClient client = new OkHttpClient();

            // This demo App is calling directly Haventec Authenticate but a production App
            //should instead send the request to the backend of your application
            //Then the backend can add the x-api-key. The x-api-key is sensitive information and
            //therefore must be secured in the the backend of your App. Please do not expose your apiKey!
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

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    try {
                        JSONObject jsonData = new JSONObject(jsonBodyStr);

                        try {
                            HaventecAuthenticate.updateStorage(thisActivity, jsonData);
                        } catch (HaventecAuthenticateException e) {
                            e.printStackTrace();
                        }

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

        try {
            haventecData = HaventecAuthenticate.getData(thisActivity);
        } catch (HaventecAuthenticateException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        // This demo App is calling directly Haventec Authenticate but a production App
        //should instead send the request to the backend of your application
        //Then the backend can add the x-api-key. The x-api-key is sensitive information and
        //therefore must be secured in the the backend of your App. Please do not expose your apiKey!
        Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .addHeader("x-api-key", apiKey)
                .addHeader("Authorization", "Bearer " + haventecData.getToken().getAccessToken())
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

                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                try {
                    JSONObject jsonData = new JSONObject(jsonBodyStr);
                    userDetails = new UserDetails(jsonData);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            titleView.setText("Hello " + haventecData.getUsername() + ",");
                            userUuidView.setText("Your userUuid is " + userDetails.getUserUuid());
                            lastLoginView.setText("Your lastLogin is " + sdf.format(new Date(userDetails.getLastLogin() * 1000)));
                            dateCreatedView.setText("Your record was created on " + sdf.format(new Date(userDetails.getDateCreated() * 1000)));
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
