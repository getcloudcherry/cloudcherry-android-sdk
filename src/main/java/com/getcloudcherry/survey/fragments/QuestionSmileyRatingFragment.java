package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;
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
public class QuestionSmileyRatingFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout;
    private String mAnswer;
    private boolean isLastPage;
    private int mCurrentPosition;
    private boolean mToUseDefaultSmiley;
    private ArrayList<Integer> mSmileyRatingSelector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtraArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nps_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSmileyRatingSelector = SurveyCC.getInstance().getSmileyRatingSelector();
        mToUseDefaultSmiley = (mSmileyRatingSelector == null || mSmileyRatingSelector.size() != 5);
        //Header
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rbgRating);
        initializeViewsWithConfig();
        mRadioGroup.setOnCheckedChangeListener(this);
        createSmileyRating();
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
            if (mToUseDefaultSmiley)
                aRadio.setText(getEmijoByUnicode(getEmojiUnicode(i + 1)));
            aRadio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            aRadio.setId(i + 1);
            aRadio.setBackgroundResource(mToUseDefaultSmiley ? R.drawable.multi_select_selector_circle : getDrawableResource(i));
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

    public String getEmijoByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
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
     * @return true or false
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.i("Radio", i + "");
        if (radioGroup.findViewById(i).isPressed()) {
            mAnswer = String.valueOf(i);
            RecordAnswer.getInstance().recordAnswer(mQuestion, Integer.parseInt(mAnswer));
            ConditionalFlowFilter.filterQuestion(mQuestion);
        }
    }

    int getEmojiUnicode(int iPosition) {
        switch (iPosition) {
            case 1:
                return 0x1F620;
            case 2:
                return 0x1F61E;
            case 3:
                return 0x1F610;
            case 4:
                return 0x1F60A;
            case 5:
                return 0x1F60D;
        }
        return 0;
    }

    int getDrawableResource(int iPosition) {
        if (mSmileyRatingSelector != null && iPosition <= mSmileyRatingSelector.size()) {
            return mSmileyRatingSelector.get(iPosition);
        }
        return 0;
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
