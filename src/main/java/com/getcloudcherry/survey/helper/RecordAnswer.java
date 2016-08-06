package com.getcloudcherry.survey.helper;

import com.getcloudcherry.survey.model.SurveyAnswers;
import com.getcloudcherry.survey.httpclient.APIHelper;
import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshdubey on 8/4/16.
 */
public class RecordAnswer {
    public static RecordAnswer mInstance;
    public SurveyAnswers mSurveyAnswer;
    public long mTotalDuration = 0;
    public long mStartTime = 0;
    public Map<String, Answer> mAnswers = new HashMap<>();

    public static RecordAnswer getInstance() {
        if (mInstance == null) {
            mInstance = new RecordAnswer();
        }
        return mInstance;
    }

    public void startedAt(long iStartTime) {
        mStartTime = iStartTime;
    }

    public void recordAnswer(SurveyQuestions iQuestion, String iAnswer) {
        mAnswers.put(iQuestion.id, new Answer(iQuestion.id, iQuestion.text, iAnswer, 0));
    }

    public SurveyAnswers getAnswers() {
        if (mSurveyAnswer == null)
            mSurveyAnswer = new SurveyAnswers();
        mSurveyAnswer.responseDateTime = APIHelper.getSystemTimeInBelowFormat();
        mSurveyAnswer.responseDuration = (System.currentTimeMillis() - mStartTime) / 1000;
        mSurveyAnswer.surveyClient = "mobile";
        mSurveyAnswer.responses = new ArrayList<>(mAnswers.values());
        return mSurveyAnswer;
    }

    public void reset(){
        mAnswers.clear();
        mSurveyAnswer = null;
    }
}
