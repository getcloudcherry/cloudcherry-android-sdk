package com.getcloudcherry.survey.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.builder.SurveyConfigBuilder;

/**
 * Created by riteshdubey on 7/30/16.
 */

/**
 * Helper class to configure and access SDK related functions
 */
public class SurveyCC {
    public static SurveyCC mInstance;
    public static Context mContext;
    public static String mSurveyToken;

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
    public int FOOTER_PAGE_FONT_COLOR = android.R.color.white;


    /**
     * Initializes the SDK with application context
     *
     * @param iContext application context
     * @param iSurveyToken     Survey Token provided by cloud cherry
     */
    public static void initialise(Context iContext, String iSurveyToken) {
        mContext = iContext;
        mSurveyToken = iSurveyToken;
        getInstance();
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
        } else if (TextUtils.isEmpty(mSurveyToken)) {
            throw new RuntimeException("SDK key not initialized");
        }
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
}
