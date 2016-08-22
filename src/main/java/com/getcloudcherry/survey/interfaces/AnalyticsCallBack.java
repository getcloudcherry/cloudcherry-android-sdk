package com.getcloudcherry.survey.interfaces;

import com.getcloudcherry.survey.model.Data;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/21/16.
 */
public interface AnalyticsCallBack {
    void onSurveyQuestionSeen(Data iData);
    void onUpdatedAnalyticsData(ArrayList<Data> iData);
}
