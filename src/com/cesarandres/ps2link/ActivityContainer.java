package com.cesarandres.ps2link;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("isfirstrun", false);
			editor.commit();

			DialogFragment newFragment = new WellcomeDialogFragment();
			newFragment.show(getSupportFragmentManager(), "Wellcome");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public static class WellcomeDialogFragment extends DialogFragment {

		public static WellcomeDialogFragment newInstance() {
			WellcomeDialogFragment d = new WellcomeDialogFragment();
			return d;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.content_new_paid_version).setPositiveButton(R.string.text_sure, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String url = "https://play.google.com/store/apps/details?id=com.cesarandres.ps2link.key";
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			}).setNegativeButton(R.string.text_not_right_now, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}
