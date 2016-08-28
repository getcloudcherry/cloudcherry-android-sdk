package com.getcloudcherry.survey.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.builder.SurveyConfigBuilder;
import com.getcloudcherry.survey.filter.QuestionFilterHelper;
import com.getcloudcherry.survey.interfaces.AnalyticsCallBack;
import com.getcloudcherry.survey.interfaces.ConditionalChangesCallBack;
import com.getcloudcherry.survey.interfaces.FragmentCallBack;
import com.getcloudcherry.survey.interfaces.QuestionCallback;
import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.Data;
import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;
import com.getcloudcherry.survey.model.SurveyToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by riteshdubey on 7/30/16.
 */

/**
 * Helper class to configure and access SDK related functions
 */
public class SurveyCC {
    public static SurveyCC mInstance;
    public static Context mContext;
    private static String mSurveyToken;
    private static boolean mShouldCreate;

    private static String mUserName;
    private static String mPassword;
    private static String mPartialResponseId;
    private boolean mIsPartialCapture;
    public String WELCOME_MESSAGE = "";
    public String THANKS_MESSAGE = "";
    public boolean SHOW_WELCOME_MESSAGE = false;
    public boolean SHOW_THANKS_MESSAGE = false;

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

    //Listeners
    private ArrayList<FragmentCallBack> mFragmentDataCallback = new ArrayList<>();
    private ArrayList<QuestionCallback> mQuestionCallbacks = new ArrayList<>();
    private ArrayList<AnalyticsCallBack> mAnalyticsCallbacks = new ArrayList<>();
    private ArrayList<ConditionalChangesCallBack> mConditionalFlowCallbacks = new ArrayList<>();

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
        mShouldCreate = false;
        getInstance();
    }

    /**
     * Initializes the SDK with application context
     *
     * @param iContext application context
     */
    public static void initialise(Context iContext, String iUsername, String iPassword, SurveyToken iTokenConfig) {
        setCredentials(iUsername, iPassword);
        mTokenConfig = iTokenConfig;
        initialise(iContext, null);
        mShouldCreate = true;
    }

    private static void setCredentials(String iUserName, String iPassword) {
        mUserName = iUserName;
        mPassword = iPassword;
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
     * Gets application context of the application the SDK in integrated to.
     *
     * @return Context - application context
     */
    public Context getContext() {
        checkSDKInitialized();
        return mContext;
    }

    /**
     * Start survey activity
     */
    public void trigger() {
        checkSDKInitialized();
        Intent aIntent = new Intent(mContext, SurveyActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(aIntent);
    }

    /**
     * Start survey activity with some request code awaiting some result from the SDK
     *
     * @param iRequestCode Request code to start an activity
     */
    public void triggerForResult(int iRequestCode) {
        checkSDKInitialized();
        Intent aIntent = new Intent(mContext, SurveyActivity.class);
        aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((AppCompatActivity) mContext).startActivityForResult(aIntent, iRequestCode);
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

}
