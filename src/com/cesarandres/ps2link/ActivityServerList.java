package com.cesarandres.ps2link;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityServerList extends FragmentActivity {
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_list);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//ActionBar actionBar = getActionBar();
			//actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
}
