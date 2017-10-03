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

import com.getcloudcherry.survey.builder.SurveyConfigBuilder;
import com.getcloudcherry.survey.customviews.CustomViewPager;
import com.getcloudcherry.survey.filter.QuestionFilterHelper;
import com.getcloudcherry.survey.fragments.MultiPageFragment;
import com.getcloudcherry.survey.fragments.ThanksFragment;
import com.getcloudcherry.survey.fragments.WelcomeFragment;
import com.getcloudcherry.survey.helper.Constants;
import com.getcloudcherry.survey.helper.RecordAnalytics;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.httpclient.SurveyClient;
import com.getcloudcherry.survey.interfaces.FragmentCallBack;
import com.getcloudcherry.survey.model.Answer;
import com.getcloudcherry.survey.model.CustomTextStyle;
import com.getcloudcherry.survey.model.LoginToken;
import com.getcloudcherry.survey.model.SurveyQuestions;
import com.getcloudcherry.survey.model.SurveyResponse;
import com.getcloudcherry.survey.model.SurveyToken;
import com.getcloudcherry.survey.storage.CCPreferences;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends AppCompatActivity implements FragmentCallBack {
    public ArrayList<SurveyQuestions> mSurveyQuestions = new ArrayList<>();
    private SurveyResponse mSurveyResponse;
    private ImageView mIVLogo;
    public Ion ION;
    private Toolbar mToolbar;
    private TextView mTvToolbarTitle;
    private ProgressBar mProgressLoading;
    private static final int RETRY_SUBMIT = 0;
    private static final int RETRY_LOGIN = 1;
    private static final int RETRY_CREATE_TOKEN = 2;
    private static final int RETRY_QUESTIONS = 3;
    private AlertDialog aAlertDialog;
    private boolean mIsLastPage;
    private int mCurrentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        mProgressLoading = (ProgressBar) findViewById(R.id.progressBar);
        ION = Ion.getDefault(getApplicationContext());
//        initToolbar();
        if (SurveyCC.getInstance().shouldCreateToken()) {
            createTokenAndGetQuestions();
        } else {
            getQuestions();
        }

        SurveyCC.getInstance().setOnFragmentDataListener(this);
    }

    /**
     * Replaces fragment in the frame container
     *
     * @param iFragment Fragment
     */
    public void replaceFragment(Fragment iFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, iFragment).commitNow();
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
        aAlertDialog = new AlertDialog.Builder(this).setPositiveButton(R.string.yes, null).setNegativeButton(R.string.no, null).create();
        aAlertDialog.setMessage(iMessage);
        aAlertDialog.setCancelable(false);
        aAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                aAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                aAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                aAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SurveyCC.getInstance().setIsUserTryingToExit(true);
                        if (RecordAnswer.getInstance().mAnswers.size() > 0) {
                            aAlertDialog.cancel();
                            submitAnswers();
                        } else {
                            finishSurvey();
                        }
                    }
                });
                aAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aAlertDialog.cancel();
                    }
                });
            }
        });
        aAlertDialog.show();
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
                .customTextStyle(CustomTextStyle.STYLE_RECTANGLE)
                .build();
        SurveyCC.getInstance().setConfigFromWeb(aHeaderConfig);
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

    /**
     * API call to submit answers
     */
    public void submitAnswers() {
        final ProgressDialog aProgressDialog = new ProgressDialog(SurveyActivity.this);
        aProgressDialog.setTitle(getString(R.string.alert_title_submit_survey));
        aProgressDialog.setMessage(getString(R.string.please_wait));
        aProgressDialog.setCancelable(false);
        aProgressDialog.show();
        Call<ResponseBody> aCall = SurveyClient.get().postAnswerAll(SurveyCC.getInstance().getSurveyToken(), RecordAnswer.getInstance().getAnswers());
        aCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    aProgressDialog.dismiss();
                    finishSurvey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    aProgressDialog.dismiss();
                    if (aAlertDialog != null) {
                        aAlertDialog.cancel();
                    }
                    showAlertRetryCallback(RETRY_SUBMIT, getString(R.string.alert_message_survey_submit_failed), SurveyActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * API call to submit partial answers
     *
     * @param isLastPage boolean to check if view pager is on last page
     * @param iAnswer    array list of answer object as per API documentation
     */
    public void submitAnswerPartial(final boolean isLastPage, ArrayList<Answer> iAnswer) {
        Call<ResponseBody> aCall = SurveyClient.get().postAnswerPartial(SurveyCC.getInstance().getPartialResponseId(), isLastPage, iAnswer);
        aCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    if (isLastPage)
//                        finishSurvey();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    if (aAlertDialog != null) {
                        aAlertDialog.cancel();
                    }
//                    if (isLastPage)
//                        showAlertRetryCallback(RETRY_SUBMIT, getString(R.string.alert_message_survey_submit_failed), SurveyActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Moves viewpager page to the next item or finishes the survey
     */
    public void moveOrSubmit() {
        if (getCurrentFragment() instanceof MultiPageFragment) {
            nextPage();
        } else {
            finishSurvey();
        }
    }

    /**
     * Method to reset all saved data and show thank you screen
     */
    void finishSurvey() {
        if (aAlertDialog != null) {
            aAlertDialog.cancel();
        }
        SurveyCC.getInstance().setRecordedAnswerCount(RecordAnswer.getInstance().mAnswers.size());
        RecordAnswer.getInstance().reset();
        RecordAnalytics.getInstance().reset();
        replaceFragment(new ThanksFragment());
    }

    /**
     * Handles display of next page or submitting answer based on certain criteria
     */
    void nextPage() {
        boolean aIsLastPage = mIsLastPage;
        if (!aIsLastPage) {
            getMultiPageViewPager().setCurrentItem(mCurrentPage + 1);
        } else {
//            if (!SurveyCC.getInstance().isPartialCapturing()) {
            submitAnswers();
//            }
        }
    }

    /**
     * Shows alert dialog to retry if any of the APIs fail to respond
     *
     * @param iWhich   which API to call integer constant
     * @param iMessage message to be shown
     * @param iContext Activity context
     */
    public void showAlertRetryCallback(final int iWhich, String iMessage, Context iContext) {
        AlertDialog dialog = new AlertDialog.Builder(iContext).create();
        dialog.setTitle(iContext.getString(R.string.alert_title_alert));
        dialog.setMessage(iMessage);
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, iContext.getString(R.string.alert_retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (iWhich) {
                    case RETRY_SUBMIT:
                        submitAnswers();
                        break;
                    case RETRY_LOGIN:
                        createTokenAndGetQuestions();
                        break;
                    case RETRY_CREATE_TOKEN:
                        createNewSurveyToken();
                        break;
                    case RETRY_QUESTIONS:
                        getQuestions();
                        break;
                }
            }
        });
        dialog.show();
    }

    void showProgressBar() {
        if (mProgressLoading != null)
            mProgressLoading.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        if (mProgressLoading != null)
            mProgressLoading.setVisibility(View.GONE);
    }


    /**
     * API call to authenticate user and creates new survey token to fetch question list
     */
    void createTokenAndGetQuestions() {
        showProgressBar();
        Call<LoginToken> aCall = SurveyClient.get().login(Constants.GRANT_TYPE, SurveyCC.getInstance().getUserName(), SurveyCC.getInstance().getPassword());
        aCall.enqueue(new Callback<LoginToken>() {
            @Override
            public void onResponse(Call<LoginToken> call, Response<LoginToken> response) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        CCPreferences.getInstance(SurveyCC.getInstance().getContext()).setUserDetail(response.body());
                        createNewSurveyToken();
                    }
                } catch (Exception e) {
                    Constants.logWarn("createTokenAndGetQuestions", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginToken> call, Throwable t) {
                try {
                    hideProgressBar();
                    showAlertRetryCallback(RETRY_LOGIN, getString(R.string.toast_failed_general), SurveyActivity.this);
                    Constants.logWarn("createTokenAndGetQuestions onFailure", t.getMessage());
                } catch (Exception e) {
                    Constants.logWarn("createTokenAndGetQuestions onFailure", e.getMessage());
                }
            }
        });

    }

    /**
     * API call to create new Survey Token
     */
    void createNewSurveyToken() {
        showProgressBar();
        Call<SurveyToken> aCall = SurveyClient.get().createSurveyToken(SurveyCC.getInstance().getTokenConfig());
        aCall.enqueue(new Callback<SurveyToken>() {
            @Override
            public void onResponse(Call<SurveyToken> call, Response<SurveyToken> response) {
                try {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        SurveyCC.getInstance().setSurveyToken(response.body().id);
                        getQuestions();
                    }
                } catch (Exception e) {
                    Constants.logWarn("createNewSurveyToken onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SurveyToken> call, Throwable t) {
                try {
                    hideProgressBar();
                    showAlertRetryCallback(RETRY_CREATE_TOKEN, getString(R.string.toast_failed_general), SurveyActivity.this);
                    Constants.logWarn("createNewSurveyToken onFailure", t.getMessage());
                } catch (Exception e) {
                    Constants.logWarn("createNewSurveyToken onFailure", e.getMessage());
                }
            }
        });
    }

    /**
     * API call to fetch survey questions from Cloud-Cherry server
     */
    void getQuestions() {
        showProgressBar();
        Call<SurveyResponse> aCall = SurveyClient.get().getQuestions(SurveyCC.getInstance().getSurveyToken(), "1234");
        aCall.enqueue(new Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                try {
                    hideProgressBar();
                    if (response != null && response.body() != null)
                        handleSurveyResponse(response.body());
                } catch (Exception e) {
                    Constants.logInfo("getQuestions onResponse", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                try {
                    Constants.logInfo("getQuestions onFailure", t.getMessage());
                    hideProgressBar();
                    showAlertRetryCallback(RETRY_QUESTIONS, getString(R.string.toast_failed_general), SurveyActivity.this);
                } catch (Exception e) {
                    Constants.logInfo("getQuestions onFailure", e.getMessage());
                }
            }
        });
    }

    /**
     * Handles survey response from API call
     *
     * @param iResponse SurveyResponse object from server
     */
    void handleSurveyResponse(SurveyResponse iResponse) {
        SurveyCC.getInstance().setSurveyResponse(iResponse);
        filterQuestions(iResponse);
        setConfigFromResponse(iResponse);
        if (SurveyCC.getInstance().isShowWelcomeMessage()) {
            replaceFragment(new WelcomeFragment());
        } else {
            replaceFragment(new MultiPageFragment());
        }
    }

    /**
     * Shows toast message
     *
     * @param iMessage message to show
     */
    void showToastMessage(int iMessage) {
        Toast.makeText(SurveyActivity.this, iMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuestionDisplayed(SurveyQuestions iQuestion, int iPosition, boolean isLastPage) {
        mCurrentPage = iPosition;
        mIsLastPage = isLastPage;
        Constants.logInfo("onQuestionDisplayed", "Question : " + iQuestion.text + " : position : " + iPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SurveyCC.getInstance().removeFragmentDataListener(this);
    }
}
