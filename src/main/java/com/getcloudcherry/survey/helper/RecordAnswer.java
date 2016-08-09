package com.getcloudcherry.survey.helper;

import android.util.Log;

import com.getcloudcherry.survey.httpclient.APIHelper;
import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.SurveyAnswers;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshdubey on 8/4/16.
 */

/**
 * Helper Singleton class to capture survey answers
 */
public class RecordAnswer {
    public static RecordAnswer mInstance;
    public SurveyAnswers mSurveyAnswer;
    public long mStartTime = 0;
    // Used for storing survey that has been answered
    public Map<String, Answer> mAnswers = new HashMap<>();
    // Used for storing pre-fill objects
    public Map<String, Object> mPreFillAnswers = new HashMap<>();

    /**
     * Gets the singleton instance of this class
     *
     * @return
     */
    public static RecordAnswer getInstance() {
        if (mInstance == null) {
            mInstance = new RecordAnswer();
        }
        return mInstance;
    }

    /**
     * Initializes the start time of the survey
     *
     * @param iStartTime time in milliseconds
     */
    public void startedAt(long iStartTime) {
        mStartTime = iStartTime;
    }

    /**
     * Saves answer to a particular question
     *
     * @param iQuestion SurveyQuestion object
     * @param iAnswer   answer string
     */
    public void recordAnswer(SurveyQuestions iQuestion, String iAnswer) {
        mAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer));
    }

    /**
     * Saves answer to a particular question
     *
     * @param iQuestion SurveyQuestion object
     * @param iAnswer   answer value in integer
     */
    public void recordAnswer(SurveyQuestions iQuestion, int iAnswer) {
        mAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer));
    }

    public void preFillQuestionWithTags(HashMap<String, Object> iPreFill) {
        mPreFillAnswers = iPreFill;
    }

    /**
     * Gets the Answer object to be sent to server
     *
     * @return SurveyAnswers instance
     */
    public SurveyAnswers getAnswers() {
        if (mSurveyAnswer == null)
            mSurveyAnswer = new SurveyAnswers();
        mSurveyAnswer.responseDateTime = APIHelper.getSystemTimeInBelowFormat();
        mSurveyAnswer.responseDuration = (System.currentTimeMillis() - mStartTime) / 1000;
        mSurveyAnswer.surveyClient = "mobile";
        mSurveyAnswer.responses = new ArrayList<>(mAnswers.values());
        return mSurveyAnswer;
    }

    /**
     * Compare questionTags against the pre-fill tag and record answers
     *
     * @param iQuestion SurveyQuestion object
     * @return
     */
    public boolean checkIfPreFillExists(SurveyQuestions iQuestion) {

        for (String aTag : iQuestion.questionTags) {
            if (mPreFillAnswers.get(aTag) != null) {
                Object aAnswerObject = mPreFillAnswers.get(aTag);
                if (aAnswerObject instanceof String) {
                    Log.i("Pre-fill question", iQuestion.text + " : " + aAnswerObject.toString());
                    recordAnswer(iQuestion, (String) aAnswerObject);
                } else if (aAnswerObject instanceof Integer) {
                    Log.i("Pre-fill question", iQuestion.text + " : " + aAnswerObject.toString());
                    recordAnswer(iQuestion, (Integer) aAnswerObject);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Resets the answer object
     */
    public void reset() {
        mAnswers.clear();
        mPreFillAnswers.clear();
        mStartTime = 0;
        mSurveyAnswer = null;
    }
}
