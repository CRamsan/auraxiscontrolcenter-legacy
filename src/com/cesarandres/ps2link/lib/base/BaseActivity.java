package com.cesarandres.ps2link.lib.base;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	protected void navigateUp() {
		finish();
	}
}
