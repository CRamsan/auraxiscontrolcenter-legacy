package com.cesarandres.ps2link.base;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.WallPaperMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.module.BitmapWorkerTask;

/**
 * @author Cesar Ramirez
 * 
 *         This fragment handles setting the background for all activities.
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

    private BitmapWorkerTask currentTask;

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
	super.onStart();

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
	super.onResume();

	// Read the current wallpaper from the settings
	SharedPreferences settings = getSharedPreferences("PREFERENCES", 0);
	String preferedWallpaper = settings.getString("preferedWallpaper", WallPaperMode.PS2.toString());
	// TODO Check if the wallpaper mode needs to be set everytime an
	// activity is resumed
	ApplicationPS2Link.setWallpaperMode(WallPaperMode.valueOf(preferedWallpaper));

	if (ApplicationPS2Link.getWallpaperMode() != WallPaperMode.PS2) {
	    if (ApplicationPS2Link.getBackground() == null) {
		// If the wallpaper has been set to some faction specific image
		// but the image has not been loaded, we need to load it first
		BitmapWorkerTask task = new BitmapWorkerTask((ImageView) findViewById(R.id.imageViewBackground), this);
		if (currentTask != null) {
		    currentTask.cancel(true);
		}
		currentTask = task;
		switch (WallPaperMode.valueOf(preferedWallpaper)) {
		case NC:
		    task.execute("nc_wallpaper.jpg");
		    break;
		case TR:
		    task.execute("tr_wallpaper.jpg");
		    break;
		case VS:
		    task.execute("vs_wallpaper.jpg");
		    break;
		default:
		    break;
		}
	    } else {
		// If the image has already been loaded, just apply it.
		ImageView background = (ImageView) findViewById(R.id.imageViewBackground);
		background.setImageBitmap(ApplicationPS2Link.getBackground());
		background.setScaleType(ScaleType.CENTER_CROP);
	    }
	}
    }
}
