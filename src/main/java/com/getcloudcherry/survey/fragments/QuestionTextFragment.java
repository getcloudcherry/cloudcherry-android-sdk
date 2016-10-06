package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.filter.ConditionalFlowFilter;
import com.getcloudcherry.survey.filter.ConditionalTextFilter;
import com.getcloudcherry.survey.filter.QuestionTypes;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.model.SurveyQuestions;


/**
 * Fragment to display and handle MultilineText type question
 */
public class QuestionTextFragment extends Fragment {
    private EditText mETAnswer;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout;
    private boolean isLastPage;
    private int mCurrentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtraArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_textarea_question, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(mQuestion.id, mETAnswer.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String aAnswer = savedInstanceState.getString(mQuestion.id, "");
            mETAnswer.setText(aAnswer);
            mETAnswer.setSelection(aAnswer.length());
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mETAnswer = (EditText) view.findViewById(R.id.etEditType);
        initializeViewsWithConfig();
        mTVTitle.setText(mQuestion.text);
        if (mQuestion.displayType.equals(QuestionTypes.TYPE_SINGLE_LINE_TEXT))
            mETAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
        else if (mQuestion.displayType.equals(QuestionTypes.TYPE_SINGLE_LINE_NUMBER))
            mETAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * Change view attributes based on the SDK config parameters
     */
    void initializeViewsWithConfig() {
        setHeaderConfig();
    }

    /**
     * Sets Header config
     */
    void setHeaderConfig() {
        //Header
        mTVTitle.setTextColor(Color.parseColor(SurveyCC.getInstance().getHeaderFontColor()));
        mTVTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, SurveyCC.getInstance().gethHeaderFontSize());
        mQuestionHeaderLayout.setBackgroundColor(Color.parseColor(SurveyCC.getInstance().getHeaderBackgroundColor()));
    }

    /**
     * Method to validate if the answer has been provided by the user only when the question has isRequired = true
     *
     * @return boolean
     */
    public boolean validateAnswer() {
        if (mQuestion.isRequired) {
            if (mETAnswer.getText().toString().trim().length() == 0) {
                showToast(getString(R.string.validate_answer));
                return false;
            } else {
                RecordAnswer.getInstance().recordAnswer(mQuestion, mETAnswer.getText().toString().trim());
                submitPartial();
            }
        } else {
            RecordAnswer.getInstance().recordAnswer(mQuestion, mETAnswer.getText().toString().trim());
            // If partial response is enabled then capture
            if (SurveyCC.getInstance().isPartialCapturing()) {
                submitPartial();
            }
        }
        return true;
    }

    /**
     * Contains logic to call the Partial response API to submit partial response
     */
    void submitPartial() {
        ConditionalFlowFilter.filterQuestion(mQuestion);
        if (SurveyCC.getInstance().isPartialCapturing()) {
            if (RecordAnswer.getInstance().getPartialAnswerForQuestionId(mQuestion.id).size() > 0)
                ((SurveyActivity) getActivity()).submitAnswerPartial(isLastPage, RecordAnswer.getInstance().getPartialAnswerForQuestionId(mQuestion.id));

        }
    }

    /**
     * Displays a toast message
     *
     * @param iMessage message to be shown
     */
    void showToast(String iMessage) {
        Toast.makeText(getActivity(), iMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Extracts the extra params passed to this fragment
     */
    private void getExtraArguments() {
        Bundle aBundle = getArguments();
        if (aBundle != null) {
            mQuestion = aBundle.getParcelable(MultiPageFragment.EXTRAS_QUESTION);
            mCurrentPosition = aBundle.getInt(MultiPageFragment.EXTRAS_POSITION);
            isLastPage = (mCurrentPosition == (SurveyCC.getInstance().getSurveyQuestions().size() - 1));
        }
    }

    /**
     * Method to handle conditional display text for current question being displayed
     */
    private void setQuestionTitle() {
        if (mQuestion != null) {
            String aTitle = ConditionalTextFilter.filterText(mQuestion);
            mTVTitle.setText(aTitle);
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            setQuestionTitle();
        }
    }

}
