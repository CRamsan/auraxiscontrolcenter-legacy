package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.TwitterException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.ToggleButton;

import com.cesarandres.ps2link.ActivityContainer;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.module.twitter.PS2Tweet;
import com.cesarandres.ps2link.module.twitter.TwitterUtil;
import com.cesarandres.ps2link.soe.util.Logger;
import com.cesarandres.ps2link.soe.view.TwitterItemAdapter;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentTwitter extends BaseFragment {

    private static final String MHIGBY = "checkBoxMHigby";
    private static final String PURRFECTSTORM = "checkBoxPurrfect";
    private static final String PS2DEALS = "checkBoxPS2Deals";
    private static final String PSTRAY = "checkBoxPSTRay";
    private static final String PLANETSIDE2 = "checkBoxPS2";
    private static final String PLANETSIDE2EU = "checkBoxPS2EU";
    private static final String RADARX = "checkBoxRadarX";
    private static final String ARCLEGGER = "checkBoxArclegger";
    private static final String MULDOONX9 = "checkBoxMuldoonX9";
    private static final String XALORN = "checkBoxMalorn";
    private static final String TAYRADACTYL = "checkBoxTayradactyl";
    private static final String XANDER = "checkBoxXander";

    private boolean loaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// Inflate the layout for this fragment

	View root = inflater.inflate(R.layout.fragment_twitter, container, false);

	SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);

	OnCheckedChangeListener updateTweetLitener = new OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		updateTweets();
	    }
	};

	CheckBox mhigby = ((CheckBox) root.findViewById(R.id.checkBoxTwitterMhigby));
	mhigby.setChecked(settings.getBoolean(MHIGBY, false));
	mhigby.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox planetside = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPlanetside));
	planetside.setChecked(settings.getBoolean(PLANETSIDE2, false));
	planetside.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox ps2deals = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPS2Deals));
	ps2deals.setChecked(settings.getBoolean(PS2DEALS, false));
	ps2deals.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox purrfectStorm = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPurrfectstorm));
	purrfectStorm.setChecked(settings.getBoolean(PURRFECTSTORM, false));
	purrfectStorm.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox pstrayStorm = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPSTray));
	pstrayStorm.setChecked(settings.getBoolean(PSTRAY, false));
	pstrayStorm.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox planetsideeu = ((CheckBox) root.findViewById(R.id.checkBoxTwitterPlanetsideEU));
	planetsideeu.setChecked(settings.getBoolean(PLANETSIDE2EU, false));
	planetsideeu.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox radarx = ((CheckBox) root.findViewById(R.id.checkBoxTwitterRadarx));
	radarx.setChecked(settings.getBoolean(RADARX, false));
	radarx.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox arclegger = ((CheckBox) root.findViewById(R.id.checkBoxTwitterArclegger));
	arclegger.setChecked(settings.getBoolean(ARCLEGGER, false));
	arclegger.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox muldoonx9 = ((CheckBox) root.findViewById(R.id.checkBoxTwitterMuldoonx9));
	muldoonx9.setChecked(settings.getBoolean(MULDOONX9, false));
	muldoonx9.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox tayradactyl = ((CheckBox) root.findViewById(R.id.checkBoxTwitterTayradactyl));
	tayradactyl.setChecked(settings.getBoolean(TAYRADACTYL, false));
	tayradactyl.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox xander = ((CheckBox) root.findViewById(R.id.checkBoxTwitterXanderClauss));
	xander.setChecked(settings.getBoolean(XANDER, false));
	xander.setOnCheckedChangeListener(updateTweetLitener);

	CheckBox malorn = ((CheckBox) root.findViewById(R.id.checkBoxTwitterXalorn));
	malorn.setChecked(settings.getBoolean(XANDER, false));
	malorn.setOnCheckedChangeListener(updateTweetLitener);

	return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	this.fragmentTitle.setText(getString(R.string.title_twitter));
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
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

		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetsideEU)).isChecked()) {
		    usersnames.add(TwitterUtil.PLANETSIDE2EU);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterRadarx)).isChecked()) {
		    usersnames.add(TwitterUtil.RADARX);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterArclegger)).isChecked()) {
		    usersnames.add(TwitterUtil.ADAMCLEGG);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMuldoonx9)).isChecked()) {
		    usersnames.add(TwitterUtil.MULDOONX9);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterXalorn)).isChecked()) {
		    usersnames.add(TwitterUtil.XALORN);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterTayradactyl)).isChecked()) {
		    usersnames.add(TwitterUtil.TAYRADACTYL);
		}
		if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterXanderClauss)).isChecked()) {
		    usersnames.add(TwitterUtil.XANDERCLAUSS);
		}

		String[] stringArray = Arrays.copyOf(usersnames.toArray(), usersnames.size(), String[].class);
		currentTask = new UpdateTweetsTask();
		currentTask.execute(stringArray);
	    }
	});
	if (savedInstanceState == null) {
	    currentTask = new UpdateTweetsTask();
	    currentTask.execute(new String[] { TwitterUtil.PURRFECTSTORM, TwitterUtil.MHIDGY, TwitterUtil.PLANETSIDE2, TwitterUtil.PS2DAILYDEALS,
		    TwitterUtil.PS_TRAY, TwitterUtil.ADAMCLEGG, TwitterUtil.MULDOONX9, TwitterUtil.PLANETSIDE2EU, TwitterUtil.RADARX, TwitterUtil.TAYRADACTYL,
		    TwitterUtil.XALORN, TwitterUtil.XANDERCLAUSS });
	} else {
	    this.loaded = savedInstanceState.getBoolean("twitterLoader", false);
	    if (!this.loaded) {
		currentTask = new UpdateTweetsTask();
		currentTask.execute(new String[] { TwitterUtil.PURRFECTSTORM, TwitterUtil.MHIDGY, TwitterUtil.PLANETSIDE2,
			TwitterUtil.PS2DAILYDEALS, TwitterUtil.PS_TRAY, TwitterUtil.ADAMCLEGG, TwitterUtil.MULDOONX9, TwitterUtil.PLANETSIDE2EU,
			TwitterUtil.RADARX, TwitterUtil.TAYRADACTYL, TwitterUtil.XALORN, TwitterUtil.XANDERCLAUSS });
	    }
	}
    }

    @Override
    public void onResume() {
	super.onResume();
	updateTweets();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	savedInstanceState.putBoolean("twitterLoader", loaded);
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
	SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
	SharedPreferences.Editor editor = settings.edit();
	editor.putBoolean(PLANETSIDE2, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetside)).isChecked());
	editor.putBoolean(PS2DEALS, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPS2Deals)).isChecked());
	editor.putBoolean(PURRFECTSTORM, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPurrfectstorm)).isChecked());
	editor.putBoolean(MHIGBY, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMhigby)).isChecked());
	editor.putBoolean(PSTRAY, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPSTray)).isChecked());

	editor.putBoolean(ARCLEGGER, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterArclegger)).isChecked());
	editor.putBoolean(MULDOONX9, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMuldoonx9)).isChecked());
	editor.putBoolean(PLANETSIDE2EU, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetsideEU)).isChecked());
	editor.putBoolean(XALORN, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterXalorn)).isChecked());
	editor.putBoolean(TAYRADACTYL, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterTayradactyl)).isChecked());
	editor.putBoolean(XANDER, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterXanderClauss)).isChecked());
	editor.putBoolean(RADARX, ((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterRadarx)).isChecked());
	editor.commit();
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

	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterPlanetsideEU)).isChecked()) {
	    usersnames.add(TwitterUtil.PLANETSIDE2EU);
	}
	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterRadarx)).isChecked()) {
	    usersnames.add(TwitterUtil.RADARX);
	}
	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterArclegger)).isChecked()) {
	    usersnames.add(TwitterUtil.ADAMCLEGG);
	}
	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterMuldoonx9)).isChecked()) {
	    usersnames.add(TwitterUtil.MULDOONX9);
	}
	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterXalorn)).isChecked()) {
	    usersnames.add(TwitterUtil.XALORN);
	}
	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterTayradactyl)).isChecked()) {
	    usersnames.add(TwitterUtil.TAYRADACTYL);
	}
	if (((CheckBox) getActivity().findViewById(R.id.checkBoxTwitterXanderClauss)).isChecked()) {
	    usersnames.add(TwitterUtil.XANDERCLAUSS);
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
	ObjectDataSource data = ((ActivityContainer) getActivity()).getData();
	listRoot.setAdapter(new TwitterItemAdapter(getActivity(), users, data));
    }

    private class UpdateTweetsTask extends AsyncTask<String, Integer, String[]> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected String[] doInBackground(String... users) {
	    ArrayList<PS2Tweet> tweetList = new ArrayList<PS2Tweet>(0);
	    ObjectDataSource data = ((ActivityContainer) getActivity()).getData();
	    for (String user : users) {
		if (this.isCancelled()) {
		    break;
		}
		try {
		    tweetList = TwitterUtil.getTweets(user);
		    data.insertAllTweets(tweetList, user);
		} catch (TwitterException e) {
		    Logger.log(Log.WARN, FragmentTwitter.this, "Error trying retrieve tweets");
		} catch (IllegalStateException e) {
		    Logger.log(Log.INFO, FragmentTwitter.this, "DB was closed. This is normal.");
		    break;
		}

	    }
	    return users;
	}

	@Override
	protected void onPostExecute(String[] result) {
	    if (result != null) {
		updateTweets();
	    }
	    setProgressButton(false);
	    loaded = true;
	}

	@Override
	protected void onCancelled() {
	    loaded = true;
	}
    }
}
