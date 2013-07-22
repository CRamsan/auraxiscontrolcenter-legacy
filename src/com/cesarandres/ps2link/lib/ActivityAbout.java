package com.cesarandres.ps2link.lib;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cesarandres.ps2link.lib.base.BaseActivity;

/**
 * Created by cesar on 6/16/13.
 */
public class ActivityAbout extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView forums = (TextView) findViewById(R.id.textViewAboutForumLink);
		forums.setText(R.string.link_forum);
		TextView donations = (TextView) findViewById(R.id.textViewAboutDonationsUrl);
		donations.setText(R.string.link_donations);
		Linkify.addLinks(forums, Linkify.WEB_URLS);
		Linkify.addLinks(donations, Linkify.WEB_URLS);
		Button titleBack = (Button) findViewById(R.id.buttonFragmentTitle);
		titleBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				navigateUp();
			}
		});
		((Button) findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_about));
	}
}