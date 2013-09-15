package com.cesarandres.ps2link;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.cesarandres.ps2link.fragments.FragmentProfile;
import com.cesarandres.ps2link.fragments.FragmentStatList;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityProfile extends BaseActivity implements FragmentCallbacks {

	private ObjectDataSource data;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	private static final int PROFILE = 0;
	private static final int FRIENDS = 1;
	private static final int STATS = 2;
	private static final int KILLBOARD = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile_full);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.profilePager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		findViewById(R.id.buttonFragmentUpdate).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String profileId = getIntent().getExtras().getString("profileId");
				switch (mViewPager.getCurrentItem()) {
				case PROFILE:
					((FragmentProfile) getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, PROFILE)))
							.downloadProfiles(profileId);
					break;
				case FRIENDS:
					((FragmentFriendList) getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, FRIENDS)))
							.downloadFriendsList(profileId);
					break;
				case STATS:
					((FragmentStatList) getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, STATS)))
							.downloadStatList(profileId);
					break;
				case KILLBOARD:

					((FragmentKillList) getSupportFragmentManager().findFragmentByTag(ApplicationPS2Link.makeFragmentName(R.id.profilePager, KILLBOARD)))
							.downloadKillList(profileId);
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

		setData(new ObjectDataSource(this));
		data.open();

		Button titleBack = (Button) findViewById(R.id.buttonFragmentTitle);
		titleBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				navigateUp();
			}
		});

		SharedPreferences settings = getSharedPreferences("PREFERENCES", 0);
		boolean isFirstRun = settings.getBoolean("isfirstrun", true);
		if (isFirstRun) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("isfirstrun", false);
			editor.commit();

			DialogFragment newFragment = new WellcomeDialogFragment();
			newFragment.show(getSupportFragmentManager(), "Wellcome");
		}

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
			args.putInt("", 0);
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

	@Override
	public void onItemSelected(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public static class WellcomeDialogFragment extends DialogFragment {

		public static WellcomeDialogFragment newInstance() {
			WellcomeDialogFragment d = new WellcomeDialogFragment();
			return d;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.content_new_paid_version).setPositiveButton(R.string.text_sure, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String url = "https://play.google.com/store/apps/details?id=com.cesarandres.ps2link.key";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			}).setNegativeButton(R.string.text_not_right_now, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}