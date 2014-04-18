package com.cesarandres.ps2link.fragments.holders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.R.id;
import com.cesarandres.ps2link.R.layout;
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
		
		View root = inflater.inflate(R.layout.activity_outfit, container, false);

		mViewPager = (ViewPager) root.findViewById(R.id.outfitPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		Bundle extras = getActivity().getIntent().getExtras();
		if(extras == null){
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
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		getActivity().findViewById(R.id.buttonFragmentUpdate).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String tag;
				Fragment fragment;
				try{
					switch (mViewPager.getCurrentItem()) {
					case ONLINE:
						tag = ApplicationPS2Link.makeFragmentName(R.id.outfitPager, ONLINE);
						fragment = ((FragmentMembersOnline) getActivity().getSupportFragmentManager().findFragmentByTag(tag));
						((FragmentMembersOnline) fragment).downloadOutfitMembers();
						break;
					case MEMBERS:
						tag = ApplicationPS2Link.makeFragmentName(R.id.outfitPager, MEMBERS);
						fragment = ((FragmentMembersList) getActivity().getSupportFragmentManager().findFragmentByTag(tag));
						((FragmentMembersList) fragment).downloadOutfitMembers();
						break;
					default:
						break;
					}
				}catch(Exception e){
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

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
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
			return fragment;
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
	}
}