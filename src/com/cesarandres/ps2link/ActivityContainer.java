package com.cesarandres.ps2link;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.cesarandres.ps2link.module.FragmentBase.OnFragmentEventListener;

public class ActivityContainer extends FragmentActivity implements
		OnFragmentEventListener {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__container, menu);
		return true;
	}

	@Override
	public void OnFragmentEvent(int id) {

		Intent intent;
		switch (id) {
		case R.id.button_characters:
			intent = new Intent();
			intent.setClass(this, ActivityProfileList.class);
			startActivity(intent);
			break;
		case R.id.button_servers:
			intent = new Intent();
			intent.setClass(this, ActivityProfileList.class);
			startActivity(intent);
			break;
		case R.id.button_outfit:
			intent = new Intent();
			intent.setClass(this, ActivityProfileList.class);
			startActivity(intent);
			break;
		case R.id.button_stats:
			intent = new Intent();
			intent.setClass(this, ActivityProfileList.class);
			startActivity(intent);
			break;
		case R.id.button_news:
			intent = new Intent();
			intent.setClass(this, ActivityProfileList.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
