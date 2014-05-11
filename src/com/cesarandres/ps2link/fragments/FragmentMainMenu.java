package com.cesarandres.ps2link.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.BitmapWorkerTask;

/**
 * @author Cesar Ramirez
 * 
 *         This fragment is very static, it has all the buttons for most of the
 *         main fragments. It will also display the Preferred Character and
 *         Preferred Outfit buttons if those have been set.
 * 
 */
public class FragmentMainMenu extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	this.fragmentTitle.setText(getString(R.string.app_name_capital));

	Button buttonCharacters = (Button) getActivity().findViewById(R.id.buttonCharacters);
	Button buttonServers = (Button) getActivity().findViewById(R.id.buttonServers);
	Button buttonOutfit = (Button) getActivity().findViewById(R.id.buttonOutfit);
	Button buttonNews = (Button) getActivity().findViewById(R.id.buttonNews);
	Button buttonTwitter = (Button) getActivity().findViewById(R.id.buttonTwitter);

	buttonCharacters.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_PROFILE_LIST.toString(), null);
	    }
	});
	buttonServers.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_SERVER_LIST.toString(), null);
	    }
	});
	buttonOutfit.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_OUTFIT_LIST.toString(), null);
	    }
	});
	buttonNews.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_LINK_MENU.toString(), null);
	    }
	});
	buttonTwitter.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_TWITTER.toString(), null);
	    }
	});

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
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	checkPreferedButtons();
    }

    /**
     * This function will check the preferences to see if any profile or outfit
     * has been set as preferred. If any has been set then the respective button
     * will be displayed, they will be hidden otherwise.
     */
    public void checkPreferedButtons() {
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
}
