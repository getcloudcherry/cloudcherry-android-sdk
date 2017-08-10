package com.getcloudcherry.survey.helper;

import android.util.Log;

import com.getcloudcherry.survey.BuildConfig;

/**
 * Created by riteshdubey on 8/10/16.
 */
public class Constants {
    private static final String TAG = "Cloud-Cherry";
    public static final String GRANT_TYPE = "password";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_BEARER = "Bearer";

    public static final void logVerbose(String iTitle, String iLogMessage) {
        if (BuildConfig.DEBUG)
            Log.v(Constants.TAG, iTitle + " : " + iLogMessage);
    }

    public static final void logInfo(String iTitle, String iLogMessage) {

        if (BuildConfig.DEBUG) {
            Log.i(Constants.TAG, iTitle + " : " + iLogMessage);
        }
    }

    public static final void logError(String iTitle, String iLogMessage,
                                      Exception iException) {
        if (BuildConfig.DEBUG)
            Log.e(Constants.TAG, iTitle + " : " + iLogMessage, iException);

    }

    public static final void logDebug(String iTitle, String iLogMessage) {
        if (BuildConfig.DEBUG)
            Log.d(Constants.TAG, iTitle + " : " + iLogMessage);

    }

    public static final void logWarn(String iTitle, String iLogMessage) {
        if (BuildConfig.DEBUG)
            Log.w(Constants.TAG, iTitle + " : " + iLogMessage);

    }
}
