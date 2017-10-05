package com.getcloudcherry.survey.interfaces;

import com.getcloudcherry.survey.model.Data;

import java.util.ArrayList;

/**
 * Created by riteshdubey on 8/21/16.
 */
public interface AnalyticsCallBack {
    void onSurveyQuestionSeen(Data iData);

    void onUpdatedAnalyticsData(ArrayList<Data> iData);

    void onSurveyExited(SurveyExitedAt iSurveyExitedAt);

    enum SurveyExitedAt {
        WELCOME_SCREEN {
            @Override
            public String toString() {
                return "Welcome Screen";
            }
        },
        MID_WAY {
            @Override
            public String toString() {
                return "Mid Way";
            }
        },
        ON_COMPLETION {
            @Override
            public String toString() {
                return "On Completion";
            }
        };
    }

}
