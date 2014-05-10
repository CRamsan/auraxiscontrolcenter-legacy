package com.cesarandres.ps2link.fragments.holders;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.fragments.FragmentFriendList;
import com.cesarandres.ps2link.fragments.FragmentKillList;
import com.cesarandres.ps2link.fragments.FragmentProfile;
import com.cesarandres.ps2link.fragments.FragmentStatList;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfilePager extends BaseFragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String profileId;
    private String profileName;
    private static final int PROFILE = 0;
    private static final int FRIENDS = 1;
    private static final int STATS = 2;
    private static final int KILLBOARD = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
	if (savedInstanceState != null) {
	    this.profileName = savedInstanceState.getString("ProfileName");
	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	View root = inflater.inflate(R.layout.fragment_profile_pager, container, false);

	mViewPager = (ViewPager) root.findViewById(R.id.profilePager);
	mViewPager.setAdapter(mSectionsPagerAdapter);

	Bundle extras = getActivity().getIntent().getExtras();
	if (extras == null) {
	    extras = getArguments();
	}
	profileId = extras.getString("PARAM_0");

	return root;
    }

    @Override
    public void onResume() {
	super.onResume();
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_PROFILE);
	this.fragmentUpdate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	this.fragmentUpdate.setVisibility(View.VISIBLE);
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		switch (mViewPager.getCurrentItem()) {
		case PROFILE:
		    ((FragmentProfile) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem())).downloadProfiles(profileId);
		    break;
		case FRIENDS:
		    ((FragmentFriendList) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem())).downloadFriendsList(profileId);
		    break;
		case STATS:
		    ((FragmentStatList) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem())).downloadStatList(profileId);
		    break;
		case KILLBOARD:
		    ((FragmentKillList) mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem())).downloadKillList(profileId);
		    break;
		default:
		    break;
		}
	    }
	});

	if (!"".equals(profileName) && profileName != null) {
	    this.fragmentTitle.setText(profileName);
	}

	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
	    @Override
	    public void onPageSelected(int arg0) {
		switch (arg0) {
		case PROFILE:
		    fragmentStar.setVisibility(View.VISIBLE);
		    fragmentAppend.setVisibility(View.VISIBLE);
		    break;
		case FRIENDS:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    break;
		case STATS:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    break;
		case KILLBOARD:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    break;
		default:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
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
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	String profileName = String.valueOf(fragmentTitle.getText());
	if (!"".equals(profileName)) {
	    savedInstanceState.putString("ProfileName", profileName);
	}
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

	private HashMap<Integer, Fragment> mMap;

	public SectionsPagerAdapter(FragmentManager fm) {
	    super(fm);
	    this.mMap = new HashMap<Integer, Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
	    Fragment fragment = null;
	    switch (position) {
	    case PROFILE:
		fragment = new FragmentProfile();
		break;
	    case FRIENDS:
		fragment = new FragmentFriendList();
		break;
	    case KILLBOARD:
		fragment = new FragmentKillList();
		break;
	    case STATS:
		fragment = new FragmentStatList();
		break;
	    default:
		break;
	    }
	    Bundle args = new Bundle();
	    args.putString("PARAM_0", profileId);
	    fragment.setArguments(args);
	    mMap.put(position, fragment);
	    return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    super.destroyItem(container, position, object);
	    mMap.remove(position);
	}

	@Override
	public int getCount() {
	    return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    switch (position) {
	    case PROFILE:
		return "OVERVIEW";
	    case FRIENDS:
		return "FRIENDS";
	    case KILLBOARD:
		return "KILLBOARD";
	    case STATS:
		return "STATS";
	    default:
		return "OVERVIEW";
	    }
	}

	public Fragment getFragment(int key) {
	    return mMap.get(key);
	}
    }

}