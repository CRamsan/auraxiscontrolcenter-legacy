package com.cesarandres.ps2link;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.base.BaseFragment.OnFragmentEventListener;

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

		if (findViewById(R.id.second_pane) != null) {
			mTwoPane = true;
		}

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			// ActionBar actionBar = getActionBar();
			// actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void OnFragmentEvent(int id) {
	}
}
