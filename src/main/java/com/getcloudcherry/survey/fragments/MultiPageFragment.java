package com.getcloudcherry.survey.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getcloudcherry.survey.R;
import com.getcloudcherry.survey.SurveyActivity;
import com.getcloudcherry.survey.helper.RecordAnswer;
import com.getcloudcherry.survey.customviews.CustomViewPager;
import com.getcloudcherry.survey.helper.FontCache;
import com.getcloudcherry.survey.helper.SurveyCC;
import com.getcloudcherry.survey.model.SurveyQuestions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment to handle multiple page creation based on the number of survey questions
 */
public class MultiPageFragment extends Fragment implements View.OnClickListener {
    public CustomViewPager mViewPager;
    public static final String EXTRAS_QUESTION = "question";
    private Button mBNext, mBPrevious;
    private TextView mTVPage;
    private LinearLayout mFooterLayout;
    private boolean mIsLastPage;
    private boolean mIsFirstPage;
    private int mCurrentPosition = 0;
    private PagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multipage, container, false);
    }

    @Override
    public void onViewCreated(View iView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(iView, savedInstanceState);
        RecordAnswer.getInstance().startedAt(System.currentTimeMillis());
        mViewPager = (CustomViewPager) iView.findViewById(R.id.viewPager);
        //Footer
        mFooterLayout = (LinearLayout) iView.findViewById(R.id.linearFooter);
        mTVPage = (TextView) iView.findViewById(R.id.tvPage);
        mBNext = (Button) iView.findViewById(R.id.bNext);
        mBPrevious = (Button) iView.findViewById(R.id.bPrevious);
        mViewPager.setPagingEnabled(false);
        mAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), ((SurveyActivity) getActivity()).mSurveyQuestions);
        mViewPager.setAdapter(mAdapter);
        mBNext.setOnClickListener(this);
        mBPrevious.setOnClickListener(this);
        setFooterConfig();
        setContentConfig();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrentPosition = position;
                if (mCurrentPosition == 0) {
                    mIsFirstPage = true;
                } else {
                    mIsFirstPage = false;
                }
                if (mViewPager.getAdapter().getCount() - 1 == mCurrentPosition) {
                    mIsLastPage = true;
                } else {
                    mIsLastPage = false;
                }
                handleFooterButtons();
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        handleFooterButtons();
    }

    /**
     * Sets Content config
     */
    void setContentConfig() {
        //Content
        mViewPager.setBackgroundColor(Color.parseColor(SurveyCC.getInstance().getContentBackgroundColor()));
    }

    /**
     * Handles footer UI based on the page selection
     */
    void handleFooterButtons() {
        if (mIsLastPage) {
            mBNext.setText(R.string.button_submit);
        } else {
            mBNext.setText(R.string.next);
        }
        if (mIsFirstPage) {
            mBPrevious.setVisibility(View.INVISIBLE);
        } else {
            mBPrevious.setVisibility(View.VISIBLE);
        }
        String aPage = String.valueOf(mCurrentPosition + 1) + "/" + (((SurveyActivity) getActivity()).mSurveyQuestions.size());
        mTVPage.setText(aPage);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bNext) {
            if (!mIsLastPage) {
                if (validate())
                    mViewPager.setCurrentItem(mCurrentPosition + 1);
            } else {
                if (validate())
                    ((SurveyActivity) getActivity()).submitAnswers(null);
            }
        } else if (i == R.id.bPrevious) {
            if (!mIsFirstPage) {
                mViewPager.setCurrentItem(mCurrentPosition - 1);
            }
        }
    }

    /**
     * Sets Footer config
     */
    void setFooterConfig() {
        //Footer
        mBPrevious.setTextSize(TypedValue.COMPLEX_UNIT_SP, SurveyCC.getInstance().getFooterFontSize());
        mBPrevious.setTextColor(ContextCompat.getColor(getActivity(), SurveyCC.getInstance().getFooterFontColor()));
        mBPrevious.setBackgroundColor(ContextCompat.getColor(getActivity(), SurveyCC.getInstance().getFooterButtonColor()));
        mBNext.setTextSize(TypedValue.COMPLEX_UNIT_SP, SurveyCC.getInstance().getFooterFontSize());
        mBNext.setTextColor(ContextCompat.getColor(getActivity(), SurveyCC.getInstance().getFooterFontColor()));
        mBNext.setBackgroundColor(ContextCompat.getColor(getActivity(), SurveyCC.getInstance().getFooterButtonColor()));
        if (!TextUtils.isEmpty(SurveyCC.getInstance().getFooterFontPath())) {
            Typeface customFont = FontCache.getTypeface("fonts/" + SurveyCC.getInstance().getFooterFontPath(), getActivity());
            mBPrevious.setTypeface(customFont);
            mBNext.setTypeface(customFont);
        }
        if (!TextUtils.isEmpty(SurveyCC.getInstance().getFooterPageFontPath())) {
            Typeface customFont = FontCache.getTypeface("fonts/" + SurveyCC.getInstance().getFooterPageFontPath(), getActivity());
            mTVPage.setTypeface(customFont);
        }
        mTVPage.setTextSize(TypedValue.COMPLEX_UNIT_SP, SurveyCC.getInstance().getFooterPageFontSize());
        mTVPage.setTextColor(ContextCompat.getColor(getActivity(), SurveyCC.getInstance().getFooterPageFontColor()));
        try {
            mFooterLayout.setBackgroundColor(Color.parseColor(SurveyCC.getInstance().getFooterBackgroundColor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<SurveyQuestions> mQuestions = new ArrayList<>();
        private Map<String, Fragment> mFragments = new HashMap<String, Fragment>();

        public PagerAdapter(FragmentManager fm, ArrayList<SurveyQuestions> mQuestions) {
            super(fm);
            this.mQuestions = mQuestions;
        }

        private String getTitleFromStrings(int iStringId) {
            return getResources().getString(iStringId);
        }

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment aFragment = null;
            Bundle aBundle = new Bundle();
            aBundle.putParcelable(EXTRAS_QUESTION, mQuestions.get(position));
            if (mFragments.get(mQuestions.get(position).id) == null && mQuestions.get(position).displayType.equals("Scale")) {
                aFragment = new QuestionNPSFragment();
                aFragment.setArguments(aBundle);
                mFragments.put(mQuestions.get(position).id, aFragment);
            } else if (mFragments.get(mQuestions.get(position).id) == null && mQuestions.get(position).displayType.equals("MultilineText")) {
                aFragment = new QuestionTextAreaFragment();
                aFragment.setArguments(aBundle);
                mFragments.put(mQuestions.get(position).id, aFragment);
            }
            return mFragments.get(mQuestions.get(position).id);
        }

        @Override
        public int getCount() {
            return mQuestions.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    /** This method calls the respective validateAnswer() method of a fragment
     *
     * @return true or false
     */
    boolean validate() {
        if (mAdapter.getItem(mViewPager.getCurrentItem()) instanceof QuestionNPSFragment) {
            return ((QuestionNPSFragment) mAdapter.getItem(mViewPager.getCurrentItem())).validateAnswer();
        } else if (mAdapter.getItem(mViewPager.getCurrentItem()) instanceof QuestionTextAreaFragment) {
            return ((QuestionTextAreaFragment) mAdapter.getItem(mViewPager.getCurrentItem())).validateAnswer();
        }
        return true;
    }

}
