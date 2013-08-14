package com.cesarandres.ps2link.base;

import java.security.NoSuchAlgorithmException;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
		try {
			PackageManager manager = getPackageManager();
			PackageInfo appLibInfo = manager.getPackageInfo("com.cesarandres.ps2link", PackageManager.GET_SIGNATURES);
			PackageInfo appKeyInfo = manager.getPackageInfo("com.cesarandres.ps2link.key", PackageManager.GET_SIGNATURES);

			ApplicationPS2Link.setFull((ApplicationPS2Link.generateMD5(appLibInfo.signatures[0].toCharsString()).equals(ApplicationPS2Link
					.generateMD5(appKeyInfo.signatures[0].toCharsString()))));

		} catch (NameNotFoundException e) {
			ApplicationPS2Link.setFull(false);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			ApplicationPS2Link.setFull(false);
			e.printStackTrace();
		}

		SharedPreferences settings = getSharedPreferences("PREFERENCES", 0);
		String preferedWallpaper = settings.getString("preferedWallpaper", WallPaperMode.PS2.toString());
		ApplicationPS2Link.setWallpaperMode(WallPaperMode.valueOf(preferedWallpaper));
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

	protected void navigateUp() {
		finish();
	}
}
