package com.getcloudcherry.survey.helper;

import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.Comparator;

/**
 * Created by riteshdubey on 8/25/16.
 */
public class SequenceComparaor implements Comparator<SurveyQuestions> {
    @Override
    public int compare(SurveyQuestions a, SurveyQuestions b) {
        return a.sequence < b.sequence ? -1 : a.sequence == b.sequence ? 0 : 1;
    }
}