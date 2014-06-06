package com.cesarandres.ps2link;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.soe.util.Logger;
import com.cesarandres.ps2link.soe.volley.BitmapLruCache;

/**
 * 
 * 
 * This class provides functions and objects that are used application wide.
 * 
 */
public class ApplicationPS2Link extends Application {

	public static RequestQueue volley;
	public static ImageLoader mImageLoader;

	private static Bitmap background;
	private static WallPaperMode wallpaper = WallPaperMode.PS2;

	public static final String ACTIVITY_MODE_KEY = "activity_mode";
	public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String PROPERTY_APP_VERSION = "appVersion";

	/**
	 * 
	 * 
	 * This enum holds all the different activity modes, there is one for each
	 * main fragment that is used.
	 * 
	 */
	public static enum ActivityMode {
		ACTIVITY_ADD_OUTFIT, ACTIVITY_ADD_PROFILE, ACTIVITY_MEMBER_LIST, ACTIVITY_OUTFIT_LIST, ACTIVITY_PROFILE, ACTIVITY_PROFILE_LIST, ACTIVITY_SERVER_LIST, ACTIVITY_TWITTER, ACTIVITY_LINK_MENU, ACTIVITY_MAIN_MENU, ACTIVITY_ALERTS
	}

	/**
	 * 
	 * 
	 * This enum holds the reference for all four background types.
	 */
	public static enum WallPaperMode {
		PS2, NC, TR, VS
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// Volley and the image loader are singletons and should always be
		// available
		if (volley == null) {
			ApplicationPS2Link.volley = Volley.newRequestQueue(this);
		}
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(ApplicationPS2Link.volley,
					new BitmapLruCache());
		}

	}

	/**
	 * @return the current Wallpaper mode
	 */
	public static WallPaperMode getWallpaperMode() {
		return wallpaper;
	}

	/**
	 * @param wallpaper
	 *            the wallpaper mode that matches the current wallpaper. This
	 *            method should only be called after the wallpaper has been set
	 */
	public static void setWallpaperMode(WallPaperMode wallpaper) {
		ApplicationPS2Link.wallpaper = wallpaper;
	}

	/**
	 * @return the bitmap holding the data for the background for all activities
	 */
	public static Bitmap getBackground() {
		return background;
	}

	/**
	 * @param background
	 *            bitmap that will be used as background for all activities
	 */
	public static void setBackground(Bitmap background) {
		ApplicationPS2Link.background = background;
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	//TODO Enable this
	/*public static boolean checkPlayServices(Activity context) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, context,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Logger.log(Log.INFO, context, "This device is not supported.");
				context.finish();
			}
			return false;
		}
		return true;
	}*/

	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	public static String getRegistrationId(Activity context) {
		final SharedPreferences prefs = ApplicationPS2Link
				.getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Logger.log(Log.INFO, context, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = ApplicationPS2Link.getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Logger.log(Log.INFO, context, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	public static SharedPreferences getGCMPreferences(Activity context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(context.getClass().getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	public static void storeRegistrationId(Activity context, String regId) {
	    final SharedPreferences prefs = ApplicationPS2Link.getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Logger.log(Log.INFO, context, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
}