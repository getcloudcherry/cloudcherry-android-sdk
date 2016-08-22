package com.getcloudcherry.survey.helper;

import com.getcloudcherry.survey.model.Data;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshdubey on 8/4/16.
 * Helper Singleton class to capture Analytics data
 */
public class RecordAnalytics {
    public static RecordAnalytics mInstance;
    public long mStartTime = 0;
    public Map<String, Data> mAnalyticsData = new HashMap<>();

    /**
     * Gets the singleton instance of this class
     *
     * @return
     */
    public static RecordAnalytics getInstance() {
        if (mInstance == null) {
            mInstance = new RecordAnalytics();
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
     * Get analytics data for the question id
     *
     * @param iQuestionId question id
     * @return analytics data
     */
    public Data getAnalyticsDataForQuestionId(String iQuestionId) {
        if (mAnalyticsData != null) {
            return mAnalyticsData.get(iQuestionId);
        }
        return null;
    }

    /**
     * Gets All the analytics data dump
     *
     * @return array list of data object
     */
    public ArrayList<Data> getAnalyticsDataDump() {
        if (mAnalyticsData != null) {
            return new ArrayList<Data> (mAnalyticsData.values());
        }
        return new ArrayList<Data>();
    }

    /**
     * Captures analytics data for question that is being displayed
     *
     * @param iQuestion question object that has to be analysed
     */
    public void capture(SurveyQuestions iQuestion) {
        Data aData = mAnalyticsData.get(iQuestion.id);
        if (aData != null) {
            aData.impression = aData.impression + 1;
            aData.lastViewedAt = System.currentTimeMillis();
            mAnalyticsData.put(iQuestion.id, aData);
        } else {
            aData = new Data(iQuestion.id, iQuestion.text, 1, System.currentTimeMillis());
            mAnalyticsData.put(iQuestion.id, aData);
        }
        SurveyCC.getInstance().sendAnalyticsDataDump();
    }

    /**
     * Ends analytics data capture for question that has moved out of the view
     *
     * @param iQuestion question object that has to be analysed
     */
    public void end(SurveyQuestions iQuestion) {
        Data aData = mAnalyticsData.get(iQuestion.id);
        if (aData != null) {
            aData.duration = aData.duration + (System.currentTimeMillis() - aData.lastViewedAt) / 1000;
            mAnalyticsData.put(iQuestion.id, aData);
            SurveyCC.getInstance().sendAnalyticsData(iQuestion);
        }
    }

    /**
     * Resets the answer object
     */
    public void reset() {
        mAnalyticsData.clear();
        mStartTime = 0;
    }
}
