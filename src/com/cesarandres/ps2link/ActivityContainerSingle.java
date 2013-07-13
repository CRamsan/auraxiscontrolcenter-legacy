package com.cesarandres.ps2link;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.base.BaseActivity;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityContainerSingle extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		String activityMode = "";
		if (extras != null) {
			activityMode = extras.getString(ApplicationPS2Link.ACTIVITY_MODE_KEY);
		}

		switch (ActivityMode.valueOf(activityMode)) {
		case ACTIVITY_ADD_OUTFIT:
			setContentView(R.layout.activity_add_outfit);
			break;
		case ACTIVITY_ADD_PROFILE:
			setContentView(R.layout.activity_add_profile);
			break;
		case ACTIVITY_MEMBER_LIST:
			setContentView(R.layout.activity_member_list);
			break;
		case ACTIVITY_OUTFIT_LIST:
			setContentView(R.layout.activity_outfit_list);
			break;
		case ACTIVITY_PROFILE:
			setContentView(R.layout.activity_profile);
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
}
