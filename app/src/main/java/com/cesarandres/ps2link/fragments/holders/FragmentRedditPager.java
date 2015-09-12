package com.cesarandres.ps2link.fragments.holders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.fragments.FragmentReddit;

import java.util.HashMap;

/**
 * This fragment holds a view pager for all the profile related fragments
 */
public class FragmentRedditPager extends BaseFragment {

    public static final String PS2_PC = "Planetside";
    public static final String PS2_PS4 = "PS4Planetside2";
    private static final int PC = 0;
    private static final int PS4 = 1;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Button goToReddit;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reddit_pager, container, false);
        return view;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_REDDIT);

        this.fragmentUpdate.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = (LayoutParams) goToReddit.getLayoutParams();
        if (params != null) {
            params.gravity = Gravity.CENTER_VERTICAL;
            goToReddit.setLayoutParams(params);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTitle);
        if (titleLayout.findViewById(R.id.buttonFragmentGoToReddit) != null) {
            goToReddit = (Button) titleLayout.findViewById(R.id.buttonFragmentGoToReddit);
        } else {
            goToReddit = (Button) View.inflate(getActivity(), R.layout.layout_go_to_reddit, null);
        }

        goToReddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intentUri;
                switch (mViewPager.getCurrentItem()) {
                    case PC:
                        intentUri = FragmentReddit.REDDIT_URL + PS2_PC;
                        break;
                    case PS4:
                        intentUri = FragmentReddit.REDDIT_URL + PS2_PS4;
                        break;
                    default:
                        intentUri = FragmentReddit.REDDIT_URL + PS2_PC;
                        break;
                }
                Intent openRedditIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentUri));
                startActivity(openRedditIntent);
            }
        });

        if (titleLayout.findViewById(R.id.buttonFragmentGoToReddit) == null) {
            titleLayout.addView(goToReddit);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) getActivity().findViewById(R.id.redditPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null) {
            extras = getArguments();
        }

        this.fragmentUpdate.setVisibility(View.VISIBLE);
        this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment selectedFragment = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
                if (selectedFragment == null) {
                    return;
                }
                switch (mViewPager.getCurrentItem()) {
                    case PC:
                    case PS4:
                        ((FragmentReddit) selectedFragment).updatePosts();
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    public void onStop() {
        super.onStop();
        LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTitle);
        if (titleLayout.findViewById(R.id.buttonFragmentGoToReddit) != null) {
            titleLayout.removeView(goToReddit);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * This pager will hold all the fragments that are displayed
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private HashMap<Integer, Fragment> mMap;

        /**
         * @param fm Fragment manager that will hold all the fragments within
         *           the pager
         */
        @SuppressLint("UseSparseArrays")
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mMap = new HashMap<Integer, Fragment>();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
         */
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            fragment = new FragmentReddit();
            Bundle args = new Bundle();
            switch (position) {
                case PC:
                    args.putString("PARAM_0", PS2_PC);
                    break;
                case PS4:
                    args.putString("PARAM_0", PS2_PS4);
                    break;
                default:
                    break;
            }
            fragment.setArguments(args);
            mMap.put(position, fragment);
            return fragment;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * android.support.v4.app.FragmentStatePagerAdapter#destroyItem(android
         * .view.ViewGroup, int, java.lang.Object)
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mMap.remove(position);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        @Override
        public int getCount() {
            return 2;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case PC:
                    return "r/" + PS2_PC;
                case PS4:
                    return "r/" + PS2_PS4;
                default:
                    return PS2_PC;
            }
        }

        /**
         * @param key integer that identifies the fragment
         * @return the fragment that is associated with the key
         */
        public Fragment getFragment(int key) {
            return mMap.get(key);
        }
    }
}