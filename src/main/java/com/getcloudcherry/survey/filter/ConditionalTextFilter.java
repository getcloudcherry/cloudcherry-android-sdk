package com.getcloudcherry.survey.filter;

import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.model.FilterByQuestions;
import com.getcloudcherry.survey.model.LeadingOption;
import com.getcloudcherry.survey.model.SurveyQuestions;

/**
 * Created by riteshdubey on 8/24/16.
 */

/**
 * Helper class to handle conditional display text
 */
public class ConditionalTextFilter {
    /**
     * Gets the question text to display that satisfies the given conditions
     *
     * @param iQuestion
     * @return
     */
    public static String filterText(SurveyQuestions iQuestion) {
        String aConditionalText = iQuestion.text;
        if (iQuestion.leadingDisplayTexts == null) {
            aConditionalText = iQuestion.text;
            return aConditionalText;
        } else if (iQuestion.leadingDisplayTexts.size() == 0) {
            aConditionalText = iQuestion.text;
            return aConditionalText;
        } else {
            for (LeadingOption aOption : iQuestion.leadingDisplayTexts) {
                if (aOption.filter != null && aOption.filter.filterquestions != null) {
                    boolean iSatisfied = false;
                    boolean iFailed = false;
                    for (FilterByQuestions aFilterByQuestion : aOption.filter.filterquestions) {
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
                        aConditionalText = aOption.text;
                    }
                }
            }
        }

        return aConditionalText;
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

    /**
     * Contains logic to control conditional display text based on the user input
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
