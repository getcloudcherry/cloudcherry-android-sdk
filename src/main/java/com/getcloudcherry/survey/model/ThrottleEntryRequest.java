package com.getcloudcherry.survey.model;

public class ThrottleEntryRequest {
    public String user;
    public String mobile;
    public String emailId;
    public String customUId;
    public String surveySentDate;
    public String surveyOpenedDate;
    public String channel;
    public boolean isOpened;

    public ThrottleEntryRequest(String user, String mobile, String emailId, String customUId, String surveySentDate, String surveyOpenedDate, String channel, boolean isOpened) {
        this.user = user;
        this.mobile = mobile;
        this.emailId = emailId;
        this.customUId = customUId;
        this.surveySentDate = surveySentDate;
        this.surveyOpenedDate = surveyOpenedDate;
        this.channel = channel;
        this.isOpened = isOpened;
    }
}
