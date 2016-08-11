package com.getcloudcherry.survey.model;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 7/30/16.
 */
public class SurveyResponse {
    public String logoURL;
    public String backgroundURL;
    public String colorCode1;
    public String colorCode2;
    public String colorCode3;
    public String welcomeText;
    public String welcomeImage;
    public String thankyouText;
    public String thankyouImage;
    public ArrayList<SurveyQuestions> questions;
    public boolean skipWelcome;
    public String disclaimerText;
    public String partialResponseId;
}
