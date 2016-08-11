package com.getcloudcherry.survey.interfaces;

import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/11/16.
 */
public interface QuestionCallback {
    void onGetSurveyResponse(SurveyResponse iResponse);
    void onGetSurveyQuestions(ArrayList<SurveyQuestions> iSurveyQuestions);
}
