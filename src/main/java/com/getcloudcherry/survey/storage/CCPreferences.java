package com.getcloudcherry.survey.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.getcloudcherry.survey.helper.GsonHelper;
import com.getcloudcherry.survey.model.LoginToken;


public class CCPreferences {

    private static String PREFS_FILE_NAME = "cc_user_data_prefs";
    public static final String USER_DETAIL = "user_data";
    public static final String FIRST_LAUNCH = "first_launch";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String DEVICE_TOKEN = "device_token";

    Context mContext;

    private static CCPreferences mInstance = null;

    public CCPreferences(Context iContext) {
        this.mContext = iContext;
    }

    public static final synchronized CCPreferences getInstance(Context iContext) {
        if (mInstance == null) {
            mInstance = new CCPreferences(iContext);

        } else {
        }
        return mInstance;
    }

    public void setUserDetail(LoginToken iData) {
        String aUserData = GsonHelper.toUserJson(iData);
        Log.i("UserData", aUserData);
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putString(USER_DETAIL, aUserData);
        prefsEditor.apply();
        if(!TextUtils.isEmpty(iData.access_token)) {
            saveString(ACCESS_TOKEN, iData.access_token);
        }
    }

    public LoginToken getUserData() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return (LoginToken) GsonHelper.getGson(aMyPrefs.getString(USER_DETAIL, ""), LoginToken.class);
    }

    ///--------------------------------------------------------------------------------------

    public void saveString(String iKey, String iValue) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putString(iKey, iValue);
        prefsEditor.commit();
    }

    public void saveBoolean(String iKey, boolean iValue) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putBoolean(iKey, iValue);
        prefsEditor.commit();
    }

    public void saveInteger(String iKey, int iValue) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.putInt(iKey, iValue);
        prefsEditor.commit();
    }

    public String getString(String iKey, String iDefault) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getString(iKey, "");
    }

    public boolean getBoolean(String iKey, boolean iDefault) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getBoolean(iKey, iDefault);
    }

    public int getInteger(String iKey, int iDefault) {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return aMyPrefs.getInt(iKey, iDefault);
    }

    ///--------------------------------------------------------------------------------------

    public void setFirstLaunch(boolean iData) {
        saveBoolean(FIRST_LAUNCH, iData);
    }

    public boolean getFirstLaunch() {
        return getBoolean(FIRST_LAUNCH, false);
    }

    public void setAccessToken(String iData) {
        saveString(ACCESS_TOKEN, iData);
    }

    public String getAccessToken() {
        return getString(ACCESS_TOKEN, "");
    }

    /**
     * Saves Device Token in preferences
     *
     * @param iToken
     */
    public void setDeviceToken(String iToken) {
        saveString(DEVICE_TOKEN, iToken);
    }

    /**
     * Gets saved Device Token from preference
     *
     * @return Long
     */
    public String getDeviceToken() {
        return getString(DEVICE_TOKEN, "");
    }

    public void clearSavedData() {
        SharedPreferences aMyPrefs = mContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = aMyPrefs.edit();
        prefsEditor.clear().commit();
    }
}
