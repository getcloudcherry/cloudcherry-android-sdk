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
import com.getcloudcherry.survey.helper.Constants;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.helper.Utils;
import com.getcloudcherry.survey.model.SurveyQuestions;


/**
 * Fragment to display and handle Single Select type question
 */
public class QuestionSelectFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
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
        return inflater.inflate(R.layout.fragment_nps_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Header
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rbgRating);
        initializeViewsWithConfig();
        mRadioGroup.setOnCheckedChangeListener(this);
        createSingleSelect();
        mTVTitle.setText(mQuestion.text);
    }

    /**
     * Dynamically generate custom Single select view
     */
    private void createSingleSelect() {
        RadioGroup.LayoutParams aParams = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        aParams.gravity = Gravity.TOP;
        for (int i = 0; i < mQuestion.multiSelect.size(); i++) {
            RadioButton aRadio = new RadioButton(getActivity());
            aRadio.setText(mQuestion.multiSelect.get(i));
            aRadio.setId(i + 1);
            aRadio.setPadding((int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5));
            aRadio.setBackgroundResource(android.R.color.transparent);
            aRadio.setButtonDrawable(android.R.color.transparent);
            aRadio.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.multi_select_selector, 0, 0);
            aRadio.setGravity(Gravity.CENTER);
            aRadio.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
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

}
