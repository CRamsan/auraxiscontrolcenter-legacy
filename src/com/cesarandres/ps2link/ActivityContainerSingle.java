package com.cesarandres.ps2link;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.base.BaseActivity;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.base.BaseFragment.FragmentCallbacks;
import com.cesarandres.ps2link.fragments.FragmentAddOutfit;
import com.cesarandres.ps2link.fragments.FragmentAddProfile;
import com.cesarandres.ps2link.fragments.FragmentMainMenu;
import com.cesarandres.ps2link.fragments.FragmentMap;
import com.cesarandres.ps2link.fragments.FragmentMemberList;
import com.cesarandres.ps2link.fragments.FragmentOutfitList;
import com.cesarandres.ps2link.fragments.FragmentProfileList;
import com.cesarandres.ps2link.fragments.FragmentServer;
import com.cesarandres.ps2link.fragments.FragmentServerList;
import com.cesarandres.ps2link.fragments.FragmentTwitter;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityContainerSingle extends BaseActivity implements FragmentCallbacks {

	private String activityMode;
	private ObjectDataSource data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String activityMode = "ACTIVITY_MAIN_MENU";
		if (extras != null) {
			activityMode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY);
		}

		setActivityMode(activityMode);

		setContentView(R.layout.activity_single_pane);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		BaseFragment newFragment = null;
		switch (ActivityMode.valueOf(getActivityMode())) {
		case ACTIVITY_ADD_OUTFIT:
			newFragment = new FragmentAddOutfit();
			break;
		case ACTIVITY_MAP:
			newFragment = new FragmentMap();
			break;
		case ACTIVITY_ADD_PROFILE:
			newFragment = new FragmentAddProfile();
			break;
		case ACTIVITY_MEMBER_LIST:
			newFragment = new FragmentMemberList();
			setData(new ObjectDataSource(this));
			data.open();
			break;
		case ACTIVITY_OUTFIT_LIST:
			newFragment = new FragmentOutfitList();
			break;
		case ACTIVITY_PROFILE_LIST:
			newFragment = new FragmentProfileList();
			break;
		case ACTIVITY_SERVER:
			newFragment = new FragmentServer();
			break;
		case ACTIVITY_SERVER_LIST:
			newFragment = new FragmentServerList();
			break;
		case ACTIVITY_TWITTER:
			newFragment = new FragmentTwitter();
			setData(new ObjectDataSource(this));
			data.open();
			break;
		case ACTIVITY_MAIN_MENU:
			newFragment = new FragmentMainMenu();
			break;
		case ACTIVITY_WDS:
			setContentView(R.layout.activity_wds);
			break;
		default:
			break;
		}
		transaction.replace(R.id.activityFrameLayout, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();

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
		switch (ActivityMode.valueOf(getActivityMode())) {
		case ACTIVITY_TWITTER:
		case ACTIVITY_MEMBER_LIST:
		case ACTIVITY_PROFILE:
			data.close();
			break;
		default:
			break;
		}
	}

	public String getActivityMode() {
		return activityMode;
	}

	public void setActivityMode(String activityMode) {
		this.activityMode = activityMode;
	}

	public ObjectDataSource getData() {
		return data;
	}

	public void setData(ObjectDataSource data) {
		this.data = data;
	}

	@Override
	public void onItemSelected(String id) {
		// TODO Auto-generated method stub

	}
}
