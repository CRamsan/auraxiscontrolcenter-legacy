package com.cesarandres.ps2link;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.base.BaseActivity;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityContainerSingle extends BaseActivity {

	private String activityMode;
	private ObjectDataSource data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String activityMode = "";
		if (extras != null) {
			activityMode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY);
		}

		setActivityMode(activityMode);

		switch (ActivityMode.valueOf(getActivityMode())) {
		case ACTIVITY_ADD_OUTFIT:
			setContentView(R.layout.activity_add_outfit);
			break;
		case ACTIVITY_MAP:
			setContentView(R.layout.activity_map);
			break;
		case ACTIVITY_ADD_PROFILE:
			setContentView(R.layout.activity_add_profile);
			break;
		case ACTIVITY_MEMBER_LIST:
			setContentView(R.layout.activity_member_list);
			setData(new ObjectDataSource(this));
			data.open();
			break;
		case ACTIVITY_OUTFIT_LIST:
			setContentView(R.layout.activity_outfit_list);
			break;
		case ACTIVITY_PROFILE:
			setContentView(R.layout.activity_profile);
			setData(new ObjectDataSource(this));
			data.open();
			break;
		case ACTIVITY_PROFILE_LIST:
			setContentView(R.layout.activity_profile_list);
			break;
		case ACTIVITY_SERVER:
			setContentView(R.layout.activity_server);
			break;
		case ACTIVITY_SERVER_LIST:
			setContentView(R.layout.activity_server_list);
			break;
		case ACTIVITY_TWITTER:
			setContentView(R.layout.activity_twitter);
			setData(new ObjectDataSource(this));
			data.open();
			break;
		default:
			break;
		}

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
}
