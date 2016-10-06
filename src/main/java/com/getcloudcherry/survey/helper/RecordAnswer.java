package com.getcloudcherry.survey.helper;

import android.text.TextUtils;
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
 * Helper Singleton class to capture survey answers
 */
public class RecordAnswer {
    public static RecordAnswer mInstance;
    public SurveyAnswers mSurveyAnswer;
    public long mStartTime = 0;
    // Used for storing survey that has been answered
    public Map<String, Answer> mAnswers = new HashMap<>();
    public Map<String, Answer> mPartialResponse = new HashMap<>();
    // Used for storing pre-fill objects
    public Map<String, Object> mPreFillTags = new HashMap<>();
    // Used to store pre-fill answer objects mapped to question id
    public Map<String, Answer> mPreFillAnswers = new HashMap<>();
    private static final String DEVICE_ID = "InApp-Droid";

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
        if (!TextUtils.isEmpty(iAnswer)) {
            mPartialResponse.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer));
            mAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer));

        } else {
            if (mAnswers.containsKey(iQuestion.id)) {
                mAnswers.remove(iQuestion.id);
            }
            if (mPartialResponse.containsKey(iQuestion.id)) {
                mPartialResponse.remove(iQuestion.id);
            }
        }
    }

    /**
     * Saves answer to a particular question
     *
     * @param iQuestion SurveyQuestion object
     * @param iAnswer   answer value in integer
     */
    public void recordAnswer(SurveyQuestions iQuestion, Integer iAnswer) {
        if (iAnswer != null) {
            mPartialResponse.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer));
            mAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer));
        } else {
            if (mAnswers.containsKey(iQuestion.id)) {
                mAnswers.remove(iQuestion.id);
            }
            if (mPartialResponse.containsKey(iQuestion.id)) {
                mPartialResponse.remove(iQuestion.id);
            }
        }
    }

    /**
     * Saves answer to a particular question
     *
     * @param iQuestionId question id
     * @param iAnswer     answer object
     */
    public void recordAnswer(String iQuestionId, Answer iAnswer) {
        mAnswers.put(iQuestionId, iAnswer);
    }

    public void preFillQuestionWithTags(HashMap<String, Object> iPreFill) {
        mPreFillTags = iPreFill;
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
        mSurveyAnswer.surveyClient = DEVICE_ID;
        mSurveyAnswer.responses = new ArrayList<>(mAnswers.values());
        return mSurveyAnswer;
    }


    /**
     * Gets the saved answer array object
     *
     * @return
     */
    public ArrayList<Answer> getSavedAnswerList() {
        return new ArrayList<Answer>(mAnswers.values());
    }

    /**
     * Gets the saved partial answer array object for particular question id as needed by the partialResponse API
     *
     * @param iQuestionId question id
     * @return
     */
    public ArrayList<Answer> getPartialAnswerForQuestionId(String iQuestionId) {
        ArrayList<Answer> aAnswer = new ArrayList<>();
        if (mPartialResponse.get(iQuestionId) != null)
            aAnswer.add(mPartialResponse.get(iQuestionId));
        if (mPreFillAnswers.size() > 0)
            aAnswer.addAll(mPreFillAnswers.values());
        return aAnswer;
    }

    /**
     * Gets the saved answer object for question id
     *
     * @param iQuestionId question id
     * @return
     */
    public Answer getAnswerForQuestionId(String iQuestionId) {
        return mPartialResponse.get(iQuestionId);
    }

    /**
     * Compare questionTags against the pre-fill tag and record answers
     *
     * @param iQuestion SurveyQuestion object
     * @return
     */
    public boolean checkIfPreFillExists(SurveyQuestions iQuestion) {

        if (iQuestion.questionTags != null)
            for (String aTag : iQuestion.questionTags) {
                if (mPreFillTags.get(aTag) != null) {
                    Object aAnswerObject = mPreFillTags.get(aTag);
                    if (aAnswerObject instanceof String) {
                        Log.i("Pre-fill question", iQuestion.text + " : " + aAnswerObject.toString());
                        recordAnswer(iQuestion, (String) aAnswerObject);
                        mPreFillAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, (String) aAnswerObject));
                    } else if (aAnswerObject instanceof Integer) {
                        Log.i("Pre-fill question", iQuestion.text + " : " + aAnswerObject.toString());
                        recordAnswer(iQuestion, (Integer) aAnswerObject);
                        mPreFillAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, (Integer) aAnswerObject));
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
        mPreFillTags.clear();
        mStartTime = 0;
    }

}
