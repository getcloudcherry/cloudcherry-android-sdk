package com.getcloudcherry.survey.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.SurveyAnswers;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;


/**
 * Fragment to display and handle NPS type question
 */
public class QuestionNPSFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private SurveyQuestions mQuestion;
    private TextView mTVTitle;
    private LinearLayout mQuestionHeaderLayout;
    private int mMin;
    private int mMax;
    private String mAnswer;
    private static final String TAG_TYPE = "NPS";
    private boolean isNPS;
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
        createScale();
        mTVTitle.setText(mQuestion.text);
    }

    /**
     * Dynamically generate custom scale view based on the min and max attributes
     */
    private void createScale() {
        if (mQuestion.multiSelect != null && !TextUtils.isEmpty(mQuestion.multiSelect.get(0))) {
            String[] aLimit = mQuestion.multiSelect.get(0).split("-");
            if (aLimit.length > 0) {
                mMin = Integer.parseInt(aLimit[0]);
                mMax = Integer.parseInt(aLimit[1]);
                for (int i = 0; i <= mMax; i++) {
                    RadioButton aRadio = new RadioButton(getActivity());
                    aRadio.setText(String.valueOf(mMin + i));
                    aRadio.setId(mMin + i);
                    aRadio.setPadding(0, (int) convertDpToPixel(5, getActivity()), 0, (int) convertDpToPixel(5, getActivity()));
                    if (isNPS) {
                        setRequiredColor(aRadio, i);
                    } else {
                        aRadio.setBackgroundResource(R.drawable.rate0_check);
                    }
                    aRadio.setButtonDrawable(android.R.color.transparent);
                    aRadio.setGravity(Gravity.CENTER);
                    aRadio.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                    aRadio.setTextColor(ContextCompat.getColorStateList(getActivity(), R.color.nps_text_state));
                    mRadioGroup.addView(aRadio);
                }
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
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
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
        ArrayList<Answer> aAnswers = new ArrayList<>();
        if (isNPS)
            aAnswers.add(new Answer(mQuestion.id, mQuestion.text, !TextUtils.isEmpty(mAnswer) ? Integer.parseInt(mAnswer) : null));
        else
            aAnswers.add(new Answer(mQuestion.id, mQuestion.text, mAnswer));
        if (SurveyCC.getInstance().isPartialCapturing()) {
            ((SurveyActivity) getActivity()).submitAnswerPartial(isLastPage, aAnswers);
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
            isNPS = mQuestion.questionTags.contains(TAG_TYPE);
            isLastPage = (mCurrentPosition == (SurveyCC.getInstance().getSurveyQuestions().size() - 1));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.i("Radio", i + "");
        mAnswer = String.valueOf(i);
        if (isNPS) {
            RecordAnswer.getInstance().recordAnswer(mQuestion, Integer.parseInt(mAnswer));
        } else {
            RecordAnswer.getInstance().recordAnswer(mQuestion, mAnswer);
        }
    }
}
