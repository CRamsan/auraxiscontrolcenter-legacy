package com.cesarandres.ps2link.fragments.holders;

import java.util.HashMap;

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

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.fragments.FragmentDirectiveList;
import com.cesarandres.ps2link.fragments.FragmentFriendList;
import com.cesarandres.ps2link.fragments.FragmentKillList;
import com.cesarandres.ps2link.fragments.FragmentProfile;
import com.cesarandres.ps2link.fragments.FragmentStatList;
import com.cesarandres.ps2link.fragments.FragmentWeaponList;

/**
 * This fragment holds a view pager for all the profile related fragments
 */
public class FragmentProfilePager extends BaseFragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String profileId;
    private String namespace;
    private String profileName;
    private static final int PROFILE = 0;
    private static final int FRIENDS = 1;
    private static final int STATS = 2;
    private static final int KILLBOARD = 3;
    private static final int WEAPONS = 4;
    private static final int DIRECTIVES = 5;

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
	    this.profileName = savedInstanceState.getString("ProfileName");
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
	return inflater.inflate(R.layout.fragment_profile_pager, container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_PROFILE);
	this.fragmentUpdate.setVisibility(View.VISIBLE);
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
	mViewPager = (ViewPager) getActivity().findViewById(R.id.profilePager);
	mViewPager.setAdapter(mSectionsPagerAdapter);

	Bundle extras = getActivity().getIntent().getExtras();
	if (extras == null) {
	    extras = getArguments();
	}
	profileId = extras.getString("PARAM_0");
	this.namespace = extras.getString("PARAM_1");
	if(this.namespace != null){
		DBGCensus.currentNamespace = Namespace.valueOf(namespace);
	}
	
	this.fragmentUpdate.setVisibility(View.VISIBLE);
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    Fragment selectedFragment = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
	    if(selectedFragment == null){
	    	return;
	    }
		switch (mViewPager.getCurrentItem()) {
		case PROFILE:
		    ((FragmentProfile) selectedFragment).downloadProfiles(profileId);
		    break;
		case FRIENDS:
		    ((FragmentFriendList) selectedFragment).downloadFriendsList(profileId);
		    break;
		case STATS:
		    ((FragmentStatList) selectedFragment).downloadStatList(profileId);
		    break;
		case KILLBOARD:
		    ((FragmentKillList) selectedFragment).downloadKillList(profileId);
		    break;
		case WEAPONS:
		    ((FragmentWeaponList) selectedFragment).downloadWeaponList(profileId);
		    break;
		case DIRECTIVES:
		    ((FragmentDirectiveList) selectedFragment).downloadDirectivesList(profileId);
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
		    fragmentMyWeapons.setVisibility(View.GONE);
		    break;
		case FRIENDS:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    fragmentMyWeapons.setVisibility(View.GONE);
		    break;
		case STATS:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    fragmentMyWeapons.setVisibility(View.GONE);
		    break;
		case KILLBOARD:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    fragmentMyWeapons.setVisibility(View.GONE);
		    break;
		case WEAPONS:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    fragmentMyWeapons.setVisibility(View.VISIBLE);
		    break;
		case DIRECTIVES:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    fragmentMyWeapons.setVisibility(View.GONE);
		    break;
		default:
		    fragmentStar.setVisibility(View.GONE);
		    fragmentAppend.setVisibility(View.GONE);
		    fragmentMyWeapons.setVisibility(View.GONE);
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
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	String profileName = String.valueOf(fragmentTitle.getText());
	if (!"".equals(profileName)) {
	    savedInstanceState.putString("ProfileName", profileName);
	}
    }

    /**
     * This pager will hold all the fragments that are displayed
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

	private HashMap<Integer, Fragment> mMap;

	/**
	 * @param fm
	 *            Fragment manager that will hold all the fragments within
	 *            the pager
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
	    case WEAPONS:
		fragment = new FragmentWeaponList();
		break;
	    case DIRECTIVES:
		fragment = new FragmentDirectiveList();
		break;
	    default:
		break;
	    }
	    Bundle args = new Bundle();
	    args.putString("PARAM_0", profileId);
	    if(namespace != null){
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
	    return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		//TODO Externalize this strings!
	    switch (position) {
	    case PROFILE:
		return "OVERVIEW";
	    case FRIENDS:
		return "FRIENDS";
	    case KILLBOARD:
		return "KILLBOARD";
	    case STATS:
		return "STATS";
	    case WEAPONS:
		return "WEAPONS";
	    case DIRECTIVES:
		return "DIRECTIVES";
	    default:
		return "OVERVIEW";
	    }
	}

	/**
	 * @param key
	 *            integer that identifies the fragment
	 * @return the fragment that is associated with the key
	 */
	public Fragment getFragment(int key) {
	    return mMap.get(key);
	}
    }
}