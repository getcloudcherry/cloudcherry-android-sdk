package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.helper.Constants;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.helper.Utils;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;


/**
 * Fragment to display and handle MultiSelect question type
 */
public class QuestionMultiselectFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout, mLinearMultiSelect;
    private String mAnswer;
    private boolean isLastPage;
    private int mCurrentPosition;
    private ArrayList<String> mSelectedAnswers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtraArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multiselect_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Header
        mQuestionHeaderLayout = (LinearLayout) view.findViewById(R.id.linearHeader);
        mTVTitle = (TextView) view.findViewById(R.id.tvTitle);
        mLinearMultiSelect = (LinearLayout) view.findViewById(R.id.linearMultiselect);
        initializeViewsWithConfig();
        createMultiSelect();
        mTVTitle.setText(mQuestion.text);
    }

    /**
     * Dynamically generate custom multi select view
     */
    private void createMultiSelect() {
        RadioGroup.LayoutParams aParams = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        aParams.gravity = Gravity.TOP;
        if (mQuestion.multiSelect != null) {
            for (int i = 0; i < mQuestion.multiSelect.size(); i++) {
                CheckBox aCheckBox = new CheckBox(getActivity());
                aCheckBox.setText(mQuestion.multiSelect.get(i));
                aCheckBox.setId(i + 1);
                aCheckBox.setPadding((int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5), (int) Utils.convertDpToPixel(5));
                aCheckBox.setBackgroundResource(android.R.color.transparent);
                aCheckBox.setButtonDrawable(android.R.color.transparent);
                aCheckBox.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.multi_select_selector, 0, 0);
                aCheckBox.setGravity(Gravity.CENTER);
                aCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                aCheckBox.setLayoutParams(aParams);
                aCheckBox.setOnCheckedChangeListener(this);
                mLinearMultiSelect.addView(aCheckBox);
            }
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String aAnswer = compoundButton.getText().toString();
        if (mSelectedAnswers.contains(aAnswer)) {
            mSelectedAnswers.remove(aAnswer);
        } else {
            mSelectedAnswers.add(aAnswer);
        }

        join(mSelectedAnswers);
    }

    /**
     * Join the array list of answers to form a comma separated string
     *
     * @param iAnswers array list of multi select answer
     */
    void join(ArrayList<String> iAnswers) {
        mAnswer = "";
        for (int i = 0; i < iAnswers.size(); i++) {
            if (i != (iAnswers.size() - 1))
                mAnswer = mAnswer + iAnswers.get(i) + ",";
            else
                mAnswer = mAnswer + iAnswers.get(i);
        }
        Constants.logInfo("Answers", mAnswer);
        RecordAnswer.getInstance().recordAnswer(mQuestion, mAnswer);
    }

}
