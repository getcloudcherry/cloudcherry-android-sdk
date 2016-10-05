# CloudCherry Android SDK
Open source library that helps you integrate Cloudcherry into your Android app

## Steps to setup SDK:

- To initialize the SDK, configure it using either by generating and using a Static Token from CloudCherry Dashboard or by using Username/Password combination (Dynamic Token):

**Static Token Initialization**

```Java
SurveyCC.initialise(this, "STATIC_TOKEN");
```

*OR*

**Dynamic Token Initialization**

```Java
SurveyToken aTokenConfig = new SurveyToken(1, “mobile");
SurveyCC.initialise(this, "CloudCherry_Username", "CloudCherry_Password", aTokenConfig);
```

**Note:**

- Enabling partial response ensures that the user response is collected after each question and does not wait until the user hits submit button at the end of the survey. This is ideal for mobile app users, as the users may be interrupted by phone calls.

**Setting style for custom text icons**

User can pass `CustomTextStyle.STYLE_CIRCLE`, `CustomTextStyle.STYLE_RECTANGLE` while initialising SDK for custom text icons. User can pass `0` to use default style i.e. `CustomTextStyle.STYLE_CIRCLE`.

```Java
//Static token:
SurveyCC.initialise(this, "STATIC_TOKEN", CustomTextStyle.STYLE_CIRCLE, null, null);

//Dynamic token:
SurveyCC.initialise(this, "CloudCherry Username", "CloudCherry Password”, SurveyToken iTokenConfig, CustomTextStyle.STYLE_CIRCLE, null, null);
```

**Setting custom assets for smiley rating and star rating**

User can set custom assets for smiley rating and star rating by passing `ArrayList` of resource IDs of your selector drawables with selected and unselected state while initialising SDK. User can pass `null` to use default style i.e. emoji unicodes for smiley rating and yellow star asset for star rating

```Java
//Static token:
SurveyCC.initialise(this, "STATIC_TOKEN", CustomTextStyle.STYLE_CIRCLE, aSmileyRatingSelector, aStarRatingSelector);

//Dynamic token:
SurveyCC.initialise(this, "CloudCherry Username", "CloudCherry Password”, SurveyToken iTokenConfig, CustomTextStyle.STYLE_CIRCLE, aSmileyRatingSelector, aStarRatingSelector);
```

**Note:**

- If any of the `ArrayList` does not contain 5 images, the SDK will switch to default style

**Triggering Survey**

- Finally start the survey by using the underlying syntax (Note: Here 'self' is the controller on which you wish to present the survey):

```Java
SurveyCC.getInstance().trigger();
```

**Setting up pre-fills in SDK**

User can set up their Email ID and Mobile Number as pre-fills, which will be sent along with the survey responses

```Java
HashMap<String, Object> aAnswers = new HashMap<>();
aAnswers.put("preFillMobile", "1234567890");
aAnswers.put("preFillEmail", "abc@gmail.com");
SurveyCC.getInstance().setPreFill(aAnswers);
SurveyCC.getInstance().trigger();
```

**Demo App**

The above features have been implemented in an Android Sample app:

https://github.com/getcloudcherry/cloudcherry-android-sdk-sample-app
