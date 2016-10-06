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

User can configure custom text style by passing CustomTextStyle.STYLE_CIRCLE or CustomTextStyle.STYLE_RECTANGLE to setCustomTextStyle() method. Skip setting the property to use default style.

```Java
SurveyCC.getInstance().setCustomTextStyle(CustomTextStyle.STYLE_CIRCLE);
```

**Note:**

- Set the style property before triggering SDK.

**Setting custom assets for smiley rating and star rating**

User can set custom assets for smiley rating and star rating by passing `ArrayList` of resource IDs of your selector drawables with selected and unselected state to methods `setSmileyRatingSelector()` and `setStarRatingSelector()` for smiley rating and star rating respectively. Skip setting the respective property to use default style.

```Java
//Smiley Rating:
SurveyCC.getInstance().setSmileyRatingSelector(aSmileyRatingSelector);

//Star Rating:
SurveyCC.getInstance().setStarRatingSelector(aStarRatingSelector);
```

**Note:**

- If the size of ArrayList containing resource IDs is not exactly 5 then default style will be used.
- Set the style property before triggering SDK.

**Setting up pre-fills in SDK**

User can set up their Email ID and Mobile Number as pre-fills, which will be sent along with the survey responses

```Java
HashMap<String, Object> aAnswers = new HashMap<>();
aAnswers.put("preFillMobile", "1234567890");
aAnswers.put("preFillEmail", "abc@gmail.com");
SurveyCC.getInstance().setPreFill(aAnswers);
```

**Triggering Survey**

- Finally start the survey by using the underlying syntax (Note: Here 'self' is the controller on which you wish to present the survey):

```Java
SurveyCC.getInstance().trigger();
```

**Demo App**

The above features have been implemented in an Android Sample app:

https://github.com/getcloudcherry/cloudcherry-android-sdk-sample-app
