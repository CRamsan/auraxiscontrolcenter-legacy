package com.cesarandres.ps2link.base;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
	
	protected void navigateUp() {
		finish();
	}
}
