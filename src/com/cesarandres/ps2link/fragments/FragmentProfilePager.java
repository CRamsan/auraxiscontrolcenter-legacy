package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ActivityProfile;
import com.cesarandres.ps2link.ActivityProfile.SectionsPagerAdapter;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.response.Character_list_response;
import com.cesarandres.ps2link.soe.content.response.Server_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfilePager extends BaseFragment {

	private ObjectDataSource data;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private String profileId;
	private static final int PROFILE = 0;
	private static final int FRIENDS = 1;
	private static final int STATS = 2;
	private static final int KILLBOARD = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.activity_profile, container, false);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager = (ViewPager) root.findViewById(R.id.profilePager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		root.findViewById(R.id.buttonFragmentUpdate).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (mViewPager.getCurrentItem()) {
				case PROFILE:
					((FragmentProfile) getActivity().getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, PROFILE)))
							.downloadProfiles(profileId);
					break;
				case FRIENDS:
					((FragmentFriendList) getActivity().getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, FRIENDS)))
							.downloadFriendsList(profileId);
					break;
				case STATS:
					((FragmentStatList) getActivity().getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, STATS)))
							.downloadStatList(profileId);
					break;
				case KILLBOARD:

					((FragmentKillList) getActivity().getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, KILLBOARD)))
							.downloadKillList(profileId);
					break;
				default:
					break;
				}
			}
		});
		
		
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
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
			return fragment;
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
	}
	
}
