package com.cesarandres.ps2link.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cesarandres.ps2link.ActivityContainerSingle;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.BitmapWorkerTask;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMainMenu extends BaseFragment {

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
				mCallbacks.onItemSelected(ActivityMode.ACTIVITY_PROFILE_LIST.toString(), null);
			}
		});

		final Button buttonServers = (Button) root.findViewById(R.id.buttonServers);
		buttonServers.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCallbacks.onItemSelected(ActivityMode.ACTIVITY_SERVER_LIST.toString(), null);
			}
		});

		final Button buttonOutfit = (Button) root.findViewById(R.id.buttonOutfit);
		buttonOutfit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCallbacks.onItemSelected(ActivityMode.ACTIVITY_OUTFIT_LIST.toString(), null);
			}
		});

		final Button buttonNews = (Button) root.findViewById(R.id.buttonNews);
		buttonNews.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCallbacks.onItemSelected(ActivityMode.ACTIVITY_LINK_MENU.toString(), null);
			}
		});

		final Button buttonTwitter = (Button) root.findViewById(R.id.buttonTwitter);
		buttonTwitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mCallbacks.onItemSelected(ActivityMode.ACTIVITY_TWITTER.toString(), null);
			}
		});

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button titleButton = ((Button) getActivity().findViewById(R.id.buttonFragmentTitle));
		titleButton.setText(getString(R.string.app_name_capital));

		final ImageButton buttonPS2Background = (ImageButton) getActivity().findViewById(R.id.buttonPS2);
		buttonPS2Background.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ImageView background = ((ImageView) getActivity().findViewById(R.id.imageViewBackground));
				background.setImageResource(R.drawable.ps2_activity_background);
				background.setScaleType(ScaleType.FIT_START);

				SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.PS2.toString());
				editor.commit();
			}
		});

		final ImageButton buttonNCBackground = (ImageButton) getActivity().findViewById(R.id.buttonNC);
		buttonNCBackground.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				BitmapWorkerTask task = new BitmapWorkerTask((ImageView) (getActivity().findViewById(R.id.imageViewBackground)), getActivity());
				task.execute("nc_wallpaper.jpg");
				SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.NC.toString());
				editor.commit();
			}
		});

		final ImageButton buttonTRBackground = (ImageButton) getActivity().findViewById(R.id.buttonTR);
		buttonTRBackground.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				BitmapWorkerTask task = new BitmapWorkerTask((ImageView) (getActivity().findViewById(R.id.imageViewBackground)), getActivity());
				task.execute("tr_wallpaper.jpg");
				SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.TR.toString());
				editor.commit();

			}
		});

		final ImageButton buttonVSBackground = (ImageButton) getActivity().findViewById(R.id.buttonVS);
		buttonVSBackground.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				BitmapWorkerTask task = new BitmapWorkerTask((ImageView) (getActivity().findViewById(R.id.imageViewBackground)), getActivity());
				task.execute("vs_wallpaper.jpg");
				SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("preferedWallpaper", ApplicationPS2Link.WallPaperMode.VS.toString());
				editor.commit();
			}
		});

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
					mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
							new String[] { settings.getString("preferedProfile", "") });
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
					mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
							new String[] { settings.getString("preferedOutfit", "") });
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

}
