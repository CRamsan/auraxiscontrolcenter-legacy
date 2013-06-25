package com.cesarandres.ps2link;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationPS2Link extends Application {

	public static RequestQueue volley;

	@Override
	public void onCreate() {
		super.onCreate();

		if (volley == null) {
			ApplicationPS2Link.volley = Volley.newRequestQueue(this);
		}
	}
}
