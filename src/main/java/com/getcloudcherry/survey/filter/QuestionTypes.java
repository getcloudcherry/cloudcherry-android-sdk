package com.getcloudcherry.survey.filter;

import com.getcloudcherry.survey.model.SurveyQuestions;

/**
 * Created by riteshdubey on 8/8/16.
 * <br> Question Types that are supported by the SDK
 */
public class QuestionTypes {
    public static final String TYPE_SCALE = "Scale";
    public static final String TYPE_MULTI_LINE_TEXT = "MultilineText";

    /** Method to check is the question type is supported by the SDK
     *
     * @param iQuestion SurveyQuestion object
     * @return true - if displayType is supported otherwise false
     */
    public static boolean isSupportedQuestion(SurveyQuestions iQuestion){
        switch (iQuestion.displayType){
            case QuestionTypes.TYPE_SCALE:
                return true;
            case QuestionTypes.TYPE_MULTI_LINE_TEXT:
                return true;
        }
        return false;
    }
}
