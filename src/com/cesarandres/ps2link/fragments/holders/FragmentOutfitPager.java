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
import android.widget.Toast;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.fragments.FragmentMembersList;
import com.cesarandres.ps2link.fragments.FragmentMembersOnline;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentOutfitPager extends BaseFragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String outfitId;
    private static final int ONLINE = 0;
    private static final int MEMBERS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);

	View root = inflater.inflate(R.layout.fragment_outfit_pager, container, false);

	mViewPager = (ViewPager) root.findViewById(R.id.outfitPager);
	mViewPager.setAdapter(mSectionsPagerAdapter);

	Bundle extras = getActivity().getIntent().getExtras();
	if (extras == null) {
	    extras = getArguments();
	}

	String activityMode = "";
	if (extras != null) {
	    activityMode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY);
	}

	outfitId = extras.getString("PARAM_0");

	return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	getActivity().findViewById(R.id.buttonFragmentUpdate).setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		Fragment fragment;
		try {
		    switch (mViewPager.getCurrentItem()) {
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
		    Toast.makeText(getActivity(), "There was a problem trying to refresh. Please try again.", Toast.LENGTH_SHORT).show();
		}
	    }
	});

	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
	    @Override
	    public void onPageSelected(int arg0) {
		switch (arg0) {
		case ONLINE:
		    getActivity().findViewById(R.id.toggleButtonShowOffline).setVisibility(View.GONE);
		    break;
		case MEMBERS:
		    getActivity().findViewById(R.id.toggleButtonShowOffline).setVisibility(View.VISIBLE);
		    break;
		default:
		    getActivity().findViewById(R.id.toggleButtonShowOffline).setVisibility(View.VISIBLE);
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
	    return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    switch (position) {
	    case ONLINE:
		return "ONLINE";
	    case MEMBERS:
		return "MEMBERS";
	    default:
		return "ONLINE";
	    }
	}

	public Fragment getFragment(int key) {
	    return mMap.get(key);
	}
    }
}