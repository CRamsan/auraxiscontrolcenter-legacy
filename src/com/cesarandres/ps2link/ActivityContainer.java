package com.cesarandres.ps2link;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ActivityContainer extends FragmentActivity {

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
	}
}
