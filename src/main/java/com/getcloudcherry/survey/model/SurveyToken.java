package com.getcloudcherry.survey.model;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/10/16.
 */
public class SurveyToken {
    public String id;
    private String user;
    private String note;
    private ArrayList<Answer> preFill;
    private String preFillViaAPICallBack;
    private String validTill;
    public int validUses;
    public String location;
    private boolean isEmailed;
    private boolean isPrinted;
    private boolean requireCaptcha;
    private boolean classicCaptcha;
    private boolean skipWelcome;
    private String rewardCode;
    private String emailQuestion;
    private String cityQuestion;
    private String stateQuestion;
    private String countryQuestion;
    private String regionQuestion;
    private String ipAddressQuestion;
    private boolean samplingMode;
    private int perSamplePresent;
    private String listenFromTwitter;

    public SurveyToken() {
    }

    public SurveyToken(int validUses, String location) {
        this.validUses = validUses;
        this.location = location;
    }
}
