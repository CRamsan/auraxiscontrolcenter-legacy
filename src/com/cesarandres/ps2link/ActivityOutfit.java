package com.cesarandres.ps2link;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.base.BaseActivity;
import com.cesarandres.ps2link.base.BaseFragment.FragmentCallbacks;
import com.cesarandres.ps2link.fragments.FragmentFriendList;
import com.cesarandres.ps2link.fragments.FragmentKillList;
import com.cesarandres.ps2link.fragments.FragmentMembersList;
import com.cesarandres.ps2link.fragments.FragmentMembersOnline;
import com.cesarandres.ps2link.fragments.FragmentProfile;
import com.cesarandres.ps2link.fragments.FragmentStatList;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityOutfit extends BaseActivity implements FragmentCallbacks {

	private ObjectDataSource data;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private String outfitId;
	private static final int ONLINE = 0;
	private static final int MEMBERS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_outfit);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.outfitPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		findViewById(R.id.buttonFragmentUpdate).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				switch (mViewPager.getCurrentItem()) {
				case ONLINE:
					((FragmentMembersOnline) getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, ONLINE)))
							.downloadOutfitMembers();
					break;
				case MEMBERS:
					((FragmentMembersList) getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, MEMBERS)))
							.downloadOutfitMembers();
					break;
				default:
					break;
				}
			}
		});

		Bundle extras = getIntent().getExtras();
		String activityMode = "";
		if (extras != null) {
			activityMode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY);
		}
		outfitId = extras.getString("PARAM_0");

		setData(new ObjectDataSource(this));
		data.open();

		Button titleBack = (Button) findViewById(R.id.buttonFragmentTitle);
		titleBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				navigateUp();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		data.close();
	}

	public ObjectDataSource getData() {
		return data;
	}

	public void setData(ObjectDataSource data) {
		this.data = data;
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

	@Override
	public void onItemSelected(String id, String args[]) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}