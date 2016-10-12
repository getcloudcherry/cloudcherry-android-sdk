# Cloudcherry Android SDK
Open source library that helps you integrate Cloudcherry into your Android app

## Steps to setup SDK:

- To initialize the SDK, configure it using either by generating and using a Static Token from Cloudcherry dashboard or by using Username/Password combination (Dynamic Token):

**Static Token Initialization**

```Java
SurveyCC.initialise(this, "STATIC_TOKEN");
```

*OR*

**Dynamic Token Initialization**

```Java
SurveyToken aTokenConfig = new SurveyToken(1, "mobile");
SurveyCC.initialise(this, "Cloudcherry_Username", "Cloudcherry_Password", aTokenConfig);
```

## Triggering Survey

- Finally start the survey by using the underlying syntax:

```Java
SurveyCC.getInstance().trigger();
```

## Sample App

The above features have been implemented in an Android sample app:
https://github.com/getcloudcherry/cloudcherry-android-sdk-sample-app

## Manual

The detailed manual for Android SDK can be found here:
https://contentcdn.azureedge.net/assets/CloudCherryDoc%20Android%20SDK%20Manual.pdf
