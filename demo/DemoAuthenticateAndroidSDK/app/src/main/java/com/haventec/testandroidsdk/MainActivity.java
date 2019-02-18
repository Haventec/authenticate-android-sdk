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
import java.util.UUID;

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
    private String serverUrl;

    private UserDetails userDetails;
    private String userPin = "123456";

    private final Context thisActivity = this;

    TextView titleView;
    TextView userUuidView;
    TextView lastLoginView;
    TextView dateCreatedView;
    TextView currentUsername;
    TextView deviceUuid;
    TextView deviceAuthKey;
    TextView newDeviceAuthKey;

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
        currentUsername = findViewById(R.id.currentUsername);
        deviceUuid = findViewById(R.id.deviceUuid);
        deviceAuthKey = findViewById(R.id.deviceAuthKey);
        newDeviceAuthKey = findViewById(R.id.newDeviceAuthKey);


        Properties p = new Properties();
        try {
            p.load(getBaseContext().getAssets().open("app.properties"));

            // This property must be defined in the backend and not the frontend
            //this is just a demo, please don't expose your apiKey.
            serverUrl = p.getProperty("serverUrl");
            applicationUuid = p.getProperty("applicationUuid");
            apiKey = p.getProperty("apiKey");

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Users have to type their username, and email
        String username = "AndroidUser_1234";
        String email = "android.user@mail.com";
        try {
            // This is the first call that you need to do in order to initialise
            //the Storage for a specific user.
            HaventecAuthenticate.initialiseStorage(thisActivity, username);
        } catch (HaventecAuthenticateException e) {
            e.printStackTrace();
        }


        //Uncomment on of the following options. Try first to sign up the user,
        //then you can comment that one and add a new device for the same user

        // User Sign up
        signUpUser(email);

        // User activates account
        //coming soon

        // User retrieve user details
        //coming soon

        // User log out
        //coming soon

        // User log in
        //coming soon

        // User adds a new device
//        addNewDevice(username);

        // User get devices info
        //coming soon

        // User reset device PIN
        //coming soon


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

    private void signUpUser(String email) {
        String username = HaventecAuthenticate.getUsername();

        String jsonString = "{"
                + "\"applicationUuid\": \"" + applicationUuid + "\","
                + "\"username\": \"" + username + "\","
                + "\"email\": \"" + email + "\""
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
                .url(serverUrl + "/self-service/user")
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
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String jsonBodyStr = response.body().string();

                try {
                    JSONObject jsonData = new JSONObject(jsonBodyStr);

                    // This must not be send to your frontend application but via another path to the user (e.g: email)
                    String activationToken = jsonData.getString("activationToken");

                    try {
                        // Upon a successful response, update the Haventec details at the Storage
                        HaventecAuthenticate.updateStorage(thisActivity, jsonData);

                        // Activate the user.
                        // This method returns the activation token to activate the user, but at real app the
                        //activation token must be sent to the user via another way (e.g: email)
                        // Users have to provide their username, activationToken, and the chosen PIN
                        activateUser(activationToken, userPin);
                    } catch (HaventecAuthenticateException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void activateUser(String activationToken, String pin) {

        String username = HaventecAuthenticate.getUsername();
        String hashedPin = HaventecAuthenticate.hashPin(pin);

        String jsonString = "{"
                + "\"applicationUuid\": \"" + applicationUuid + "\","
                + "\"username\": \"" + username + "\","
                + "\"activationToken\": \"" + activationToken + "\","
                + "\"hashedPin\": \"" + hashedPin + "\","
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
                .url(serverUrl + "/authentication/activate/user")
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
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String jsonBodyStr = response.body().string();

                try {
                    JSONObject jsonData = new JSONObject(jsonBodyStr);

                    try {
                        // Upon a successful response, update the Haventec details at the Storage
                        HaventecAuthenticate.updateStorage(thisActivity, jsonData);

                        // Get user details
                        getCurrentUser();
                    } catch (HaventecAuthenticateException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addNewDevice(String username) {
        String jsonString = "{"
                + "\"applicationUuid\": \"" + applicationUuid + "\","
                + "\"username\": \"" + username + "\","
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
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String jsonBodyStr = response.body().string();

                try {
                    JSONObject jsonData = new JSONObject(jsonBodyStr);

                    // This must not be send to your frontend application but via another path to the user (e.g: email)
                    String activationToken = jsonData.getString("activationToken");

                    try {
                        HaventecAuthenticate.updateStorage(thisActivity, jsonData);

                        String pin = "123456";
                        activateDevice(activationToken, pin);
                    } catch (HaventecAuthenticateException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void activateDevice(String activationToken, String pin) {

        try {
            // These three parameters are the one your backend application needs
            //to send to Haventec Authenticate in order to activate the device.
            String hashedPin = HaventecAuthenticate.hashPin(pin);
            String username = HaventecAuthenticate.getUsername();
            String deviceUuid = HaventecAuthenticate.getDeviceUuid();

            String jsonString = "{"
                    + "\"applicationUuid\": \"" + applicationUuid + "\","
                    + "\"username\": \"" + username + "\","
                    + "\"deviceUuid\": \"" + deviceUuid + "\","
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
            String accessToken = HaventecAuthenticate.getAccessToken();

            OkHttpClient client = new OkHttpClient();

            // This demo App is calling directly Haventec Authenticate but a production App
            //should instead send the request to the backend of your application
            //Then the backend can add the x-api-key. The x-api-key is sensitive information and
            //therefore must be secured in the the backend of your App. Please do not expose your apiKey!
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

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    try {
                        JSONObject jsonData = new JSONObject(jsonBodyStr);
                        userDetails = new UserDetails(jsonData);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                titleView.setText("Hello " + userDetails.getUsername() + ",");
                                userUuidView.setText("Your userUuid is " + userDetails.getUserUuid());
                                lastLoginView.setText("Your lastLogin is " + sdf.format(new Date(userDetails.getLastLogin() * 1000)));
                                dateCreatedView.setText("Your record was created on " + sdf.format(new Date(userDetails.getDateCreated() * 1000)));

                                // Haventec Data
                                currentUsername.setText("Current Username: " + HaventecAuthenticate.getUsername());
                                deviceUuid.setText("Device UUID: " + HaventecAuthenticate.getDeviceUuid());
                                deviceAuthKey.setText("Device AuthKey: " + HaventecAuthenticate.getAuthKey());
                            }
                        });

                        // User logs out
                        userLogOut();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (HaventecAuthenticateException e) {
            e.printStackTrace();
        }
    }


  private void userLogOut() {
      try {
          String accessToken = HaventecAuthenticate.getAccessToken();

          OkHttpClient client = new OkHttpClient();

          // This demo App is calling directly Haventec Authenticate but a production App
          //should instead send the request to the backend of your application
          //Then the backend can add the x-api-key. The x-api-key is sensitive information and
          //therefore must be secured in the the backend of your App. Please do not expose your apiKey!
          Request request = new Request.Builder()
                  .addHeader("Content-type", "application/json")
                  .addHeader("x-api-key", apiKey)
                  .addHeader("Authorization", "Bearer " + accessToken)
                  .url(serverUrl + "/authentication/logout")
                  .delete()
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
                              newDeviceAuthKey.setText("New Device AuthKey: " + HaventecAuthenticate.getAuthKey());
                          }
                      });

                      userLogIn();

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
              }
          });
      } catch (HaventecAuthenticateException e) {
          e.printStackTrace();
      }
  }

  private void userLogIn() {
        try {
            // Set the name with upper case just to verify that the username is supported case insensitive
            String username = HaventecAuthenticate.getUsername().toUpperCase();

            try {
                // This is the first call that you need to do in order to initialise
                //the Storage for a specific user.
                HaventecAuthenticate.initialiseStorage(thisActivity, username);
            } catch (HaventecAuthenticateException e) {
                e.printStackTrace();
            }

            // These three parameters are the one your backend application needs
            //to send to Haventec Authenticate in order to log in.
            String hashedPin = HaventecAuthenticate.hashPin(userPin);
            String deviceUuid = HaventecAuthenticate.getDeviceUuid();

            String jsonString = "{"
                    + "\"applicationUuid\": \"" + applicationUuid + "\","
                    + "\"username\": \"" + username + "\","
                    + "\"deviceUuid\": \"" + deviceUuid + "\","
                    + "\"hashedPin\": \"" + hashedPin + "\","
                    + "\"authKey\": \"" + HaventecAuthenticate.getAuthKey() + "\""
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
                    .url(serverUrl + "/authentication/login")
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

                        // Upon a successful response, update the Haventec details at the Storage
                        HaventecAuthenticate.updateStorage(thisActivity, jsonData);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                newDeviceAuthKey.setText("New Device AuthKey: " + HaventecAuthenticate.getAuthKey());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (HaventecAuthenticateException e) {
            e.printStackTrace();
        }
    }
}
