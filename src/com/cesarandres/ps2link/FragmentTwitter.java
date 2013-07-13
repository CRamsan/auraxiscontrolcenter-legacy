package com.cesarandres.ps2link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.module.twitter.PS2Tweet;
import com.cesarandres.ps2link.module.twitter.TwitterUtil;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.view.TwitterItemAdapter;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentTwitter extends Fragment {

	public static Bitmap ps2deals;
	public static Bitmap plantside2;
	public static Bitmap purrfectstorm;
	public static Bitmap mhigby;
	private FragmentTwitter tag = this;
	private ArrayList<AsyncTask> taskList;
	private ObjectDataSource data;
	private boolean loaded = false;
	private static final String MHIGBY = "checkBoxMHigby";
	private static final String PURRFECTSTORM = "checkBoxPurrfect";
	private static final String PS2DEALS = "checkBoxPS2Deals";
	private static final String PSTRAY = "checkBoxPSTRay";
	private static final String PLANETSIDE2 = "checkBoxPS2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskList = new ArrayList<AsyncTask>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View root = inflater.inflate(R.layout.fragment_twitter, container, false);

		ListView listRoot = (ListView) root.findViewById(R.id.listViewTweetList);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				String text = ((PS2Tweet) myAdapter.getItemAtPosition(myItemInt)).getContent();
				ArrayList<String> links = new ArrayList<String>();
				String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(text);
				while (m.find()) {
					String urlStr = m.group();
					if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
						urlStr = urlStr.substring(1, urlStr.length() - 1);
					}
					links.add(urlStr);
				}
				if (links.size() > 0) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(0)));
					startActivity(browserIntent);
				}
			}
		});

		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);

		CheckBox mhigby = ((CheckBox) root.findViewById(R.id.checkBoxTwitterMhigby));
		mhigby.setChecked(settings.getBoolean(MHIGBY, false));
		mhigby.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateTweets();
			}
		});

		CheckBox planetside = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPlanetside));
		planetside.setChecked(settings.getBoolean(PLANETSIDE2, false));
		planetside.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateTweets();
			}
		});

		CheckBox ps2deals = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPS2Deals));
		ps2deals.setChecked(settings.getBoolean(PS2DEALS, false));
		ps2deals.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateTweets();
			}
		});

		CheckBox purrfectStorm = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPurrfectstorm));
		purrfectStorm.setChecked(settings.getBoolean(PURRFECTSTORM, false));
		purrfectStorm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateTweets();
			}
		});

		CheckBox pstrayStorm = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPSTray));
		pstrayStorm.setChecked(settings.getBoolean(PSTRAY, false));
		pstrayStorm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateTweets();
			}
		});

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.text_menu_twitter));
		ImageButton updateButton = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);
		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ArrayList<String> usersnames = new ArrayList<String>();
				if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMhigby)).isChecked()) {
					usersnames.add(TwitterUtil.MHIDGY);
				}
				if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetside)).isChecked()) {
					usersnames.add(TwitterUtil.PLANETSIDE2);
				}
				if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPS2Deals)).isChecked()) {
					usersnames.add(TwitterUtil.PS2DAILYDEALS);
				}
				if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPurrfectstorm)).isChecked()) {
					usersnames.add(TwitterUtil.PURRFECTSTORM);
				}
				if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPSTray)).isChecked()) {
					usersnames.add(TwitterUtil.PS_TRAY);
				}
				String[] stringArray = Arrays.copyOf(usersnames.toArray(), usersnames.size(), String[].class);
				new UpdateTweets().execute(stringArray);
			}
		});
		data = ((ActivityContainerSingle) getActivity()).getData();
		updateTweets();
		if (savedInstanceState == null) {
			new UpdateTweets().execute(new String[] {
					TwitterUtil.PURRFECTSTORM,
					TwitterUtil.MHIDGY,
					TwitterUtil.PLANETSIDE2,
					TwitterUtil.PS2DAILYDEALS,
					TwitterUtil.PS_TRAY });
		} else {
			this.loaded = savedInstanceState.getBoolean("twitterLoader", false);
			if (!this.loaded) {
				new UpdateTweets().execute(new String[] {
						TwitterUtil.PURRFECTSTORM,
						TwitterUtil.MHIDGY,
						TwitterUtil.PLANETSIDE2,
						TwitterUtil.PS2DAILYDEALS,
						TwitterUtil.PS_TRAY });
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean("twitterLoader", loaded);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		for (AsyncTask task : taskList) {
			task.cancel(true);
		}
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(PLANETSIDE2, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetside)).isChecked());
		editor.putBoolean(PS2DEALS, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPS2Deals)).isChecked());
		editor.putBoolean(PURRFECTSTORM, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPurrfectstorm)).isChecked());
		editor.putBoolean(MHIGBY, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMhigby)).isChecked());
		editor.putBoolean(PSTRAY, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPSTray)).isChecked());
		editor.commit();

		ApplicationPS2Link.volley.cancelAll(tag);
	}

	private void setUpdateButton(boolean enabled) {
		getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(enabled);
	}

	private void setUpdateView(boolean enabled) {
		if (enabled) {
			View loadingView = getActivity().findViewById(R.id.loadingItemList);
			if (loadingView != null) {
				LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTwitter);
				layout.removeView(loadingView);
			}
		} else {
			View loadingView = getActivity().findViewById(R.id.loadingItemList);
			if (loadingView == null) {
				LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTwitter);
				loadingView = getActivity().getLayoutInflater().inflate(R.layout.loading_item_list, null);
				layout.addView(loadingView, 1);
			}
		}
	}

	private void updateTweets() {
		ArrayList<String> usersnames = new ArrayList<String>();
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMhigby)).isChecked()) {
			usersnames.add(TwitterUtil.MHIDGY);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetside)).isChecked()) {
			usersnames.add(TwitterUtil.PLANETSIDE2);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPS2Deals)).isChecked()) {
			usersnames.add(TwitterUtil.PS2DAILYDEALS);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPurrfectstorm)).isChecked()) {
			usersnames.add(TwitterUtil.PURRFECTSTORM);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPSTray)).isChecked()) {
			usersnames.add(TwitterUtil.PS_TRAY);
		}
		String[] stringArray = Arrays.copyOf(usersnames.toArray(), usersnames.size(), String[].class);
		updateContent(stringArray);
	}

	private void updateContent(String[] users) {
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewTweetList);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				String url = "https://twitter.com/#!/" + ((PS2Tweet) myAdapter.getItemAtPosition(myItemInt)).getTag() + "/status/"
						+ ((PS2Tweet) myAdapter.getItemAtPosition(myItemInt)).getId();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		listRoot.setAdapter(new TwitterItemAdapter(getActivity(), users, data));
	}

	private class UpdateTweets extends AsyncTask<String, Integer, String[]> {

		@Override
		protected void onPreExecute() {
			taskList.add(this);
			setUpdateButton(false);
			setUpdateView(false);
		}

		@Override
		protected String[] doInBackground(String... users) {
			ArrayList<PS2Tweet> tweetList = new ArrayList<PS2Tweet>(0);
			try {
				for (String user : users) {
					tweetList = TwitterUtil.getTweets(user);
					ObjectDataSource data = new ObjectDataSource(getActivity());
					data.open();
					data.insertAllTweets(tweetList, user);
					data.close();

				}
			} catch (Exception e) {
				return null;
			}
			return users;
		}

		@Override
		protected void onPostExecute(String[] result) {
			if (!this.isCancelled()) {
				if (result != null) {
					updateTweets();
				}
				setUpdateView(true);
				setUpdateButton(true);
			}
			loaded = true;
			taskList.remove(this);
		}
	}
}
