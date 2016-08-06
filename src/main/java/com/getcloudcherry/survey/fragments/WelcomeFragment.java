package com.getcloudcherry.survey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.helper.SurveyCC;


public class WelcomeFragment extends Fragment implements View.OnClickListener {

    private Button mBYes;
    private TextView mTVStartSurveyWelcome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_launcher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTVStartSurveyWelcome = (TextView) view.findViewById(R.id.tvStartSurvey);
        mBYes = (Button) view.findViewById(R.id.bYes);
        mBYes.setOnClickListener(this);
        setWelcomeMessage(SurveyCC.getInstance().getWelcomeMessage());
    }

    void setWelcomeMessage(String iMessage) {
        mTVStartSurveyWelcome.setText(iMessage);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bYes) {
            ((SurveyActivity) getActivity()).replaceFragment(new MultiPageFragment());
        }
    }
}
