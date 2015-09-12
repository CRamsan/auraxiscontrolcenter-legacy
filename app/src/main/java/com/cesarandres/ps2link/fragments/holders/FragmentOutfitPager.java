package com.cesarandres.ps2link.fragments.holders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.fragments.FragmentMembersList;
import com.cesarandres.ps2link.fragments.FragmentMembersOnline;
import com.cesarandres.ps2link.fragments.FragmentOutfit;

import java.util.HashMap;

/**
 * This fragment has a view pager that displays the online member next to all
 * the member.
 */
public class FragmentOutfitPager extends BaseFragment {

    private static final int OUTFIT = 0;
    private static final int ONLINE = 1;
    private static final int MEMBERS = 2;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String outfitId;
    private String namespace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outfit_pager, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        mViewPager = (ViewPager) getActivity().findViewById(R.id.outfitPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras == null) {
            extras = getArguments();
        }

        outfitId = extras.getString("PARAM_0");
        this.namespace = extras.getString("PARAM_1");
        DBGCensus.currentNamespace = Namespace.valueOf(namespace);

        this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment;
                try {
                    switch (mViewPager.getCurrentItem()) {
                        case OUTFIT:
                            fragment = ((FragmentOutfit) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem()));
                            ((FragmentOutfit) fragment).downloadOutfit(outfitId);
                            break;
                        case ONLINE:
                            fragment = ((FragmentMembersOnline) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem()));
                            ((FragmentMembersOnline) fragment).downloadOutfitMembers();
                            break;
                        case MEMBERS:
                            fragment = ((FragmentMembersList) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem()));
                            ((FragmentMembersList) fragment).downloadOutfitMembers();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case OUTFIT:
                        fragmentShowOffline.setVisibility(View.GONE);
                        fragmentAppend.setVisibility(View.VISIBLE);
                        fragmentStar.setVisibility(View.VISIBLE);
                        break;
                    case ONLINE:
                        fragmentShowOffline.setVisibility(View.GONE);
                        fragmentAppend.setVisibility(View.GONE);
                        fragmentStar.setVisibility(View.GONE);
                        break;
                    case MEMBERS:
                        fragmentShowOffline.setVisibility(View.VISIBLE);
                        fragmentAppend.setVisibility(View.GONE);
                        fragmentStar.setVisibility(View.GONE);
                        break;
                    default:
                        fragmentShowOffline.setVisibility(View.GONE);
                        fragmentAppend.setVisibility(View.GONE);
                        fragmentStar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        this.fragmentAppend.setVisibility(View.VISIBLE);
        this.fragmentStar.setVisibility(View.VISIBLE);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_MEMBER_LIST);
    }

    /**
     * This pager will hold all the fragments that are displayed
     */
    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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
            switch (position) {
                case OUTFIT:
                    fragment = new FragmentOutfit();
                    break;
                case ONLINE:
                    fragment = new FragmentMembersOnline();
                    break;
                case MEMBERS:
                    fragment = new FragmentMembersList();
                    break;
                default:
                    break;
            }
            Bundle args = new Bundle();
            args.putString("PARAM_0", outfitId);
            if (namespace != null) {
                args.putString("PARAM_1", namespace);
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
            return 3;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case OUTFIT:
                    return getResources().getString(R.string.title_outfit);
                case ONLINE:
                    return getResources().getString(R.string.text_online_caps);
                case MEMBERS:
                    return getResources().getString(R.string.text_members);
                default:
                    return getResources().getString(R.string.text_online_caps);
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