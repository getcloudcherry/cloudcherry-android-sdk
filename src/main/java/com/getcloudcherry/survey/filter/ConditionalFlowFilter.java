package com.getcloudcherry.survey.filter;

import com.getcloudcherry.survey.helper.Constants;
import com.getcloudcherry.survey.helper.RecordAnalytics;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SequenceComparaor;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.model.FilterByQuestions;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.Collections;

/**
 * Created by riteshdubey on 8/24/16.
 */

/**
 * Helper class to handle conditional flow : Display question if the condition satisfies certain criteria
 */
public class ConditionalFlowFilter {

    /**
     * Add or remove questions that satisfies the given conditions
     *
     * @param iQuestion
     * @return
     */
    public synchronized static void filterQuestion(SurveyQuestions iQuestion) {
        int aAddedCount = 0;
        int aRemovedCount = 0;
        if (RecordAnswer.getInstance().getAnswerForQuestionId(iQuestion.id) != null)
            for (SurveyQuestions aQuestion : SurveyCC.getInstance().getConditionalSurveyQuestions()) {
                boolean iSatisfied = false;
                boolean iFailed = false;
                for (FilterByQuestions aFilterByQuestion : aQuestion.conditionalFilter.filterquestions) {
                    if (isAnd(aFilterByQuestion)) {
                        if (doesSatisfy(aFilterByQuestion) && !iFailed) {
                            iSatisfied = true;
                        } else {
                            iFailed = true;
                            break;
                        }
                    } else if (isOr(aFilterByQuestion)) {
                        if (doesSatisfy(aFilterByQuestion)) {
                            iSatisfied = true;
                            break;
                        }
                    }

                }
                if (iSatisfied && !iFailed) {
                    if (!SurveyCC.getInstance().getSurveyQuestions().contains(aQuestion)) {
                        SurveyCC.getInstance().getSurveyQuestions().add(aQuestion);
                        aAddedCount++;
                    }
                } else {
                    if (SurveyCC.getInstance().getSurveyQuestions().contains(aQuestion)) {
                        aRemovedCount++;
                        SurveyCC.getInstance().getSurveyQuestions().remove(aQuestion);
                        RecordAnswer.getInstance().mPartialResponse.remove(aQuestion.id);
                        RecordAnswer.getInstance().mAnswers.remove(aQuestion.id);
                        RecordAnalytics.getInstance().mAnalyticsData.remove(aQuestion.id);
                    }
                }
            }
        if(aAddedCount > 0 || aRemovedCount > 0) {
            Constants.logInfo("Conditional Flow", "Added or Removed");
            Collections.sort(SurveyCC.getInstance().getSurveyQuestions(), new SequenceComparaor());
            SurveyCC.getInstance().sendConditionalFLowQuestionsData(SurveyCC.getInstance().getSurveyQuestions().size());
        }
    }

    /**
     * Checks if groupBy is AND
     *
     * @param iFilterByQuestion
     * @return
     */
    private static boolean isAnd(FilterByQuestions iFilterByQuestion) {
        if (iFilterByQuestion.groupBy == null || iFilterByQuestion.groupBy.equalsIgnoreCase("AND")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the groupBy is OR
     *
     * @param iFilterByQuestion
     * @return
     */
    private static boolean isOr(FilterByQuestions iFilterByQuestion) {
        if (iFilterByQuestion.groupBy != null && iFilterByQuestion.groupBy.equalsIgnoreCase("OR")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the answerCheck array contains any of the "lt", "gt" or "eq" conditions.
     *
     * @param iFilterByQuestion
     * @return
     */
    private static boolean isNumberCheck(FilterByQuestions iFilterByQuestion) {
        if (iFilterByQuestion.answerCheck.get(0).contains("lt") || iFilterByQuestion.answerCheck.get(0).contains("gt") || iFilterByQuestion.answerCheck.get(0).contains("eq")) {
            return true;
        }
        return false;
    }

    /** Contains logic to control conditional flow and whether to show or hide the questions based on the user input
     *
     * @param iFilterByQuestion
     * @return
     */
    private static boolean doesSatisfy(FilterByQuestions iFilterByQuestion) {
        if (isNumberCheck(iFilterByQuestion)) {
            if (iFilterByQuestion.answerCheck.get(0).equalsIgnoreCase("lt")) {
                if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId) != null)
                    if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).numberInput != null && RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).numberInput < iFilterByQuestion.number) {
                        return true;
                    }
            } else if (iFilterByQuestion.answerCheck.get(0).equalsIgnoreCase("gt")) {
                if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId) != null)
                    if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).numberInput != null && RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).numberInput > iFilterByQuestion.number) {
                        return true;
                    }
            } else if (iFilterByQuestion.answerCheck.get(0).equalsIgnoreCase("eq")) {
                if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId) != null)
                    if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).numberInput != null && RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).numberInput == iFilterByQuestion.number) {
                        return true;
                    }
            }
        } else {
            boolean iFoundAll = false;
            for (String aAnswer : iFilterByQuestion.answerCheck) {
                if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId) != null)
                    if (RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).textInput != null && RecordAnswer.getInstance().getAnswerForQuestionId(iFilterByQuestion.questionId).textInput.contains(aAnswer)) {
                        iFoundAll = true;
                    } else {
                        iFoundAll = false;
                        break;
                    }
            }
            if (iFoundAll)
                return true;
        }

        return false;
    }

}
