# Haventec Authenticate Android SDK

A collection of functions for native Android apps to facilitate client-side interaction with Haventec backend services.

## Installation

The bundled .aar file can be imported into any Android project via the standard way of importing modules.

If using gradle, add the following dependencies:

```
implementation "com.haventec.authenticate.android.sdk:authenticate-android-sdk:0.1@aar"
```

Ensure to use the latest published version of the SDK

## Usage

The main functionality provided relates to the SHA-512 Hashing of the PIN that is required by Haventec Authenticate endpoints,
so these SDK functions provide a convenient and consistent way of implementing these functions in an Android app.

To use the SDK, import the following class and its dependencies:
```
import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
```

This class has the following methods:
```
public class HaventecAuthenticate {

    /**
     * Storage and initialisation functions
     */
    public static void initialiseStorage(Context context, String username) throws HaventecAuthenticateException {
    }

    public static void updateStorage(Context context, JSONObject jsonObject) throws HaventecAuthenticateException {
    }

    /**
     * Hash a PIN code to send via Haventec endpoints
     */
    public static String hashPin(Context context, String pin) throws HaventecAuthenticateException {
    }
    
    /**
     * Access Haventec data required for Haventec endpoints
     */
    public static String getAccessToken(Context context) throws HaventecAuthenticateException {
    }

    public static String getAuthKey(Context context) throws HaventecAuthenticateException {
    }

    public static String getUsername(Context context) throws HaventecAuthenticateException {
    }

    public static String getDeviceUuid(Context context) throws HaventecAuthenticateException {
    }

    public static String getUserUuid(Context context) throws HaventecAuthenticateException {
    }
}
```

Note, all of the methods require the Android Context as input.

To initialize, call the initializeStorage method. This provisions the device persisted storage for the username.

Whenever you invoke a method that changes the authentication state of the device - add user, add device, activate user, 
activate device, login - you must update the Haventec SDK storage, using updateStorage. 
This can accept the JSONObject directly returned from any of these endpoints.
There is an alternative implementation of this function that accepts an object that implements the HaventecAuthenticateResponse interface, for more custom interfacing.

In order to authenticate with Haventec endpoints, a hashed version of the pincode is required, so the hashPin method is used for that.

The rest of the methods provide easy access to the data that is required for the Haventec endpoints.

## Demo app
To run the Demo app, configure src/main/assets/app.properties with your Haventec Application and User details.
See src/main/assets/app.properties.example for required properties
Build the project as a normal Android app. The demo/DemoAuthenticateAndroidSDK folder can be opened as a project in Android Studio

## Development
To build, run the following:
```
gradle clean build publish
```

## Contributors

 - Justin Crosbie

## License

This code is available under the MIT license. A copy of the license can be found in the LICENSE file included with the distribution.
 