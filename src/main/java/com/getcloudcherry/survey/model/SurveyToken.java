package com.getcloudcherry.survey.model;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/10/16.
 */
public class SurveyToken {
    public String id;
    public String user;
    public String note;
    public ArrayList<Answer> preFill;
    public String preFillViaAPICallBack;
    public String validTill;
    public int validUses;
    public String location;
    public boolean isEmailed;
    public boolean isPrinted;
    public boolean requireCaptcha;
    public boolean classicCaptcha;
    public boolean skipWelcome;
    public String rewardCode;
    public String emailQuestion;
    public String cityQuestion;
    public String stateQuestion;
    public String countryQuestion;
    public String regionQuestion;
    public String ipAddressQuestion;
    public boolean samplingMode;
    public int perSamplePresent;
    public String listenFromTwitter;

    public SurveyToken() {
    }

    public SurveyToken(int validUses) {
        this.validUses = validUses;
    }
}
