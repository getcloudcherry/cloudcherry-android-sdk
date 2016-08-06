package com.getcloudcherry.survey.model;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/4/16.
 */
public class SurveyAnswers {
    public String id;
    public String user;
    public String locationId;
    public String responseDateTime;
    public long responseDuration;
    public String surveyClient;
    public ArrayList<Answer> responses;
}
