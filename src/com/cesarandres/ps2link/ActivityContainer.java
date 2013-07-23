package com.cesarandres.ps2link;

import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cesarandres.ps2link.base.BaseActivity;

public class ActivityContainer extends BaseActivity {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	protected boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		if (findViewById(R.id.second_pane) != null) {
			mTwoPane = true;
		}

		SharedPreferences settings = getSharedPreferences("PREFERENCES", 0);
		boolean isFirstRun = settings.getBoolean("isfirstrun", true);
		if (isFirstRun) {
			DialogFragment newFragment = new WellcomeDialogFragment();
			newFragment.show(getSupportFragmentManager(), "Wellcome");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!ApplicationPS2Link.isFull()) {
			final Button buttonFullVersion = (Button) findViewById(R.id.buttonFullVersion);
			buttonFullVersion.setVisibility(View.VISIBLE);
			buttonFullVersion.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String url = "http://www.example.com";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public class WellcomeDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.content_new_paid_version).setPositiveButton(R.string.text_sure, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("isfirstrun", false);
					editor.commit();

					String url = "http://www.example.com";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			}).setNegativeButton(R.string.text_not_right_now, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("isfirstrun", false);
					editor.commit();
				}
			});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}
