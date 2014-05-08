package com.cesarandres.ps2link;

import android.app.Application;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.soe.volley.BitmapLruCache;

/**
 * @author Cesar Ramirez
 * 
 *         This class provides functions and objects that are used application
 *         wide.
 * 
 */
public class ApplicationPS2Link extends Application {

    public static RequestQueue volley;
    public static ImageLoader mImageLoader;

    private static Bitmap background;
    private static WallPaperMode wallpaper = WallPaperMode.PS2;

    public static final String ACTIVITY_MODE_KEY = "activity_mode";

    /**
     * @author Cesar Ramirez
     * 
     *         This enum holds all the different activity modes, there is one
     *         for each fragment that is used.
     * 
     */
    public static enum ActivityMode {
	ACTIVITY_ADD_OUTFIT, ACTIVITY_ADD_PROFILE, ACTIVITY_MEMBER_LIST, ACTIVITY_OUTFIT_LIST, ACTIVITY_PROFILE, ACTIVITY_MAP, ACTIVITY_PROFILE_LIST, ACTIVITY_SERVER_LIST, ACTIVITY_TWITTER, ACTIVITY_LINK_MENU, ACTIVITY_MAIN_MENU
    }

    /**
     * @author Cesar Ramirez
     * 
     *         This enum holds the reference for all four background types.
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

	if (volley == null) {
	    ApplicationPS2Link.volley = Volley.newRequestQueue(this);
	}
	if (mImageLoader == null) {
	    mImageLoader = new ImageLoader(ApplicationPS2Link.volley, new BitmapLruCache());
	}

    }

    /**
     * @param viewId
     *            This is the unique id of the resource that makes this view
     * @param index
     *            The index of this view within it's view pager
     * @return String tag of the provided view
     */
    /*public static String makeFragmentName(int viewId, int index) {
	return "android:switcher:" + viewId + ":" + index;
    }*/

    /**
     * @return the current Wallpaper mode
     */
    public static WallPaperMode getWallpaperMode() {
	return wallpaper;
    }

    /**
     * @param wallpaper the wallpaper mode that matches the current wallpaper
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
     * @param background bitmap that will be used as background for all activities
     */
    public static void setBackground(Bitmap background) {
	ApplicationPS2Link.background = background;
    }

}
