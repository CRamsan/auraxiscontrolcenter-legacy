package com.cesarandres.ps2link;

import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.google.gson.Gson;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityProfile extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
}
