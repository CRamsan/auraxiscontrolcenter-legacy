package com.cesarandres.ps2link;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.content.CharacterProfile;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMainMenu extends BaseFragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_main_menu, container,
				false);

		final Button buttonCharacters = (Button) root
				.findViewById(R.id.buttonCharacters);
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityProfileList.class);
				startActivity(intent);
			}
		});

		final Button buttonServers = (Button) root
				.findViewById(R.id.buttonServers);
		buttonServers.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityServerList.class);
				startActivity(intent);
			}
		});

		final Button buttonOutfit = (Button) root
				.findViewById(R.id.buttonOutfit);
		buttonOutfit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityOutfitList.class);
				startActivity(intent);
			}
		});

		final Button buttonNews = (Button) root.findViewById(R.id.buttonNews);
		buttonNews.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityReddit.class);
				startActivity(intent);
			}
		});

		final Button buttonTwitter = (Button) root.findViewById(R.id.buttonTwitter);
		buttonTwitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityTwitter.class);
				startActivity(intent);
			}
		});
		
		Button titleButton = ((Button)  root.findViewById(R.id.buttonFragmentTitle));
		titleButton.setText(getString(R.string.fragment_title));
		titleButton.setCompoundDrawables(null, null, null, null);
		
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = getActivity().getSharedPreferences(
				"PREFERENCES", 0);
		String preferedProfileId = settings.getString("preferedProfile", "");
		String preferedProfileName = settings.getString("preferedProfileName",
				"");
		final Button buttonPreferedProfile = (Button) getActivity()
				.findViewById(R.id.buttonPreferedProfile);
		if (!preferedProfileId.equals("")) {
			buttonPreferedProfile
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							SharedPreferences settings = getActivity()
									.getSharedPreferences("PREFERENCES", 0);
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									ActivityProfile.class);
							intent.putExtra("profileId",
									settings.getString("preferedProfile", ""));
							startActivity(intent);
						}
					});
			buttonPreferedProfile.setText(preferedProfileName);
			buttonPreferedProfile.setVisibility(View.VISIBLE);
		} else {
			buttonPreferedProfile.setVisibility(View.GONE);
		}

		String preferedOutfitId = settings.getString("preferedOutfit", "");
		String preferedOutfitName = settings
				.getString("preferedOutfitName", "");
		final Button buttonPreferedOutfit = (Button) getActivity()
				.findViewById(R.id.buttonPreferedOutfit);
		if (!preferedOutfitId.equals("")) {

			buttonPreferedOutfit.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					SharedPreferences settings = getActivity()
							.getSharedPreferences("PREFERENCES", 0);
					Intent intent = new Intent();
					intent.setClass(getActivity(), ActivityMermberList.class);
					intent.putExtra("outfit_id",
							settings.getString("preferedOutfit", ""));
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
