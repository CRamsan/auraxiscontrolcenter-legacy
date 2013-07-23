package com.cesarandres.ps2link.base;

import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;

import com.cesarandres.ps2link.ApplicationPS2Link;

public class BaseActivity extends FragmentActivity {

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
	}

	protected void navigateUp() {
		finish();
	}
}
