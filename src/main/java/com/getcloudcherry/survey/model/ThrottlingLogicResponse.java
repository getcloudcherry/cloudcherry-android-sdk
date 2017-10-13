package com.getcloudcherry.survey.model;

import java.util.ArrayList;

public class ThrottlingLogicResponse {

    public ThrottlingLogic logic;

    public class ThrottlingLogic {
        public String user;
        public String uniqueIDQuestionIdOrTag;
        public ArrayList<Logics> logics;
        public boolean advancedThrottling;
        public ArrayList<String> inputIds;

        public class Logics {
            public Filter filter;
            public boolean conditionSurveySent;
            public boolean conditionSurveyAnswered;
            public boolean surveyEmailOpened;

            public class Filter {
                public ArrayList<String> location;
                public String afterdate;
                public String beforedate;
            }
        }
    }

}
