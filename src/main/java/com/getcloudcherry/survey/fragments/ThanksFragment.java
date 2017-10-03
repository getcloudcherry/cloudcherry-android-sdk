package com.getcloudcherry.survey.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.interfaces.ExitCallBack;


public class ThanksFragment extends Fragment implements View.OnClickListener {

    private Button mBContinue;
    private TextView mTVThanks, mTVFooterText;
    private ImageView mImageFooterLogo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thanks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageFooterLogo = (ImageView) view.findViewById(R.id.ivFooterLogo);
        mTVThanks = (TextView) view.findViewById(R.id.tvThanks);
        mTVFooterText = (TextView) view.findViewById(R.id.tvFooter);
        mBContinue = (Button) view.findViewById(R.id.bThanks);
        mBContinue.setOnClickListener(this);
        setThanksMessage(SurveyCC.getInstance().getThanksMessage());
    }

    void setThanksMessage(String iMessage) {
        mTVThanks.setText(iMessage);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bThanks) {
            if (SurveyCC.getInstance().isUserTryingToExit()) {
                if (SurveyCC.getInstance().getRecordedAnswerCount() > 0) {
                    SurveyCC.getInstance().sendExitState(ExitCallBack.SurveyState.PARTIALLY_COMPLETED);
                } else {
                    SurveyCC.getInstance().sendExitState(ExitCallBack.SurveyState.VIEWED);
                }
            } else {
                SurveyCC.getInstance().sendExitState(ExitCallBack.SurveyState.COMPLETED);
            }
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}
