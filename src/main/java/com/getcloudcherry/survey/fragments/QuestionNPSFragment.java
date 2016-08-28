package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
 * Fragment to display and handle NPS type question
 */
public class QuestionNPSFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout, mLegendLayout;
    private int mMin;
    private int mMax;
    private String mAnswer;
    private static final String TAG_TYPE = "NPS";
    private boolean isNPS;
    private boolean isLastPage;
    private int mCurrentPosition;
    private boolean isListenerSet = false;
    private int positionChecked = 0;

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
        mLegendLayout = (LinearLayout) view.findViewById(R.id.linearLegends);
        initializeViewsWithConfig();
        createScale();
        mTVTitle.setText(mQuestion.text);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * Dynamically generate custom scale view based on the min and max attributes
     */
    private void createScale() {
        mLegendLayout.setVisibility(isNPS ? View.VISIBLE : View.GONE);

        if (mQuestion.multiSelect != null && !TextUtils.isEmpty(mQuestion.multiSelect.get(0))) {
            String[] aData = mQuestion.multiSelect.get(0).split("-");
            String[] aLowData = aData[0].split(";");
            String[] aMaxData = aData[1].split(";");
            if (aLowData.length > 1) {
                //Has custom legend
                String aMinLegend = aLowData[1];
                String aMaxLegend = aMaxData[1];
                mMin = Integer.parseInt(aLowData[0]);
                mMax = Integer.parseInt(aMaxData[0]);
                if (isNPS)
                    createMinMaxLegend(aMinLegend, aMaxLegend);
            } else {
                mMin = Integer.parseInt(aData[0]);
                mMax = Integer.parseInt(aData[1]);
                if (isNPS)
                    createDefaultLegend();
            }
            for (int i = 0; i <= mMax; i++) {
                RadioButton aRadio = new RadioButton(getActivity());
                aRadio.setText(String.valueOf(mMin + i));
                aRadio.setId(mMin + i);
                aRadio.setPadding(0, (int) Utils.convertDpToPixel(5), 0, (int) Utils.convertDpToPixel(5));
                if (isNPS) {
                    setRequiredColor(aRadio, i);
                } else {
                    aRadio.setBackgroundResource(R.drawable.rate0_check);
                }
                aRadio.setButtonDrawable(android.R.color.transparent);
                aRadio.setGravity(Gravity.CENTER);
                aRadio.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                aRadio.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.nps_text_state));
                mRadioGroup.addView(aRadio);
            }
        }
    }

    /**
     * Generates default legend
     */
    void createDefaultLegend() {
        for (int i = 0; i < 3; i++) {
            LinearLayout aLinearLegend = new LinearLayout(getActivity());
            aLinearLegend.setGravity(Gravity.CENTER);
            aLinearLegend.setOrientation(LinearLayout.VERTICAL);
            View aView = new View(getActivity());
            aView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.convertDpToPixel(2)));
            TextView aLegendText = new TextView(getActivity());
            aLegendText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            aLegendText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            if (i == 0) {
                aLegendText.setText(R.string.not_at_all);
                aLinearLegend.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 70f));
                aView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_legend_1));
                aLinearLegend.addView(aView);
                aLinearLegend.addView(aLegendText);
                mLegendLayout.addView(aLinearLegend);
            } else if (i == 1) {
                aLegendText.setText(R.string.maybe);
                aLinearLegend.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 20f));
                aView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_legend_2));
                aLinearLegend.addView(aView);
                aLinearLegend.addView(aLegendText);
                mLegendLayout.addView(aLinearLegend);
            } else if (i == 2) {
                aLegendText.setText(R.string.yes_for_sure);
                aLinearLegend.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 20f));
                aView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_legend_3));
                aLinearLegend.addView(aView);
                aLinearLegend.addView(aLegendText);
                mLegendLayout.addView(aLinearLegend);
            }
        }
    }

    /**
     * Generates custom legend for min and max values
     *
     * @param iMinLegend
     * @param iMaxLegend
     */
    void createMinMaxLegend(String iMinLegend, String iMaxLegend) {
        for (int i = 0; i < 2; i++) {
            TextView aLegendText = new TextView(getActivity());
            aLegendText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            aLegendText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            if (i == 0) {
                aLegendText.setText(iMinLegend);
                aLegendText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                mLegendLayout.addView(aLegendText);

            } else if (i == 1) {
                aLegendText.setText(iMaxLegend);
                aLegendText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                mLegendLayout.addView(aLegendText);
            }
        }
    }

    /**
     * Sets radio button color based on the position
     *
     * @param iRadio    Radio Button
     * @param iPosition Position
     */
    void setRequiredColor(RadioButton iRadio, int iPosition) {
        if (iPosition == 0)
            iRadio.setBackgroundResource(R.drawable.rate0_check);
        else if (iPosition == 1)
            iRadio.setBackgroundResource(R.drawable.rate1_check);
        else if (iPosition == 2)
            iRadio.setBackgroundResource(R.drawable.rate2_check);
        else if (iPosition == 3)
            iRadio.setBackgroundResource(R.drawable.rate3_check);
        else if (iPosition == 4)
            iRadio.setBackgroundResource(R.drawable.rate4_check);
        else if (iPosition == 5)
            iRadio.setBackgroundResource(R.drawable.rate5_check);
        else if (iPosition == 6)
            iRadio.setBackgroundResource(R.drawable.rate6_check);
        else if (iPosition == 7)
            iRadio.setBackgroundResource(R.drawable.rate7_check);
        else if (iPosition == 8)
            iRadio.setBackgroundResource(R.drawable.rate8_check);
        else if (iPosition == 9)
            iRadio.setBackgroundResource(R.drawable.rate9_check);
        else if (iPosition == 10)
            iRadio.setBackgroundResource(R.drawable.rate10_check);
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
//        ConditionalFlowFilter.filterQuestion(mQuestion);
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
            if (mQuestion.questionTags != null)
                isNPS = mQuestion.questionTags.contains(TAG_TYPE);
            isLastPage = (mCurrentPosition == (SurveyCC.getInstance().getSurveyQuestions().size() - 1));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.i("Radio", i + "");
        Constants.logInfo("Button Pressed", radioGroup.findViewById(i).isPressed() + "");
        if (radioGroup.findViewById(i).isPressed()) {
            mAnswer = String.valueOf(i);
            if (isNPS) {
                RecordAnswer.getInstance().recordAnswer(mQuestion, Integer.parseInt(mAnswer));
            } else {
                RecordAnswer.getInstance().recordAnswer(mQuestion, mAnswer);
            }
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
