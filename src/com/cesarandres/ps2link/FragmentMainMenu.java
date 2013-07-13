package com.cesarandres.ps2link;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMainMenu extends Fragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_main_menu, container, false);

		final Button buttonCharacters = (Button) root.findViewById(R.id.buttonCharacters);
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityContainerSingle.class);
				intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_PROFILE_LIST.toString());
				startActivity(intent);
			}
		});

		final Button buttonServers = (Button) root.findViewById(R.id.buttonServers);
		buttonServers.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityContainerSingle.class);
				intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_SERVER_LIST.toString());
				startActivity(intent);
			}
		});

		final Button buttonOutfit = (Button) root.findViewById(R.id.buttonOutfit);
		buttonOutfit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityContainerSingle.class);
				intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_OUTFIT_LIST.toString());
				startActivity(intent);
			}
		});

		final Button buttonNews = (Button) root.findViewById(R.id.buttonNews);
		buttonNews.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://www.reddit.com/r/Planetside/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonTwitter = (Button) root.findViewById(R.id.buttonTwitter);
		buttonTwitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityContainerSingle.class);
				intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_TWITTER.toString());
				startActivity(intent);
			}
		});

		final Button buttonAbout = (Button) root.findViewById(R.id.buttonAbout);
		buttonAbout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityAbout.class);
				startActivity(intent);
			}
		});

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button titleButton = ((Button) getActivity().findViewById(R.id.buttonFragmentTitle));
		titleButton.setText(getString(R.string.fragment_title));
		titleButton.setCompoundDrawables(null, null, null, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		String preferedProfileId = settings.getString("preferedProfile", "");
		String preferedProfileName = settings.getString("preferedProfileName", "");
		final Button buttonPreferedProfile = (Button) getActivity().findViewById(R.id.buttonPreferedProfile);
		if (!preferedProfileId.equals("")) {
			buttonPreferedProfile.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
					Intent intent = new Intent();
					intent.setClass(getActivity(), ActivityContainerSingle.class);
					intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_PROFILE.toString());
					intent.putExtra("profileId", settings.getString("preferedProfile", ""));
					startActivity(intent);
				}
			});
			buttonPreferedProfile.setText(preferedProfileName);
			buttonPreferedProfile.setVisibility(View.VISIBLE);
		} else {
			buttonPreferedProfile.setVisibility(View.GONE);
		}

		String preferedOutfitId = settings.getString("preferedOutfit", "");
		String preferedOutfitName = settings.getString("preferedOutfitName", "");
		final Button buttonPreferedOutfit = (Button) getActivity().findViewById(R.id.buttonPreferedOutfit);
		if (!preferedOutfitId.equals("")) {

			buttonPreferedOutfit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
					Intent intent = new Intent();
					intent.setClass(getActivity(), ActivityContainerSingle.class);
					intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_MEMBER_LIST.toString());
					intent.putExtra("outfit_id", settings.getString("preferedOutfit", ""));
					startActivity(intent);
				}
			});
			buttonPreferedOutfit.setVisibility(View.VISIBLE);
			buttonPreferedOutfit.setText(preferedOutfitName);
		} else {
			buttonPreferedOutfit.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
