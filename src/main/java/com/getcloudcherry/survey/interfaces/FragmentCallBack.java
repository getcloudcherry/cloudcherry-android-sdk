package com.getcloudcherry.survey.interfaces;

import com.getcloudcherry.survey.model.SurveyQuestions;

/**
 * Created by riteshdubey on 8/11/16.
 */
public interface FragmentCallBack {
    void onQuestionDisplayed(SurveyQuestions iQuestion, int iPosition, boolean isLastPage);
}
