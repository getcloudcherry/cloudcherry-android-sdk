package com.getcloudcherry.survey.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.builder.SurveyConfigBuilder;
import com.getcloudcherry.survey.filter.QuestionFilterHelper;
import com.getcloudcherry.survey.httpclient.SurveyClient;
import com.getcloudcherry.survey.interfaces.AnalyticsCallBack;
import com.getcloudcherry.survey.interfaces.ConditionalChangesCallBack;
import com.getcloudcherry.survey.interfaces.FragmentCallBack;
import com.getcloudcherry.survey.interfaces.QuestionCallback;
import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.CustomTextStyle;
import com.getcloudcherry.survey.model.Data;
import com.getcloudcherry.survey.model.LoginToken;
import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;
import com.getcloudcherry.survey.model.SurveyToken;
import com.getcloudcherry.survey.model.ThrottleEntryRequest;
import com.getcloudcherry.survey.model.ThrottleResponse;
import com.getcloudcherry.survey.model.ThrottlingLogicResponse;
import com.getcloudcherry.survey.storage.CCPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by riteshdubey on 7/30/16.
 */

/**
 * Helper class to configure and access SDK related functions
 */
public class SurveyCC {
    public static SurveyCC mInstance;
    public static Context mContext, mActivityContext;
    private static String mSurveyToken;
    private static boolean mShouldCreate;
    private static ArrayList<Integer> mSmileyRatingSelector;
    private static ArrayList<Integer> mStarRatingSelector;
    private static ProgressDialog mProgressDialog;

    private static String mUserName;
    private static String mPassword;
    private static String mPartialResponseId;
    private boolean mIsPartialCapture;
    public String WELCOME_MESSAGE = "";
    public String THANKS_MESSAGE = "";
    public boolean SHOW_WELCOME_MESSAGE = false;
    public boolean SHOW_THANKS_MESSAGE = false;
    private boolean mIsUserTryingToExit = false;
    private int mRecordedAnswerCount;
    private boolean mToThrottle;
    private ThrottlingLogicResponse.ThrottlingLogic mThrottleLogic;
    private HashMap<String, String> mThrottleUniqueId;

    private static final int RETRY_LOGIN = 0;
    private static final int RETRY_GET_SURVEY_THROTTLING_LOGIC = 1;
    private static final int RETRY_CHECK_THROTTLING = 2;

    // Header Config variables
    public String HEADER_BACKGROUND_COLOR = "#FFFFFF";
    public String HEADER_FONT_PATH = "";
    public int HEADER_FONT_SIZE = 16;
    public String HEADER_FONT_COLOR = "#000000";
    public int ACTION_BAR_BACKGROUND_COLOR = R.color.color_lightgrey;
    public int ACTION_BAR_FONT_COLOR = android.R.color.white;
    public int ACTION_BAR_FONT_SIZE = 16;
    public String ACTION_BAR_FONT_PATH = "";
    public String HEADER_LOGO = "";

    // Content Config variables
    public String CONTENT_BACKGROUND_COLOR = "#FFFFFF";
    public String CONTENT_FONT_PATH = "";
    public int CONTENT_FONT_SIZE = 16;
    public int CONTENT_FONT_COLOR = android.R.color.black;

    // Footer Config variables
    public String FOOTER_BACKGROUND_COLOR = "#FFFFFF";
    public String FOOTER_BUTTON_FONT_PATH = "";
    public int FOOTER_BUTTON_FONT_SIZE = 13;
    public int FOOTER_BUTTON_FONT_COLOR = android.R.color.black;
    public int FOOTER_BUTTON_COLOR = R.color.color_lightgrey;
    public String FOOTER_PAGE_FONT_PATH = "";
    public int FOOTER_PAGE_FONT_SIZE = 12;
    public int FOOTER_PAGE_FONT_COLOR = android.R.color.black;
    private SurveyResponse mSurveyResponse;
    private ArrayList<SurveyQuestions> mSurveyQuestions = new ArrayList<>();
    private ArrayList<SurveyQuestions> mConditionalQuestions = new ArrayList<>();

    //Question Type
    private int CUSTOM_TEXT_STYLE = CustomTextStyle.STYLE_CIRCLE;

    //Listeners
    private ArrayList<FragmentCallBack> mFragmentDataCallback = new ArrayList<>();
    private ArrayList<QuestionCallback> mQuestionCallbacks = new ArrayList<>();
    private ArrayList<AnalyticsCallBack> mAnalyticsCallbacks = new ArrayList<>();
    private ArrayList<ConditionalChangesCallBack> mConditionalFlowCallbacks = new ArrayList<>();
//    private ArrayList<ExitCallBack> mExitCallBacks = new ArrayList<>();

    //Token Config
    private static SurveyToken mTokenConfig;

    /**
     * Initializes the SDK with application context
     *
     * @param iContext     application context
     * @param iSurveyToken Survey Token provided by cloud cherry
     */
    public static void initialise(Context iContext, String iSurveyToken) {
        mContext = iContext;
        mSurveyToken = iSurveyToken;
        mShouldCreate = TextUtils.isEmpty(iSurveyToken);
        getInstance();
        initialiseProgressDialog();
    }

    /**
     * Initializes the SDK with application context
     *
     * @param iContext     application context
     * @param iUsername    Username for SDK
     * @param iPassword    Password for SDK
     * @param iSurveyToken Survey Token provided by cloud cherry
     */
    public static void initialise(Context iContext, String iUsername, String iPassword, String iSurveyToken) {
        setCredentials(iUsername, iPassword);
        mContext = iContext;
        mSurveyToken = iSurveyToken;
        mShouldCreate = TextUtils.isEmpty(iSurveyToken);
        getInstance();
        initialiseProgressDialog();
    }

    /**
     * Initializes the SDK with application context
     *
     * @param iContext     application context
     * @param iUsername    Username for SDK
     * @param iPassword    Password for SDK
     * @param iTokenConfig SurveyToken configuration
     */
    public static void initialise(Context iContext, String iUsername, String iPassword, SurveyToken iTokenConfig) {
        mTokenConfig = iTokenConfig;
        initialise(iContext, iUsername, iPassword, "");
    }

    private static void setCredentials(String iUserName, String iPassword) {
        mUserName = iUserName;
        mPassword = iPassword;
    }

    public void setCustomTextStyle(int iCustomTextStyle) {
        CUSTOM_TEXT_STYLE = (iCustomTextStyle != CustomTextStyle.STYLE_CIRCLE && iCustomTextStyle != CustomTextStyle.STYLE_RECTANGLE) ? CustomTextStyle.STYLE_CIRCLE : iCustomTextStyle;
    }

    public void setSmileyRatingSelector(ArrayList<Integer> iSmileyRatingSelector) {
        if (iSmileyRatingSelector != null && iSmileyRatingSelector.size() == 5) {
            mSmileyRatingSelector = iSmileyRatingSelector;
        }
    }

    public void setStarRatingSelector(ArrayList<Integer> iStarRatingSelector) {
        if (iStarRatingSelector != null && iStarRatingSelector.size() == 5) {
            mStarRatingSelector = iStarRatingSelector;
        }
    }

    /**
     * Method to change SDK view related config developer
     *
     * @param iConfigBuilder SurveyConfigBuilder builder instance
     */
    public void setConfigFromApp(SurveyConfigBuilder iConfigBuilder) {

        if (iConfigBuilder != null) {
            if (!TextUtils.isEmpty(iConfigBuilder.HEADER_FONT_PATH))
                HEADER_FONT_PATH = iConfigBuilder.HEADER_FONT_PATH;
            HEADER_FONT_SIZE = iConfigBuilder.HEADER_FONT_SIZE;
            if (!TextUtils.isEmpty(iConfigBuilder.ACTION_BAR_FONT_PATH))
                ACTION_BAR_FONT_PATH = iConfigBuilder.ACTION_BAR_FONT_PATH;
            ACTION_BAR_FONT_SIZE = iConfigBuilder.ACTION_BAR_FONT_SIZE;
            ACTION_BAR_FONT_COLOR = iConfigBuilder.ACTION_BAR_FONT_COLOR;
            ACTION_BAR_BACKGROUND_COLOR = iConfigBuilder.ACTION_BAR_BACKGROUND_COLOR;
            CONTENT_BACKGROUND_COLOR = iConfigBuilder.CONTENT_BACKGROUND_COLOR;
            if (!TextUtils.isEmpty(iConfigBuilder.CONTENT_FONT_PATH))
                CONTENT_FONT_PATH = iConfigBuilder.CONTENT_FONT_PATH;
            CONTENT_FONT_SIZE = iConfigBuilder.CONTENT_FONT_SIZE;
            CONTENT_FONT_COLOR = iConfigBuilder.CONTENT_FONT_COLOR;
            if (!TextUtils.isEmpty(iConfigBuilder.FOOTER_BUTTON_FONT_PATH))
                FOOTER_BUTTON_FONT_PATH = iConfigBuilder.FOOTER_BUTTON_FONT_PATH;
            FOOTER_BUTTON_FONT_SIZE = iConfigBuilder.FOOTER_BUTTON_FONT_SIZE;
            FOOTER_BUTTON_FONT_COLOR = iConfigBuilder.FOOTER_BUTTON_FONT_COLOR;
            FOOTER_BUTTON_COLOR = iConfigBuilder.FOOTER_BUTTON_COLOR;
            if (!TextUtils.isEmpty(iConfigBuilder.FOOTER_PAGE_FONT_PATH))
                FOOTER_PAGE_FONT_PATH = iConfigBuilder.FOOTER_PAGE_FONT_PATH;
            FOOTER_PAGE_FONT_COLOR = iConfigBuilder.FOOTER_PAGE_FONT_COLOR;
            FOOTER_PAGE_FONT_SIZE = iConfigBuilder.FOOTER_PAGE_FONT_SIZE;
        }
    }

    /**
     * Method to change SDK view related config from web
     *
     * @param iConfigBuilder SurveyConfigBuilder builder instance
     */
    public void setConfigFromWeb(SurveyConfigBuilder iConfigBuilder) {

        if (iConfigBuilder != null) {
            if (!TextUtils.isEmpty(iConfigBuilder.WELCOME_MESSAGE))
                WELCOME_MESSAGE = iConfigBuilder.WELCOME_MESSAGE;
            if (!TextUtils.isEmpty(iConfigBuilder.THANKS_MESSAGE))
                THANKS_MESSAGE = iConfigBuilder.THANKS_MESSAGE;
            SHOW_THANKS_MESSAGE = iConfigBuilder.SHOW_THANKS_MESSAGE;
            SHOW_WELCOME_MESSAGE = iConfigBuilder.SHOW_WELCOME_MESSAGE;
            HEADER_BACKGROUND_COLOR = iConfigBuilder.HEADER_BACKGROUND_COLOR;
            if (!TextUtils.isEmpty(iConfigBuilder.HEADER_FONT_COLOR))
                HEADER_FONT_COLOR = iConfigBuilder.HEADER_FONT_COLOR;
            if (!TextUtils.isEmpty(iConfigBuilder.HEADER_LOGO))
                HEADER_LOGO = iConfigBuilder.HEADER_LOGO;
            if (!TextUtils.isEmpty(iConfigBuilder.FOOTER_BACKGROUND_COLOR))
                FOOTER_BACKGROUND_COLOR = iConfigBuilder.FOOTER_BACKGROUND_COLOR;
        }
    }

    /**
     * Gets the single instance of the SurveyCC class in order to use SDK related methods
     *
     * @return instance of SurveyCC class
     */
    public static SurveyCC getInstance() {
        if (mInstance == null) {
            mInstance = new SurveyCC();
        }
        return mInstance;
    }

    /**
     * Initialises the progress loading dialog to show while API call is being done
     */
    private static void initialiseProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * Gets application context of the application the SDK in integrated to.
     *
     * @return Context - application context
     */
    public Context getContext() {
        checkSDKInitialized();
        return mContext;
    }

    /**
     * Start survey activity without throttling
     *
     * @param iContext Activity context
     */
    public void trigger(Context iContext) throws Exception {
        trigger(iContext, false);
    }

    /**
     * Start survey activity
     *
     * @param iContext    Activity context
     * @param iToThrottle boolean that specifies if survey has to be throttled or not
     */
    public void trigger(Context iContext, boolean iToThrottle) throws Exception {
        mActivityContext = iContext;
        mToThrottle = iToThrottle;
        if (mToThrottle || shouldCreateToken()) {
            if (TextUtils.isEmpty(getUserName()) || TextUtils.isEmpty(getPassword())) {
                throw new Exception("Please provide username and password.");
            } else
                login(new CheckThrottleCallBack() {
                    @Override
                    public void onResponse() {
                        processTrigger();
                    }
                });
        } else
            processTrigger();
    }

    /**
     * Start survey activity with some request code awaiting some result from the SDK without throttling
     *
     * @param iContext     Activity context
     * @param iRequestCode Request code to start an activity
     */
    public void triggerForResult(Context iContext, int iRequestCode) throws Exception {
        triggerForResult(iContext, iRequestCode, false);
    }

    /**
     * Start survey activity with some request code awaiting some result from the SDK
     *
     * @param iContext     Activity context
     * @param iRequestCode Request code to start an activity
     * @param iToThrottle  boolean that specifies if survey has to be throttled or not
     */
    public void triggerForResult(final Context iContext, final int iRequestCode, boolean iToThrottle) throws Exception {
        mActivityContext = iContext;
        mToThrottle = iToThrottle;
        if (mToThrottle || shouldCreateToken()) {
            if (TextUtils.isEmpty(getUserName()) || TextUtils.isEmpty(getPassword())) {
                throw new Exception("Please provide username and password.");
            } else
                login(new CheckThrottleCallBack() {
                    @Override
                    public void onResponse() {
                        processTriggerForResult(iContext, iRequestCode);
                    }
                });
        } else
            processTriggerForResult(iContext, iRequestCode);
    }

    /**
     * Start survey activity after throttling check
     */
    private void processTrigger() {
        resetExitCallBackParams();
        checkSDKInitialized();
        Intent aIntent = new Intent(mContext, SurveyActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(aIntent);
    }

    /**
     * Start survey activity with some request code awaiting some result from the SDK after throttling check
     *
     * @param iContext     Activity context
     * @param iRequestCode Request code to start an activity
     */
    private void processTriggerForResult(Context iContext, int iRequestCode) {
        resetExitCallBackParams();
        checkSDKInitialized();
        Intent aIntent = new Intent(mContext, SurveyActivity.class);
        ((AppCompatActivity) iContext).startActivityForResult(aIntent, iRequestCode);
    }

    /**
     * Method to check if the SDK has been properly initialized in order to function properly
     */
    private void checkSDKInitialized() {
        if (mContext == null) {
            throw new RuntimeException("Context not initialized");
        }
    }

    /**
     * Gets the survey token
     *
     * @return survey token
     */
    public String getSurveyToken() {
        return mSurveyToken;
    }

    /**
     * Sets survey token
     *
     * @param iSurveyToken survey token
     */
    public void setSurveyToken(String iSurveyToken) {
        mSurveyToken = iSurveyToken;
    }

    /**
     * Sets pre-fill questions
     *
     * @param iPreFillAnswers HashMap of pre-fill question data
     *                        <br>key = question tag
     *                        <br>value = answer object
     */
    public void setPreFill(HashMap<String, Object> iPreFillAnswers) {
        RecordAnswer.getInstance().preFillQuestionWithTags(iPreFillAnswers);
    }

    /**
     * Sets pre-fill questions
     *
     * @param iPreFillAnswers HashMap of pre-fill question data
     *                        <br>key = question tag
     *                        <br>value = answer object
     */
    public void setPreFillFromAPI(ArrayList<Answer> iPreFillAnswers) {
        if (iPreFillAnswers != null)
            for (Answer aAnswer : iPreFillAnswers) {
                RecordAnswer.getInstance().recordAnswer(aAnswer.questionId, aAnswer);
            }
    }

    /**
     * Sets unique Id for input in throttling logic
     *
     * @param iThrottleUniqueId HashMap of unique ID
     *                          <br>key = "email", "mobile" and any other exact string of uniqueIDQuestionIdOrTag
     *                          <br>value = email address, mobile number and uniqueIDQuestionIdOrTag that has to be sent as input
     */
    public void setThrottleUniqueId(HashMap<String, String> iThrottleUniqueId) {
        mThrottleUniqueId = iThrottleUniqueId;
    }

    /**
     * Get hash map of unique Id for input in throttling logic
     */
    public HashMap<String, String> getThrottleUniqueId() {
        return mThrottleUniqueId;
    }

    /**
     * Call to check whether to create a new Survey Token or use static Survey Token
     *
     * @return boolean
     */
    public boolean shouldCreateToken() {
        return mShouldCreate;
    }

    /**
     * Gets the username
     *
     * @return username
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Gets the password
     *
     * @return password
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Gets the partial response id
     *
     * @return partialResponseId
     */
    public String getPartialResponseId() {
        return mPartialResponseId;
    }

    /**
     * Sets partial response id
     *
     * @param iPartialResponseId partial response id
     */
    public void setPartialResponseId(String iPartialResponseId) {
        if (!TextUtils.isEmpty(iPartialResponseId)) {
            mPartialResponseId = iPartialResponseId;
            mIsPartialCapture = true;
        } else {
            mIsPartialCapture = false;
        }
    }

    /**
     * Gets Survey token configuration set earlier
     *
     * @return SurveyToken object
     */
    public SurveyToken getTokenConfig() {
        return mTokenConfig;
    }

    /**
     * Checks if response can be partially captured
     *
     * @return boolean
     */
    public boolean isPartialCapturing() {
        return mIsPartialCapture;
    }

    /**
     * Set if user is trying to exit in between
     *
     * @param isUserTryingToExit
     */
    public void setIsUserTryingToExit(boolean isUserTryingToExit) {
        mIsUserTryingToExit = isUserTryingToExit;
    }

    /**
     * Checks if user is trying to exit in between
     *
     * @return boolean
     */
    public boolean isUserTryingToExit() {
        return mIsUserTryingToExit;
    }

    /**
     * Set the number of recorded answers
     *
     * @param iRecordedAnswerCount
     */
    public void setRecordedAnswerCount(int iRecordedAnswerCount) {
        mRecordedAnswerCount = iRecordedAnswerCount;
    }

    /**
     * Checks if to throttle or not
     *
     * @return boolean
     */
    public boolean toThrottle() {
        return mToThrottle;
    }

    /**
     * Get the number of recorded answers
     *
     * @return int
     */
    public int getRecordedAnswerCount() {
        return mRecordedAnswerCount;
    }

    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    public String getThanksMessage() {
        return THANKS_MESSAGE;
    }

    public boolean isShowWelcomeMessage() {
        return SHOW_WELCOME_MESSAGE;
    }

    public boolean isShowThanksMessage() {
        return SHOW_THANKS_MESSAGE;
    }

    public String getHeaderBackgroundColor() {
        return HEADER_BACKGROUND_COLOR;
    }

    public String getHeaderFontPath() {
        return HEADER_FONT_PATH;
    }

    public int gethHeaderFontSize() {
        return HEADER_FONT_SIZE;
    }

    public String getHeaderFontColor() {
        return HEADER_FONT_COLOR;
    }

    public int getHeaderActionBarBackgroundColor() {
        return ACTION_BAR_BACKGROUND_COLOR;
    }

    public int getHeaderActionBarFontColor() {
        return ACTION_BAR_FONT_COLOR;
    }

    public int getHeaderActionBarFontSize() {
        return ACTION_BAR_FONT_SIZE;
    }

    public String getHeaderActionBarFontPath() {
        return ACTION_BAR_FONT_PATH;
    }

    public String getHeaderActionBarLogo() {
        return HEADER_LOGO;
    }

    public String getContentBackgroundColor() {
        return CONTENT_BACKGROUND_COLOR;
    }

    public String getContentFontPath() {
        return CONTENT_FONT_PATH;
    }

    public int getContentFontSize() {
        return CONTENT_FONT_SIZE;
    }

    public int getContentFontColor() {
        return CONTENT_FONT_COLOR;
    }

    public String getFooterBackgroundColor() {
        return FOOTER_BACKGROUND_COLOR;
    }

    public String getFooterFontPath() {
        return FOOTER_BUTTON_FONT_PATH;
    }

    public int getFooterFontSize() {
        return FOOTER_BUTTON_FONT_SIZE;
    }

    public int getFooterFontColor() {
        return FOOTER_BUTTON_FONT_COLOR;
    }

    public int getFooterButtonColor() {
        return FOOTER_BUTTON_COLOR;
    }

    public String getFooterPageFontPath() {
        return FOOTER_PAGE_FONT_PATH;
    }

    public int getFooterPageFontSize() {
        return FOOTER_PAGE_FONT_SIZE;
    }

    public int getFooterPageFontColor() {
        return FOOTER_PAGE_FONT_COLOR;
    }

    public int getCustomTextStyle() {
        return CUSTOM_TEXT_STYLE;
    }

    public ArrayList<Integer> getSmileyRatingSelector() {
        return mSmileyRatingSelector;
    }

    public ArrayList<Integer> getStarRatingSelector() {
        return mStarRatingSelector;
    }

    public void resetExitCallBackParams() {
        setIsUserTryingToExit(false);
        setRecordedAnswerCount(0);
    }

    //***************************Fragment Data Callback*****************************//

    /**
     * Sets listener to receive updates on current question being displayed
     *
     * @param iCallBack
     */
    public void setOnFragmentDataListener(FragmentCallBack iCallBack) {
        if (iCallBack != null)
            mFragmentDataCallback.add(iCallBack);
    }

    /**
     * Removes listener
     *
     * @param iCallBack
     */
    public void removeFragmentDataListener(FragmentCallBack iCallBack) {
        if (iCallBack != null)
            mFragmentDataCallback.remove(iCallBack);
    }

    /**
     * Gets array list of all the listeners registered for current question being displayed
     *
     * @return
     */
    private ArrayList<FragmentCallBack> getFragmentDataListeners() {
        return mFragmentDataCallback;
    }

    /**
     * Sends data to the registered listeners for current question being displayed
     *
     * @param iQuestion  question object
     * @param iPosition  position of the viewpager item being displayed
     * @param isLastPage boolean whether the item displayed is the last page or not
     */
    public void sendFragmentData(SurveyQuestions iQuestion, int iPosition, boolean isLastPage) {
        if (mFragmentDataCallback != null) {
            for (FragmentCallBack aCallBack : mFragmentDataCallback) {
                aCallBack.onQuestionDisplayed(iQuestion, iPosition, isLastPage);
            }
        }
    }

    //***************************Fragment Data Callback*****************************//

    //******************************Question CallBack**********************************//

    /**
     * Sets listener to receive updates on current question being displayed
     *
     * @param iCallBack
     */
    public void setQuestionListener(QuestionCallback iCallBack) {
        if (iCallBack != null)
            mQuestionCallbacks.add(iCallBack);
    }

    /**
     * Gets array list of all the listeners registered for current question being displayed
     *
     * @return
     */
    private ArrayList<QuestionCallback> getQuestionCallback() {
        return mQuestionCallbacks;
    }

    /**
     * Sends arraylist of survey questions
     */
    public void sendQuestions() {
        if (mQuestionCallbacks != null) {
            for (QuestionCallback aCallBack : mQuestionCallbacks) {
                aCallBack.onGetSurveyQuestions(getSurveyQuestions());
                aCallBack.onGetSurveyResponse(getSurveyResponse());
            }
        }
    }

    //******************************Question CallBack**********************************//

    //******************************Analytics CallBack**********************************//

    /**
     * Sets analytics listener to receive updates
     *
     * @param iCallBack
     */
    public void setAnalyticsListener(AnalyticsCallBack iCallBack) {
        if (iCallBack != null)
            mAnalyticsCallbacks.add(iCallBack);
    }

    /**
     * Removes analytics listener
     *
     * @param iCallBack
     */
    public void removeAnalyticsListener(AnalyticsCallBack iCallBack) {
        if (iCallBack != null)
            mAnalyticsCallbacks.remove(iCallBack);
    }

    /**
     * Gets array list of all the listeners registered for current question being displayed
     *
     * @return
     */
    private ArrayList<AnalyticsCallBack> getAnalyticsCallback() {
        return mAnalyticsCallbacks;
    }

    /**
     * Sends array list analytics data
     */
    public void sendAnalyticsDataDump() {
        if (mAnalyticsCallbacks != null) {
            for (AnalyticsCallBack aCallBack : mAnalyticsCallbacks) {
                aCallBack.onUpdatedAnalyticsData(RecordAnalytics.getInstance().getAnalyticsDataDump());
            }
        }
    }

    /**
     * Sends arraylist of survey questions
     */
    public void sendAnalyticsData(SurveyQuestions iQuestion) {
        if (mAnalyticsCallbacks != null) {
            for (AnalyticsCallBack aCallBack : mAnalyticsCallbacks) {
                Data aData = RecordAnalytics.getInstance().getAnalyticsDataForQuestionId(iQuestion.id);
                if (aData != null)
                    aCallBack.onSurveyQuestionSeen(aData);
            }
        }
    }

    /**
     * Sends state of exit on exiting survey
     */
    public void sendExitState(AnalyticsCallBack.SurveyExitedAt iSurveyExitedAt) {
        if (mAnalyticsCallbacks != null) {
            for (AnalyticsCallBack aCallBack : mAnalyticsCallbacks) {
                aCallBack.onSurveyExited(iSurveyExitedAt, getSurveyToken());
            }
        }
    }

    //******************************Analytics CallBack**********************************//

    //******************************Conditional Question CallBack**********************************//

    /**
     * Sets listener to receive updates on conditional question filter
     *
     * @param iCallBack
     */
    public void setConditionalFlowListener(ConditionalChangesCallBack iCallBack) {
        if (iCallBack != null)
            mConditionalFlowCallbacks.add(iCallBack);
    }

    /**
     * Removes conditional flow listener
     *
     * @param iCallBack
     */
    public void removeConditionalFlowListener(ConditionalChangesCallBack iCallBack) {
        if (iCallBack != null)
            mConditionalFlowCallbacks.remove(iCallBack);
    }

    /**
     * Gets array list of all the listeners registered for conditional flow changes
     *
     * @return
     */
    private ArrayList<ConditionalChangesCallBack> getConditionalFLowListener() {
        return mConditionalFlowCallbacks;
    }

    /**
     * Sends arraylist of survey questions
     */
    public void sendConditionalFLowQuestionsData(int iTotalPageCount) {
        if (mConditionalFlowCallbacks != null) {
            for (ConditionalChangesCallBack aCallBack : mConditionalFlowCallbacks) {
                aCallBack.onConditionalQuestionsAdded();
                aCallBack.onPageCountChange(iTotalPageCount);
            }
        }
    }

    //******************************Question CallBack**********************************//

    /**
     * Sets SurveyResponse object
     *
     * @param iResponse
     */
    public void setSurveyResponse(SurveyResponse iResponse) {
        if (iResponse != null) {
            mSurveyResponse = iResponse;
            mSurveyQuestions.clear();
            mSurveyQuestions.addAll(QuestionFilterHelper.getFilteredQuestions(mSurveyResponse));
            setPartialResponseId(iResponse.partialResponseId);
            sendQuestions();
        }
    }

    /**
     * Gets SurveyResponse object
     *
     * @return SurveyResponse object
     */
    public SurveyResponse getSurveyResponse() {
        return mSurveyResponse;
    }

    /**
     * Gets array list of survey questions
     *
     * @return array list of survey questions
     */
    public ArrayList<SurveyQuestions> getSurveyQuestions() {
        return mSurveyQuestions;
    }

    /**
     * Gets array list of conditional survey questions
     *
     * @return array list of survey questions
     */
    public ArrayList<SurveyQuestions> getConditionalSurveyQuestions() {
        return mConditionalQuestions;
    }

    /**
     * Adds conditional survey question to the list of conditional survey question
     *
     * @return survey questions object
     */
    public void addConditionalSurveyQuestions(SurveyQuestions iQuestion) {
        mConditionalQuestions.add(iQuestion);
    }

    /**
     * API call to authenticate user and creates new survey token to fetch question list
     *
     * @param iCheckThrottleCallBack
     */
    private void login(final CheckThrottleCallBack iCheckThrottleCallBack) {
        showProgressBar();
        Call<LoginToken> aCall = SurveyClient.get().login(Constants.GRANT_TYPE, SurveyCC.getInstance().getUserName(), SurveyCC.getInstance().getPassword());
        aCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        CCPreferences.getInstance(SurveyCC.getInstance().getContext()).setUserDetail(response.body());
                        if (toThrottle())
                            getSurveyThrottlingLogic(iCheckThrottleCallBack);
                        else {
                            hideProgressBar();
                            iCheckThrottleCallBack.onResponse();
                        }
                    } else {
                        hideProgressBar();
                        showAlertRetryCallback(RETRY_LOGIN, mContext.getString(R.string.toast_failed_general), mActivityContext, iCheckThrottleCallBack);
                        Constants.logWarn("login onFailure", "problem in response");
                    }
                } catch (Exception e) {
                    Constants.logWarn("login", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                try {
                    hideProgressBar();
                    showAlertRetryCallback(RETRY_LOGIN, mContext.getString(R.string.toast_failed_general), mActivityContext, iCheckThrottleCallBack);
                    Constants.logWarn("login onFailure", t.getMessage());
                } catch (Exception e) {
                    Constants.logWarn("login onFailure", e.getMessage());
                }
            }
        });

    }

    /**
     * API call to get throttling logic
     *
     * @param iCheckThrottleCallBack
     */
    private void getSurveyThrottlingLogic(final CheckThrottleCallBack iCheckThrottleCallBack) {
        showProgressBar();
        Call<List<ThrottlingLogicResponse>> aCall = SurveyClient.getApiDispatcher().getSurveyThrottlingLogic(SurveyCC.getInstance().getTokenConfig() != null ? SurveyCC.getInstance().getTokenConfig().location : "mobile");
        aCall.enqueue(new Callback<List<ThrottlingLogicResponse>>() {
            @Override
            public void onResponse(Call<List<ThrottlingLogicResponse>> call, Response<List<ThrottlingLogicResponse>> response) {
                try {
                    if (response != null && response.body() != null && response.body().size() > 0) {
                        mThrottleLogic = response.body().get(0).logic;
                        checkThrottling(iCheckThrottleCallBack);
                    } else {
                        hideProgressBar();
                        showAlertRetryCallback(RETRY_LOGIN, mContext.getString(R.string.toast_failed_general), mActivityContext, iCheckThrottleCallBack);
                        Constants.logWarn("login onFailure", "problem in response");
                    }
                } catch (Exception e) {
                    Constants.logWarn("getSurveyThrottlingLogic onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<ThrottlingLogicResponse>> call, Throwable t) {
                try {
                    hideProgressBar();
                    showAlertRetryCallback(RETRY_GET_SURVEY_THROTTLING_LOGIC, mContext.getString(R.string.toast_failed_general), mActivityContext, iCheckThrottleCallBack);
                    Constants.logWarn("getSurveyThrottlingLogic onFailure", t.getMessage());
                } catch (Exception e) {
                    Constants.logWarn("getSurveyThrottlingLogic onFailure", e.getMessage());
                }
            }
        });
    }

    private void checkThrottling(final CheckThrottleCallBack iCheckThrottleCallBack) {
        if (mThrottleLogic != null) {
            if (mThrottleLogic.inputIds == null) {
                mThrottleLogic.inputIds = new ArrayList<>();
            }
            mThrottleLogic.inputIds.add(mThrottleUniqueId.get(mThrottleLogic.uniqueIDQuestionIdOrTag.toLowerCase()));
            if (mThrottleLogic.logics != null && mThrottleLogic.logics.size() > 0 && mTokenConfig != null) {
                mThrottleLogic.logics.get(0).filter.location = new ArrayList<>();
                mThrottleLogic.logics.get(0).filter.location.add(mTokenConfig.location);
            }
        }
        showProgressBar();
        Call<List<ThrottleResponse>> aCall = SurveyClient.getApiDispatcher().checkThrottling(mThrottleLogic);
        aCall.enqueue(new Callback<List<ThrottleResponse>>() {
            @Override
            public void onResponse(Call<List<ThrottleResponse>> call, Response<List<ThrottleResponse>> response) {
                try {
                    if (response != null && response.body() != null && response.body().size() > 0) {
                        hideProgressBar();
                        if (response.body().get(0).key.equals(mThrottleUniqueId.get(mThrottleLogic.uniqueIDQuestionIdOrTag.toLowerCase())) && response.body().get(0).value)
                            iCheckThrottleCallBack.onResponse();
                    } else {
                        hideProgressBar();
                        showAlertRetryCallback(RETRY_CHECK_THROTTLING, mContext.getString(R.string.toast_failed_general), mActivityContext, iCheckThrottleCallBack);
                    }
                } catch (Exception e) {
                    Constants.logWarn("checkThrottling onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<List<ThrottleResponse>> call, Throwable t) {
                try {
                    hideProgressBar();
                    showAlertRetryCallback(RETRY_CHECK_THROTTLING, mContext.getString(R.string.toast_failed_general), mActivityContext, iCheckThrottleCallBack);
                    Constants.logWarn("checkThrottling onFailure", t.getMessage());
                } catch (Exception e) {
                    Constants.logWarn("checkThrottling onFailure", e.getMessage());
                }
            }
        });
    }

    /**
     * Shows alert dialog to retry if any of the APIs fail to respond
     *
     * @param iWhich   which API to call integer constant
     * @param iMessage message to be shown
     * @param iContext Activity context
     */
    private void showAlertRetryCallback(final int iWhich, String iMessage, Context iContext, final CheckThrottleCallBack iCheckThrottleCallBack) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        dialog.setTitle(iContext.getString(R.string.alert_title_alert));
        dialog.setMessage(iMessage);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(R.string.alert_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (iWhich) {
                    case RETRY_LOGIN:
                        login(iCheckThrottleCallBack);
                        break;
                    case RETRY_GET_SURVEY_THROTTLING_LOGIC:
                        getSurveyThrottlingLogic(iCheckThrottleCallBack);
                        break;
                    case RETRY_CHECK_THROTTLING:
                        checkThrottling(iCheckThrottleCallBack);
                }
            }
        });
        dialog.show();
    }

    public void addThrottleEntry(ThrottleEntryRequest iThrottleEntry) {
        List<ThrottleEntryRequest> aThrottleEntries = new ArrayList<>();
        aThrottleEntries.add(iThrottleEntry);
        Call<ResponseBody> aCall = SurveyClient.getApiDispatcher().addThrottlingEntries(aThrottleEntries);
        aCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                } catch (Exception e) {
                    Constants.logWarn("addThrottleEntry onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    Constants.logWarn("addThrottleEntry onFailure", t.getMessage());
                } catch (Exception e) {
                    Constants.logWarn("addThrottleEntry onFailure", e.getMessage());
                }
            }
        });
    }

    private void showProgressBar() {
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideProgressBar() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }


    interface CheckThrottleCallBack {
        void onResponse();
    }

}
