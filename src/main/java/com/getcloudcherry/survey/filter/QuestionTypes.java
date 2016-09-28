package com.getcloudcherry.survey.filter;

import com.getcloudcherry.survey.model.SurveyQuestions;

/**
 * Created by riteshdubey on 8/8/16.
 * <br> Question Types that are supported by the SDK
 */
public class QuestionTypes {
    public static final String TYPE_SCALE = "Scale";
    public static final String TYPE_MULTI_LINE_TEXT = "MultilineText";
    public static final String TYPE_SINGLE_LINE_TEXT = "Text";
    public static final String TYPE_SINGLE_LINE_NUMBER = "Number";
    public static final String TYPE_RATING_STAR = "Star-5";
    public static final String TYPE_RATING_SMILEY = "Smile-5";
    public static final String TYPE_MULTI_SELECT = "MultiSelect";
    public static final String TYPE_SELECT = "Select";
    public static final String TYPE_DROPDOWN = "Dropdown";

    /**
     * Method to check is the question type is supported by the SDK
     *
     * @param iQuestion SurveyQuestion object
     * @return true - if displayType is supported otherwise false
     */
    public static boolean isSupportedQuestion(SurveyQuestions iQuestion) {
        switch (iQuestion.displayType) {
            case QuestionTypes.TYPE_SCALE:
                return true;
            case QuestionTypes.TYPE_MULTI_LINE_TEXT:
                return true;
            case QuestionTypes.TYPE_SINGLE_LINE_TEXT:
                return true;
            case QuestionTypes.TYPE_SINGLE_LINE_NUMBER:
                return true;
            case QuestionTypes.TYPE_RATING_STAR:
                return true;
            case QuestionTypes.TYPE_MULTI_SELECT:
                return true;
            case QuestionTypes.TYPE_RATING_SMILEY:
                return true;
            case QuestionTypes.TYPE_SELECT:
                return true;
        }
        return false;
    }
}
