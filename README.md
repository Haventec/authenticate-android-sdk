# Haventec Authenticate Android SDK

A collection of functions for native Android apps to facilitate the integration with Haventec Authenticate

## Requirements

Android 4.4 or later

## Installation

The bundled .aar file can be imported into your Android project via the standard way of importing modules.

If using gradle, add the following repository:
```
repositories {
    ...
    ..
    .
    jcenter()
}
```    

and the following dependencies:

```
dependencies {
   ...
   ..
   .
   implementation 'com.haventec.common.android.sdk:common-android-sdk:0.3'
   implementation 'com.haventec.authenticate.android.sdk:authenticate-android-sdk:1.0.5'
}
```

Ensure to use the latest published version of the SDK[ ![Download](https://api.bintray.com/packages/haventec/maven/authenticate-android-sdk/images/download.svg?version=1.0.5) ](https://bintray.com/haventec/maven/authenticate-android-sdk/1.0.5/link)

## Usage

To use the SDK, import the following class and its dependencies:
```
import com.haventec.authenticate.android.sdk.api.HaventecAuthenticate;
```

This class has the following methods:

 * **initialiseStorage:** It initialises the Android Storage for a specific Haventec Authenticate user. Your app has to call this method once you know the username of the user.

* **updateStorage:** It updates the Android Storage with the new user details. Whenever your app invokes a method that changes the authentication state (add user, add device, activate user, activate device, or login), your app must update the Haventec SDK storage using this method.

* **hashPin:** It returns a SHA-512 Hashing of the PIN passed as argument. 

* **getAccessToken:** It returns the session access token of the current authenticated user from the Android Storage

* **getAuthKey:** It returns the current authKey of the current user from the Android Storage

* **getUsername:** It returns the username of the current user from the Android Storage.

* **getDeviceUuid:** It returns the deviceUuid of the current user from the Android Storage

* **regenerateSalt:** Allows the salt to be regenerated post-initialisation. This should be done whenever a new hashed PIN is created, e.g.on PIN reset or add new device

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

## License

This code is available under the MIT license. A copy of the license can be found in the LICENSE file included with the distribution.
 
