package com.cesarandres.ps2link;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.soe.volley.BitmapLruCache;

public class ApplicationPS2Link extends Application {

	public static RequestQueue volley;
	public static ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();

		if (volley == null) {
			ApplicationPS2Link.volley = Volley.newRequestQueue(this);
		}
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(ApplicationPS2Link.volley,
					new BitmapLruCache());
		}
	}
}
