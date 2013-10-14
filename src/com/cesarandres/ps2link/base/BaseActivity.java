package com.cesarandres.ps2link.base;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.WallPaperMode;
import com.cesarandres.ps2link.R;

public class BaseActivity extends FragmentActivity {

	private BitmapWorkerTask currentTask;

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences settings = getSharedPreferences("PREFERENCES", 0);
		String preferedWallpaper = settings.getString("preferedWallpaper", WallPaperMode.PS2.toString());
		ApplicationPS2Link.setWallpaperMode(WallPaperMode.valueOf(preferedWallpaper));

		if (ApplicationPS2Link.getWallpaperMode() != WallPaperMode.PS2) {

			if (ApplicationPS2Link.getBackground() == null) {
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
				}
			} else {
				ImageView background = (ImageView) findViewById(R.id.imageViewBackground);
				background.setImageBitmap(ApplicationPS2Link.getBackground());
				background.setScaleType(ScaleType.CENTER_CROP);
			}
		}
	}

	protected void navigateUp() {
		finish();
	}
}
