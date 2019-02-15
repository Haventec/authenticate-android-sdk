# Haventec Authenticate Android SDK

A collection of functions for native Android apps to facilitate the integration with Haventec Authenticate

## Installation

The bundled .aar file can be imported into your Android project via the standard way of importing modules.

If using gradle, add the following dependencies:

```
compile 'com.haventec.authenticate.android.sdk:authenticate-android-sdk:0.1'
```

Ensure to use the latest published version of the SDK

## Usage

To use the SDK, import the following class and its dependencies:
```
import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
```

This class has the following methods:

 * **initialiseStorage:** It initilises the Android Storage for a specific Haventec Authenticate user. Your app has to call this method once you know the username of the user.

* **updateStorage:** It updates the Android Storage with the new user details. Whenever your app invokes a method that changes the authentication state (add user, add device, activate user, activate device, or login), your app must update the Haventec SDK storage using this method.

* **hashPin:** It returns a SHA-512 Hashing of the PIN passed as argument. 

* **getAccessToken:** It returns the session access token of the current authenticated user from the Android Storage

* **getAuthKey:** It returns the current authKey of the current user from the Android Storage

* **getUsername:** It returns the username of the current user from the Android Storage.

* **getDeviceUuid:** It returns the deviceUuid of the current user from the Android Storage


## Demo app
The demo app uses Haventec Authenticate directly but in a real scenario the frontend app should send requests to your backend app and after adding some private details (at least the apiKey) will communicate with Haventec Authenticate.

In order to run the Demo app you will need a free account in [Haventec Console demo](https://console-demo.haventec.com/orgsignup), if you already have one please [login here](https://console-demo.haventec.com/login). Finally create an Authenticate application in order to get your application private details "applicationUuid" and "apiKey".

1. Create a personal app.properties file based on the template: 
```
cp src/main/assets/app.properties.example src/main/assets/app.properties
```

2. Fill in the "applicationUuid" and "apiKey" of your application into "app.properties"

3. Open the folder demo/DemoAuthenticateAndroidSDK as a project in Android Studio and run the Android app

## Development
To build, run the following:
```
./gradlew clean build publish
```

## Contributors

 - Justin Crosbie

## License

This code is available under the MIT license. A copy of the license can be found in the LICENSE file included with the distribution.
 
