package com.cesarandres.ps2link;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.soe.volley.BitmapLruCache;

public class ApplicationPS2Link extends Application {

	public static RequestQueue volley;
	public static ImageLoader mImageLoader;
	private static boolean full = false;

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

	public static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	public static String generateMD5(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(input.getBytes());
		byte[] mdbytes = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static boolean isFull() {
		return full;
	}

	public static void setFull(boolean full) {
		ApplicationPS2Link.full = full;
	}

}
