package com.getcloudcherry.survey.interfaces;

public interface ExitCallBack {

    enum SurveyState {
        VIEWED {
            @Override
            public String toString() {
                return "Viewed";
            }
        },
        PARTIALLY_COMPLETED {
            @Override
            public String toString() {
                return "Partially Completed";
            }
        },
        COMPLETED {
            @Override
            public String toString() {
                return "Completed";
            }
        };
    }

    void onSurveyExited(SurveyState iSurveyState);
}
