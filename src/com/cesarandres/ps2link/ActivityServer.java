package com.cesarandres.ps2link;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.World;
import com.google.gson.Gson;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityServer extends FragmentActivity {

	private World server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		this.server = new Gson().fromJson(
				getIntent().getExtras().getString("server"), World.class);
	}
}
