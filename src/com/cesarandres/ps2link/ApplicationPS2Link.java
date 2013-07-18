package com.cesarandres.ps2link;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.soe.volley.BitmapLruCache;

public class ApplicationPS2Link extends Application {

	public static RequestQueue volley;
	public static ImageLoader mImageLoader;

	public static final String ACTIVITY_MODE_KEY = "activity_mode";

	public enum ActivityMode {
		ACTIVITY_ADD_OUTFIT,
		ACTIVITY_ADD_PROFILE,
		ACTIVITY_MEMBER_LIST,
		ACTIVITY_OUTFIT_LIST,
		ACTIVITY_PROFILE,
		ACTIVITY_MAP,
		ACTIVITY_PROFILE_LIST,
		ACTIVITY_SERVER,
		ACTIVITY_SERVER_LIST,
		ACTIVITY_TWITTER
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (volley == null) {
			ApplicationPS2Link.volley = Volley.newRequestQueue(this);
		}
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(ApplicationPS2Link.volley, new BitmapLruCache());
		}
	}
}
