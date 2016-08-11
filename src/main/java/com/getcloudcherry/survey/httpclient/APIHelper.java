package com.getcloudcherry.survey.httpclient;

import android.graphics.Color;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class APIHelper {

    public static final String GET_QUESTIONS = "SurveyByToken/{token}/{deviceId}";
    public static final String POST_ANSWER_PARTIAL = "PartialSurvey/{id}/{complete}";
    public static final String POST_ANSWER_ALL = "SurveyByToken/{token}";
    public static final String POST_CREATE_SURVEY_TOKEN = "SurveyToken";
    public static final String POST_LOGIN_TOKEN = "LoginToken";

    /**
     * Validates email
     *
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    /**
     * Method to check if the color is dark or light
     *
     * @param color
     * @return
     */
    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }

    public static String getSystemTimeInBelowFormat() {
        String timestamp = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
        return timestamp;
    }
}
