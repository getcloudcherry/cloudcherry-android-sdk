package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.filter.ConditionalFlowFilter;
import com.getcloudcherry.survey.filter.ConditionalTextFilter;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.helper.Utils;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;


/**
 * Fragment to display and handle Star rating type question
 */
public class QuestionStarRatingFragment extends Fragment implements /*RatingBar.OnRatingBarChangeListener,*/ RadioGroup.OnCheckedChangeListener {
    //    private RatingBar mRating;
    private RadioGroup mRadioGroup;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout;
    private boolean isLastPage;
    private int mCurrentPosition;
    private String mAnswer;
    private boolean isRestored = false;
    private boolean mToUseDefaultStar;
    private ArrayList<Integer> mStarRatingSelector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isRestored = true;
        }
        getExtraArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nps_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStarRatingSelector = SurveyCC.getInstance().getStarRatingSelector();
        mToUseDefaultStar = (mStarRatingSelector == null || mStarRatingSelector.size() != 5);
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rbgRating);
//        mRating = (RatingBar) view.findViewById(R.id.ratingBar);
        initializeViewsWithConfig();
        mRadioGroup.setOnCheckedChangeListener(this);
        createSmileyRating();
//        mRating.setOnRatingBarChangeListener(this);
        setSelection(mRadioGroup, mRadioGroup.getCheckedRadioButtonId());
        mTVTitle.setText(mQuestion.text);
    }

    /**
     * Dynamically generate custom smiley rating view
     */
    private void createSmileyRating() {
        RadioGroup.LayoutParams aParams = new RadioGroup.LayoutParams((int) Utils.convertDpToPixel(48), (int) Utils.convertDpToPixel(48));
        aParams.setMargins((int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(10), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(10));
        for (int i = 0; i < 5; i++) {
            RadioButton aRadio = new RadioButton(getActivity());
            aRadio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            aRadio.setId(i + 1);
            aRadio.setBackgroundResource(getDrawableResource(i));
            aRadio.setButtonDrawable(android.R.color.transparent);
            aRadio.setGravity(Gravity.CENTER);
            aRadio.setLayoutParams(aParams);
            mRadioGroup.addView(aRadio);
        }
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

//    @Override
//    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//        Log.i("Star", (int) v + "");
//        if (!isRestored) {
//            mAnswer = String.valueOf((int) v);
//            RecordAnswer.getInstance().recordAnswer(mQuestion, Integer.parseInt(mAnswer));
//            ConditionalFlowFilter.filterQuestion(mQuestion);
//        } else {
//            isRestored = false;
//        }
//    }

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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.i("Star", i + "");
        if (radioGroup.findViewById(i).isPressed()) {
            mAnswer = String.valueOf(i);
            RecordAnswer.getInstance().recordAnswer(mQuestion, Integer.parseInt(mAnswer));
            ConditionalFlowFilter.filterQuestion(mQuestion);
            setSelection(radioGroup, i);
        }
    }

    private void setSelection(RadioGroup radioGroup, int iSelectedRadioId) {
        if (iSelectedRadioId > -1) {
            for (@IdRes int i = 1; i <= iSelectedRadioId; i++) {
                radioGroup.findViewById(i).setSelected(true);
            }
            if (iSelectedRadioId < 5) {
                for (int i = iSelectedRadioId + 1; i <= 5; i++) {
                    radioGroup.findViewById(i).setSelected(false);
                }
            }
        }
    }

    int getDrawableResource(int iPosition) {
        if (!mToUseDefaultStar && mStarRatingSelector != null && iPosition <= mStarRatingSelector.size()) {
            return mStarRatingSelector.get(iPosition);
        }
        return R.drawable.star_selector;
    }
}
