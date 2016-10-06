package com.getcloudcherry.survey.builder;

import android.text.TextUtils;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.model.CustomTextStyle;

/**
 * Created by riteshdubey on 7/30/16.
 */

/**
 * Configuration builder helper class to change predefined properties of the SDK
 */
public class SurveyConfigBuilder {

    public String WELCOME_MESSAGE = "";
    public String THANKS_MESSAGE = "";
    public boolean SHOW_WELCOME_MESSAGE = false;
    public boolean SHOW_THANKS_MESSAGE = false;

    public String HEADER_BACKGROUND_COLOR = "#FFFFFF";
    public String HEADER_LOGO = "";
    public String HEADER_FONT_PATH = "";
    public int HEADER_FONT_SIZE = 16;
    public String HEADER_FONT_COLOR = "#000000";
    public int ACTION_BAR_BACKGROUND_COLOR = R.color.color_lightgrey;
    public int ACTION_BAR_FONT_COLOR = android.R.color.white;
    public int ACTION_BAR_FONT_SIZE = 16;
    public String ACTION_BAR_FONT_PATH = "";

    //Footer
    public String FOOTER_BACKGROUND_COLOR = "#FFFFFF";
    public String FOOTER_BUTTON_FONT_PATH = "";
    public int FOOTER_BUTTON_FONT_SIZE = 13;
    public int FOOTER_BUTTON_FONT_COLOR = android.R.color.black;
    public int FOOTER_BUTTON_COLOR = R.color.color_lightgrey;
    public String FOOTER_PAGE_FONT_PATH = "";
    public int FOOTER_PAGE_FONT_SIZE = 12;
    public int FOOTER_PAGE_FONT_COLOR = android.R.color.white;
    //Content
    public String CONTENT_BACKGROUND_COLOR = "#FFFFFF";
    public String CONTENT_FONT_PATH = "";
    public int CONTENT_FONT_SIZE = 16;
    public int CONTENT_FONT_COLOR = R.color.colorPrimary;

    //Question Type
    public int CUSTOM_TEXT_STYLE = CustomTextStyle.STYLE_CIRCLE;

    public static class Builder {

        public String welcomeMessage = "";
        public String thanksMessage = "";
        public boolean showWelcomeMessage = false;
        public boolean showThanksMessage = false;

        private String headerBackgroundColor;
        private String headerLogo;
        private String headerFontPath;
        private int headerFontSize;
        private String headerFontColor;
        private int actionBarBackgroundColor;
        private String actionBarFontPath;
        private int actionBarFontSize;
        private int actionBarFontColor;

        //Footer
        private String footerBackgroundColor;
        private String footerButtonFontPath;
        private int footerButtonFontSize;
        private int footerButtonFontColor;
        private int footerButtonColor;
        private String footerPaginationFontPath;
        private int footerPaginationFontSize;
        private int footerPaginationFontColor;

        //Content
        private String contentBackgroundColor;
        private String contentFontPath;
        private int contentFontSize;
        private int contentFontColor;

        //QuestionType
        private int customTextStyle;

        /**
         * Builder method for setting welcome message
         *
         * @param iMessage welcome message text
         * @return Builder instance
         */
        public Builder welcomeMessage(String iMessage) {
            this.welcomeMessage = iMessage;
            return this;
        }

        /**
         * Builder method for setting thank you message
         *
         * @param iMessage thank you message text
         * @return Builder instance
         */
        public Builder thankYouMessage(String iMessage) {
            this.thanksMessage = iMessage;
            return this;
        }

        /**
         * Builder method for setting whether to show thanks screen
         *
         * @param iCanShow
         * @return Builder instance
         */
        public Builder showThankYouMessage(boolean iCanShow) {
            this.showThanksMessage = iCanShow;
            return this;
        }

        /**
         * Builder method for setting whether to show welcome screen
         *
         * @param iCanShow
         * @return Builder instance
         */
        public Builder showWelcomeMessage(boolean iCanShow) {
            this.showWelcomeMessage = iCanShow;
            return this;
        }

        /**
         * Builder method for setting background color of the header
         *
         * @param iBackgroundColor background color string
         * @return Builder instance
         */
        public Builder headerBackgroundColor(String iBackgroundColor) {
            this.headerBackgroundColor = iBackgroundColor;
            return this;
        }

        /**
         * Builder method for setting the headerLogo of actionbar
         *
         * @param iLogoUrl header logo url
         * @return Builder instance
         */
        public Builder headerLogo(String iLogoUrl) {
            this.headerLogo = iLogoUrl;
            return this;
        }

        /**
         * Builder method for setting the custom font path of action bar title
         *
         * @param iPath name of the font placed in assets/fonts/ folder
         * @return
         */
        public Builder actionBarFontPath(String iPath) {
            this.actionBarFontPath = iPath;
            return this;
        }

        /**
         * Builder method for setting the background color of action bar
         *
         * @param iBackgroundColor font size
         * @return
         */
        public Builder actionBarBackgroundColor(int iBackgroundColor) {
            this.actionBarBackgroundColor = iBackgroundColor;
            return this;
        }

        /**
         * Builder method for setting the font size of action bar title
         *
         * @param iFontSize font size
         * @return
         */
        public Builder actionBarFontSize(int iFontSize) {
            this.actionBarFontSize = iFontSize;
            return this;
        }

        /**
         * Builder method for setting the font color of action bar title
         *
         * @param iFontColor font color
         * @return
         */
        public Builder actionBarFontColor(int iFontColor) {
            this.actionBarFontColor = iFontColor;
            return this;
        }

        /**
         * Builder method for setting the custom font path of header
         *
         * @param iPath name of the font placed in assets/fonts/ folder
         * @return
         */
        public Builder headerFontPath(String iPath) {
            this.headerFontPath = iPath;
            return this;
        }

        /**
         * Builder method for setting the font size of header
         *
         * @param iFontSize font size
         * @return
         */
        public Builder headerFontSize(int iFontSize) {
            this.headerFontSize = iFontSize;
            return this;
        }

        /**
         * Builder method for setting the font color of header
         *
         * @param iFontColor font color
         * @return
         */
        public Builder headerFontColor(String iFontColor) {
            this.headerFontColor = iFontColor;
            return this;
        }

        /**
         * Builder method for setting background color of the footer
         *
         * @param iBackgroundColor background color string
         * @return Builder instance
         */
        public Builder footerBackgroundColor(String iBackgroundColor) {
            this.footerBackgroundColor = iBackgroundColor;
            return this;
        }

        /**
         * Builder method for setting the custom font path for the footer buttons
         *
         * @param iPath name of the font placed in assets/fonts/ folder
         * @return
         */
        public Builder footerButtonFontPath(String iPath) {
            this.footerButtonFontPath = iPath;
            return this;
        }

        /**
         * Builder method for setting the font size of the footer buttons
         *
         * @param iFontSize font size
         * @return
         */
        public Builder footerButtonFontSize(int iFontSize) {
            this.footerButtonFontSize = iFontSize;
            return this;
        }

        /**
         * Builder method for setting the font color of the footer buttons
         *
         * @param iFontColor font color
         * @return
         */
        public Builder footerButtonFontColor(int iFontColor) {
            this.footerButtonFontColor = iFontColor;
            return this;
        }

        /**
         * Builder method for setting the background color of the footer buttons
         *
         * @param iButtonColor button color resource id
         * @return
         */
        public Builder footerButtonColor(int iButtonColor) {
            this.footerButtonColor = iButtonColor;
            return this;
        }

        // Page Config

        /**
         * Builder method for setting the custom font path for the footer pagination
         *
         * @param iPath name of the font placed in assets/fonts/ folder
         * @return
         */
        public Builder footerPaginationFontPath(String iPath) {
            this.footerPaginationFontPath = iPath;
            return this;
        }

        /**
         * Builder method for setting the font size of the footer pagination
         *
         * @param iFontSize font size
         * @return
         */
        public Builder footerPaginationFontSize(int iFontSize) {
            this.footerPaginationFontSize = iFontSize;
            return this;
        }

        /**
         * Builder method for setting the font color of the footer pagination
         *
         * @param iFontColor font color
         * @return
         */
        public Builder footerPaginationFontColor(int iFontColor) {
            this.footerPaginationFontColor = iFontColor;
            return this;
        }

        /**
         * Builder method for setting background color of the screen property
         *
         * @param iBackgroundColor background color string
         * @return Builder instance
         */
        public Builder contentBackgroundColor(String iBackgroundColor) {
            this.contentBackgroundColor = iBackgroundColor;
            return this;
        }

        /**
         * Builder method for setting the custom font path for the content area text
         *
         * @param iPath name of the font placed in assets/fonts/ folder
         * @return
         */
        public Builder contentFontPath(String iPath) {
            this.contentFontPath = iPath;
            return this;
        }

        /**
         * Builder method for setting the font size of the content area text
         *
         * @param iFontSize font size
         * @return
         */
        public Builder contentFontSize(int iFontSize) {
            this.contentFontSize = iFontSize;
            return this;
        }

        /**
         * Builder method for setting the font color of content area text
         *
         * @param iFontColor font color
         * @return
         */
        public Builder contentFontColor(int iFontColor) {
            this.contentFontColor = iFontColor;
            return this;
        }

        public Builder customTextStyle(int iStyle) {
            this.customTextStyle = iStyle;
            return this;
        }


        //return fully build object
        public SurveyConfigBuilder build() {
            return new SurveyConfigBuilder(this);
        }
    }

    //private constructor to enforce object creation through builder
    private SurveyConfigBuilder(Builder builder) {
//        DIALOG_COLOR = builder.dialogColor;
        if (!TextUtils.isEmpty(builder.headerBackgroundColor))
            this.HEADER_BACKGROUND_COLOR = builder.headerBackgroundColor;
        if (!TextUtils.isEmpty(builder.headerLogo))
            this.HEADER_LOGO = builder.headerLogo;
        if (!TextUtils.isEmpty(builder.headerFontPath))
            this.HEADER_FONT_PATH = builder.headerFontPath;
        if (builder.headerFontSize != 0)
            this.HEADER_FONT_SIZE = builder.headerFontSize;
        if (!TextUtils.isEmpty(builder.headerFontColor))
            this.HEADER_FONT_COLOR = builder.headerFontColor;

        if (builder.actionBarBackgroundColor != 0)
            this.ACTION_BAR_BACKGROUND_COLOR = builder.actionBarBackgroundColor;
        if (!TextUtils.isEmpty(builder.actionBarFontPath))
            this.ACTION_BAR_FONT_PATH = builder.actionBarFontPath;
        if (builder.actionBarFontSize != 0)
            this.ACTION_BAR_FONT_SIZE = builder.actionBarFontSize;
        if (builder.actionBarFontColor != 0)
            this.ACTION_BAR_FONT_COLOR = builder.actionBarFontColor;

        //Footer
        if (!TextUtils.isEmpty(builder.footerBackgroundColor))
            this.FOOTER_BACKGROUND_COLOR = builder.footerBackgroundColor;
        if (!TextUtils.isEmpty(builder.footerButtonFontPath))
            this.FOOTER_BUTTON_FONT_PATH = builder.footerButtonFontPath;
        if (builder.footerButtonFontSize != 0)
            this.FOOTER_BUTTON_FONT_SIZE = builder.footerButtonFontSize;
        if (builder.footerButtonFontColor != 0)
            this.FOOTER_BUTTON_FONT_COLOR = builder.footerButtonFontColor;
        if (builder.footerButtonColor != 0)
            this.FOOTER_BUTTON_COLOR = builder.footerButtonColor;
        if (!TextUtils.isEmpty(builder.footerPaginationFontPath))
            this.FOOTER_PAGE_FONT_PATH = builder.footerPaginationFontPath;
        if (builder.footerPaginationFontSize != 0)
            this.FOOTER_PAGE_FONT_SIZE = builder.footerPaginationFontSize;
        if (builder.footerPaginationFontColor != 0)
            this.FOOTER_PAGE_FONT_COLOR = builder.footerPaginationFontColor;

        //Content
        if (!TextUtils.isEmpty(builder.contentBackgroundColor))
            this.CONTENT_BACKGROUND_COLOR = builder.contentBackgroundColor;
        if (!TextUtils.isEmpty(builder.contentFontPath))
            this.CONTENT_FONT_PATH = builder.contentFontPath;
        if (builder.contentFontSize != 0)
            this.CONTENT_FONT_SIZE = builder.contentFontSize;
        if (builder.contentFontColor != 0)
            this.CONTENT_FONT_COLOR = builder.contentFontColor;

        //Question Type
        if (builder.customTextStyle != 0)
            this.CUSTOM_TEXT_STYLE = builder.customTextStyle;

        if (!TextUtils.isEmpty(builder.welcomeMessage))
            this.WELCOME_MESSAGE = builder.welcomeMessage;
        if (!TextUtils.isEmpty(builder.thanksMessage))
            this.THANKS_MESSAGE = builder.thanksMessage;
        this.SHOW_THANKS_MESSAGE = builder.showThanksMessage;
        this.SHOW_WELCOME_MESSAGE = builder.showWelcomeMessage;
    }
}