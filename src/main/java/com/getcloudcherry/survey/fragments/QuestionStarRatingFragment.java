package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.model.SurveyQuestions;


/**
 * Fragment to display and handle Star rating type question
 */
public class QuestionStarRatingFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
    private RatingBar mRating;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout;
    private boolean isLastPage;
    private int mCurrentPosition;
    private String mAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtraArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_star_rating_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mRating = (RatingBar) view.findViewById(R.id.ratingBar);
        initializeViewsWithConfig();
        mTVTitle.setText(mQuestion.text);
        mRating.setOnRatingBarChangeListener(this);
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
            if (TextUtils.isEmpty(mAnswer)) {
                showToast(getString(R.string.validate_answer));
                return false;
            } else {
                submitPartial();
            }
        } else {
            submitPartial();
        }
        return true;
    }

    /**
     * Contains logic to call the Partial response API to submit partial response
     */
    void submitPartial() {
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

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        Log.i("Star", (int) v + "");
        mAnswer = String.valueOf((int) v);
        RecordAnswer.getInstance().recordAnswer(mQuestion, Integer.parseInt(mAnswer));
    }

}
