package com.getcloudcherry.survey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getcloudcherry.survey.filter.QuestionFilterHelper;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.builder.SurveyConfigBuilder;
import com.getcloudcherry.survey.customviews.CustomViewPager;
import com.getcloudcherry.survey.fragments.WelcomeFragment;
import com.getcloudcherry.survey.fragments.MultiPageFragment;
import com.getcloudcherry.survey.fragments.ThanksFragment;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.httpclient.SurveyClient;
import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends AppCompatActivity {
    public ArrayList<SurveyQuestions> mSurveyQuestions = new ArrayList<>();
    private SurveyResponse mSurveyResponse;
    private ImageView mIVLogo;
    public Ion ION;
    private Toolbar mToolbar;
    private TextView mTvToolbarTitle;
    private ProgressBar mProgressLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mProgressLoading = (ProgressBar) findViewById(R.id.progressBar);
        ION = Ion.getDefault(getApplicationContext());
//        initToolbar();
        getQuestions();
    }

    /**
     * Replaces fragment in the frame container
     *
     * @param iFragment Fragment
     */
    public void replaceFragment(Fragment iFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, iFragment).commit();
    }

    /**
     * Gets the instance of the current fragment in frame container
     *
     * @return Fragment
     */
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    /**
     * Gets the ViewPager instance
     *
     * @return ViewPager
     */
    public CustomViewPager getMultiPageViewPager() {
        return ((MultiPageFragment) getCurrentFragment()).mViewPager;
    }

//    /**
//     * Initializes toolbar attributes
//     */
//    private void initToolbar() {
//        mToolbar = (Toolbar) findViewById(R.id.appToolbar);
//        if (mToolbar != null) {
//            mIVLogo = (ImageView) mToolbar.findViewById(R.id.ivLogo);
//            mTvToolbarTitle = (TextView) mToolbar.findViewById(R.id.tvToolbarTitle);
//            mToolbar.setBackgroundColor(ContextCompat.getColor(this, SurveyCC.getInstance().getHeaderActionBarBackgroundColor()));
//            mTvToolbarTitle.setText(R.string.survey_title);
//            mTvToolbarTitle.setTextColor(ContextCompat.getColor(this, SurveyCC.getInstance().getHeaderActionBarFontColor()));
//            mTvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, SurveyCC.getInstance().getHeaderActionBarFontSize());
//        }
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        if (!TextUtils.isEmpty(SurveyCC.getInstance().getHeaderActionBarLogo())) {
//            try {
//                if (mToolbar != null) {
//                    mToolbar.setContentInsetsRelative(0, 0);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showAlertYesNo(getString(R.string.alert_exit));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertYesNo(getString(R.string.alert_exit));
    }

    /**
     * Alert dialog to alert when user presses the back button
     *
     * @param iMessage Message to display
     */
    public void showAlertYesNo(String iMessage) {
        final AlertDialog aAlert = new AlertDialog.Builder(this).setPositiveButton(R.string.yes, null).setNegativeButton(R.string.no, null).create();
        aAlert.setMessage(iMessage);
        aAlert.setCancelable(false);
        aAlert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                aAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                aAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                aAlert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitAnswers(aAlert);
                    }
                });
                aAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aAlert.cancel();
                    }
                });
            }
        });
        aAlert.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    /**
     * API call to fetch survey questions from Cloud-Cherry server
     */
    void getQuestions() {
        mProgressLoading.setVisibility(View.VISIBLE);
        Call<SurveyResponse> aCall = SurveyClient.get().getQuestions(SurveyCC.mSurveyToken, "1234");
        aCall.enqueue(new Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                try {
                    mProgressLoading.setVisibility(View.GONE);
                    if (response != null)
                        if (response.body() != null) {
                            mSurveyResponse = response.body();
                            filterQuestions(mSurveyResponse);
                            setConfigFromResponse(mSurveyResponse);
                            if (SurveyCC.getInstance().isShowWelcomeMessage())
                                replaceFragment(new WelcomeFragment());
                            else
                                replaceFragment(new MultiPageFragment());
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                try {
                    mProgressLoading.setVisibility(View.GONE);
                    Toast.makeText(SurveyActivity.this, R.string.toast_failed_general, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets config parameters based on API response
     *
     * @param iSurveyResponse response data from server
     */
    private void setConfigFromResponse(SurveyResponse iSurveyResponse) {
        SurveyConfigBuilder aHeaderConfig = new SurveyConfigBuilder.Builder()
                .headerLogo(iSurveyResponse.logoURL)
                .headerBackgroundColor(iSurveyResponse.colorCode1)
                .headerFontColor(iSurveyResponse.colorCode3)
                .footerBackgroundColor(iSurveyResponse.colorCode2)
                .welcomeMessage(iSurveyResponse.welcomeText)
                .thankYouMessage(iSurveyResponse.thankyouText)
                .showWelcomeMessage(!iSurveyResponse.skipWelcome)
                .showThankYouMessage(true)
                .build();

        SurveyCC.getInstance().setConfigFromWeb(aHeaderConfig);
//        setAppLogo();
    }

    /**
     * Fetches logo from logo url and display
     */
    private void setAppLogo() {
//        mIVLogo.setVisibility(View.VISIBLE);
//        ION.build(SurveyCC.getInstance().getContext()).load(SurveyCC.HEADER_LOGO).withBitmap().smartSize(false).error(0).placeholder(0).intoImageView(mIVLogo);
//        mToolbar.setNavigationIcon(APIHelper.isColorDark(SurveyCC.getInstance().getHeaderActionBarBackgroundColor()) ? R.drawable.back_white : R.drawable.back_black);
//        mToolbar.setBackgroundColor(ContextCompat.getColor(this, SurveyCC.getInstance().getHeaderActionBarBackgroundColor()));
//        mTvToolbarTitle.setTextColor(ContextCompat.getColor(this, SurveyCC.getInstance().getHeaderActionBarFontColor()));
//        mTvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, SurveyCC.getInstance().getHeaderActionBarFontSize());
    }

    void filterQuestions(SurveyResponse iResponse) {
        mSurveyQuestions.clear();
        mSurveyQuestions.addAll(QuestionFilterHelper.getFilteredQuestions(iResponse));
    }

    public void submitAnswers(final AlertDialog aAlert) {
        if (RecordAnswer.getInstance().mAnswers.size() > 0) {
            final ProgressDialog aProgressDialog = new ProgressDialog(SurveyActivity.this);
            aProgressDialog.setTitle("Submitting survey");
            aProgressDialog.setMessage("Please wait...");
            aProgressDialog.setCancelable(false);
            aProgressDialog.show();
            Call<ResponseBody> aCall = SurveyClient.get().postAnswerAll(SurveyCC.mSurveyToken, RecordAnswer.getInstance().getAnswers());
            aCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        aProgressDialog.dismiss();
                        finishSurvey(aAlert);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        aProgressDialog.dismiss();
                        if (aAlert != null) {
                            aAlert.cancel();
                        }
                        showAlertRetryCallback(getString(R.string.alert_message_survey_submit_failed), SurveyActivity.this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                submitAnswers(null);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            finishSurvey(aAlert);
        }
    }

    void finishSurvey(AlertDialog aAlert) {
        if (aAlert != null) {
            aAlert.cancel();
        }
        RecordAnswer.getInstance().reset();
        replaceFragment(new ThanksFragment());
    }

    public void showAlertRetryCallback(String iMessage, Context iContext, DialogInterface.OnClickListener iYesListener) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        dialog.setTitle(iContext.getString(R.string.alert_title_alert));
        dialog.setMessage(iMessage);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(R.string.alert_retry), iYesListener);

        dialog.show();
    }
}
