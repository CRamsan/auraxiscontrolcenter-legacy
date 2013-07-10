package com.cesarandres.ps2link;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityAbout extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// ActionBar actionBar = getActionBar();
			// actionBar.setDisplayHomeAsUpEnabled(true);
		}
		TextView forums = (TextView) findViewById(R.id.textViewAboutForumLink);
		forums.setText(R.string.forum_link);
		TextView donations = (TextView) findViewById(R.id.textViewAboutDonationsUrl);
		donations.setText(R.string.donations_link);
		Linkify.addLinks(forums, Linkify.WEB_URLS);
		Linkify.addLinks(donations, Linkify.WEB_URLS);
		Button titleBack = (Button) findViewById(R.id.buttonFragmentTitle);
		titleBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				navigateUp();
			}
		});
		((Button) findViewById(R.id.buttonFragmentTitle))
		.setText(getString(R.string.text_menu_about));
	}

	private void navigateUp() {
		finish();
	}
}
