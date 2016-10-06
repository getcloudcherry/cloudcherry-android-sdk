package com.getcloudcherry.survey.filter;

import android.util.Log;

import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SequenceComparaor;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by riteshdubey on 8/8/16.
 */
public class QuestionFilterHelper {
//    private static QuestionFilterHelper ourInstance = new QuestionFilterHelper();
//
//    public static QuestionFilterHelper getInstance() {
//        return ourInstance;
//    }

    private QuestionFilterHelper() {
    }

    /**
     * All the question filtering logic goes here
     *
     * @param iResponse Response from server
     * @return arraylist of filtered questions
     */
    public static ArrayList<SurveyQuestions> getFilteredQuestions(SurveyResponse iResponse) {
        SurveyCC.getInstance().getConditionalSurveyQuestions().clear();
        ArrayList<SurveyQuestions> aFilteredQuestions = new ArrayList<>();
        for (SurveyQuestions aQuestion : iResponse.questions) {
            if (QuestionTypes.isSupportedQuestion(aQuestion) && !aQuestion.isRetired) {
                if (!RecordAnswer.getInstance().checkIfPreFillExists(aQuestion) && !aQuestion.staffFill && !aQuestion.apiFill) {
                    Log.i("Filtered Question", aQuestion.toString());
                    if (aQuestion.conditionalFilter != null && (aQuestion.conditionalFilter.filterquestions == null || aQuestion.conditionalFilter.filterquestions.size() == 0)) {
                        aFilteredQuestions.add(aQuestion);
                    } else {
                        SurveyCC.getInstance().addConditionalSurveyQuestions(aQuestion);
                    }
                }
            }
        }
        Collections.sort(aFilteredQuestions, new SequenceComparaor());
        return aFilteredQuestions;
    }
}
