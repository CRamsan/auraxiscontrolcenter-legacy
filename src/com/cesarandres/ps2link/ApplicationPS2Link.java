package com.cesarandres.ps2link;

import android.app.Application;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.soe.volley.BitmapLruCache;

public class ApplicationPS2Link extends Application {

	public static RequestQueue volley;
	public static ImageLoader mImageLoader;
	private static WallPaperMode wallpaper = WallPaperMode.PS2;
	private static Bitmap background;
	
	public static final String ACTIVITY_MODE_KEY = "activity_mode";

	public static enum ActivityMode {
		ACTIVITY_ADD_OUTFIT,
		ACTIVITY_ADD_PROFILE,
		ACTIVITY_MEMBER_LIST,
		ACTIVITY_OUTFIT_LIST,
		ACTIVITY_PROFILE,
		ACTIVITY_MAP,
		ACTIVITY_PROFILE_LIST,
		ACTIVITY_SERVER,
		ACTIVITY_SERVER_LIST,
		ACTIVITY_TWITTER,
		ACTIVITY_WDS
		ACTIVITY_MAIN_MENU
	}

	public static enum WallPaperMode {
		PS2,
		NC,
		TR,
		VS
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

	public static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	public static WallPaperMode getWallpaperMode() {
		return wallpaper;
	}

	public static void setWallpaperMode(WallPaperMode wallpaper) {
		ApplicationPS2Link.wallpaper = wallpaper;
	}

	public static Bitmap getBackground() {
		return background;
	}

	public static void setBackground(Bitmap background) {
		ApplicationPS2Link.background = background;
	}

}
