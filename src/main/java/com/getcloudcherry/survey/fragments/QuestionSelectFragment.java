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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.customviews.FlowLayout;
import com.getcloudcherry.survey.customviews.GRadioGroup;
import com.getcloudcherry.survey.filter.ConditionalFlowFilter;
import com.getcloudcherry.survey.filter.ConditionalTextFilter;
import com.getcloudcherry.survey.helper.Constants;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.helper.Utils;
import com.getcloudcherry.survey.model.CustomTextStyle;
import com.getcloudcherry.survey.model.SurveyQuestions;


/**
 * Fragment to display and handle Single Select type question
 */
public class QuestionSelectFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    //    private RadioGroup mRadioGroup;
    private GRadioGroup mRadioGroup;
    private FlowLayout mFlowLayout;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout;
    private String mAnswer;
    private boolean isLastPage;
    private int mCurrentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtraArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_nps_question, container, false);
        return inflater.inflate(R.layout.fragment_multiselect_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Header
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mFlowLayout = (FlowLayout) view.findViewById(R.id.linearMultiselect);
//        mRadioGroup = (RadioGroup) view.findViewById(R.id.rbgRating);
//        mFlowLayout.addView(mRadioGroup);
        initializeViewsWithConfig();
//        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup = new GRadioGroup();
        createSingleSelect();
        mTVTitle.setText(mQuestion.text);
    }

    /**
     * Dynamically generate custom Single select view
     */
    private void createSingleSelect() {
//        RadioGroup.LayoutParams aParams = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
//        aParams.gravity = Gravity.TOP;
        FlowLayout.LayoutParams aParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        aParams.setMargins((int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5));
        for (int i = 0; i < mQuestion.multiSelect.size(); i++) {
            RadioButton aRadio = new RadioButton(getActivity());
            aRadio.setText(mQuestion.multiSelect.get(i));
            aRadio.setId(i + 1);
            aRadio.setPadding((int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5));
            aRadio.setBackgroundResource(SurveyCC.getInstance().getCustomTextStyle() == CustomTextStyle.STYLE_CIRCLE ? android.R.color.transparent : R.drawable.multi_select_selector_rectangle);
            aRadio.setButtonDrawable(android.R.color.transparent);
            aRadio.setCompoundDrawablesWithIntrinsicBounds(0, SurveyCC.getInstance().getCustomTextStyle() == CustomTextStyle.STYLE_CIRCLE ? R.drawable.multi_select_selector_circle : 0, 0, 0);
            aRadio.setGravity(Gravity.CENTER);
            aRadio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            aRadio.setLayoutParams(aParams);
            aRadio.setOnCheckedChangeListener(this);
            mFlowLayout.addView(aRadio);
            mRadioGroup.addRadioButton(aRadio);
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
            mAnswer = ((RadioButton) radioGroup.findViewById(i)).getText().toString();
            Constants.logInfo("Answer", mAnswer);
            RecordAnswer.getInstance().recordAnswer(mQuestion, mAnswer);
            ConditionalFlowFilter.filterQuestion(mQuestion);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i("Radio", isChecked + "");
        if (isChecked) {
            mAnswer = buttonView.getText().toString();
            Constants.logInfo("Answer", mAnswer);
            RecordAnswer.getInstance().recordAnswer(mQuestion, mAnswer);
            ConditionalFlowFilter.filterQuestion(mQuestion);
        }
    }
}
