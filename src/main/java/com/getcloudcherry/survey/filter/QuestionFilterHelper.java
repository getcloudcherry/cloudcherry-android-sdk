package com.getcloudcherry.survey.filter;

import android.util.Log;

import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;

import java.util.ArrayList;

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
        ArrayList<SurveyQuestions> aFilteredQuestions = new ArrayList<>();
        for (SurveyQuestions aQuestion : iResponse.questions) {
            if (QuestionTypes.isSupportedQuestion(aQuestion) && !aQuestion.isRetired) {
                if (!RecordAnswer.getInstance().checkIfPreFillExists(aQuestion) && !aQuestion.staffFill && !aQuestion.apiFill){
                    Log.i("Filtered Question", aQuestion.toString());
                    aFilteredQuestions.add(aQuestion);
                }
            }
        }
        return aFilteredQuestions;
    }
}
