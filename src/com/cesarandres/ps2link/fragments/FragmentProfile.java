package com.cesarandres.ps2link.fragments;

import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Character_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.Logger;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;

/**
 * This fragment will read a profile from the database and display it to the
 * user. It will then try to update the data by doing a query to the API
 */
public class FragmentProfile extends BaseFragment {

    private boolean isCached;
    private CharacterProfile profile;
    private String profileId;

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
	UpdateProfileFromTable task = new UpdateProfileFromTable();
	setCurrentTask(task);
	this.profileId = getArguments().getString("PARAM_0");
	task.execute(this.profileId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /**
     * @param character
     *            Character that contains all the data to populate the UI
     */
    private void updateUI(final CharacterProfile character) {
	this.fragmentTitle.setText(character.getName().getFirst());
	try {
	    if (this.getView() != null) {
		ImageView faction = ((ImageView) getActivity().findViewById(R.id.imageViewProfileFaction));
		if (character.getFaction_id().equals(Faction.VS)) {
		    faction.setImageResource(R.drawable.icon_faction_vs);
		} else if (character.getFaction_id().equals(Faction.NC)) {
		    faction.setImageResource(R.drawable.icon_faction_nc);
		} else if (character.getFaction_id().equals(Faction.TR)) {
		    faction.setImageResource(R.drawable.icon_faction_tr);
		}

		TextView initialBR = ((TextView) getActivity().findViewById(R.id.textViewCurrentRank));
		initialBR.setText(Integer.toString(character.getBattle_rank().getValue()));
		initialBR.setTextColor(Color.BLACK);

		TextView nextBR = ((TextView) getActivity().findViewById(R.id.textViewNextRank));
		nextBR.setText(Integer.toString(character.getBattle_rank().getValue() + 1));
		nextBR.setTextColor(Color.BLACK);

		int progressBR = (character.getBattle_rank().getPercent_to_next());
		((ProgressBar) getActivity().findViewById(R.id.progressBarProfileBRProgress)).setProgress((int) (progressBR));

		Float progressCerts = Float.parseFloat(character.getCerts().getPercent_to_next());
		((ProgressBar) getActivity().findViewById(R.id.progressBarProfileCertsProgress)).setProgress((int) (progressCerts * 100));
		TextView certs = ((TextView) getActivity().findViewById(R.id.textViewProfileCertsValue));
		certs.setText(character.getCerts().getAvailable_points());

		TextView loginStatus = ((TextView) getActivity().findViewById(R.id.TextViewProfileLoginStatusText));
		String onlineStatusText = "UNKOWN";
		if (character.getOnline_status() == 0) {
		    onlineStatusText = "OFFLINE";
		    loginStatus.setText(onlineStatusText);
		    loginStatus.setTextColor(Color.RED);
		} else {
		    onlineStatusText = "ONLINE";
		    loginStatus.setText(onlineStatusText);
		    loginStatus.setTextColor(Color.GREEN);
		}

		((TextView) getActivity().findViewById(R.id.textViewProfileMinutesPlayed)).setText(Integer.toString((Integer.parseInt(character.getTimes()
			.getMinutes_played()) / 60)));

		PrettyTime p = new PrettyTime();
		String lastLogin = p.format(new Date(Long.parseLong(character.getTimes().getLast_login()) * 1000));

		((TextView) getActivity().findViewById(R.id.textViewProfileLastLogin)).setText(lastLogin);

		if (character.getOutfitName() == null) {
	    	Button outfitButton = (Button)getActivity().findViewById(R.id.buttonProfileToOutfit);
		    if (character.getOutfit() == null) {
		    	outfitButton.setOnClickListener(null);	    	
		    	outfitButton.setEnabled(false);
		    	outfitButton.getBackground().setAlpha(85);
		    } else {
		    	outfitButton.setText(character.getOutfit().getName());
		    	outfitButton.setEnabled(true);
		    	outfitButton.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
				    	mCallbacks.onItemSelected(ActivityMode.ACTIVITY_MEMBER_LIST.toString(), new String[] { character.getOutfit().getOutfit_Id() });
					}
				});	
		    }
		}

		if (character.getServer() != null) {
		    ((TextView) getActivity().findViewById(R.id.textViewServerText)).setText(character.getServer().getName().getEn());
		} else {
		    ((TextView) getActivity().findViewById(R.id.textViewServerText)).setText("UNKNOWN");
		}

	    }

	    this.fragmentStar.setOnCheckedChangeListener(null);
	    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
	    String preferedProfileId = settings.getString("preferedProfile", "");
	    if (preferedProfileId.equals(character.getCharacterId())) {
		this.fragmentStar.setChecked(true);
	    } else {
		this.fragmentStar.setChecked(false);
	    }

	    this.fragmentStar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		    SharedPreferences.Editor editor = settings.edit();
		    if (isChecked) {
			editor.putString("preferedProfile", profile.getCharacterId());
			editor.putString("preferedProfileName", profile.getName().getFirst());
		    } else {
			editor.putString("preferedProfileName", "");
			editor.putString("preferedProfile", "");
		    }
		    editor.commit();
		    getActivityContainer().checkPreferedButtons();
		}
	    });

	    this.fragmentAppend.setOnCheckedChangeListener(null);
	    this.fragmentAppend.setChecked(isCached);
	    this.fragmentAppend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    if (isChecked) {
			CacheProfile task = new CacheProfile();
			setCurrentTask(task);
			task.execute(profile);
		    } else {
			UnCacheProfile task = new UnCacheProfile();
			setCurrentTask(task);
			task.execute(profile);
		    }
		}
	    });
	} catch (NullPointerException e) {
	    // TODO Check when this error happens
	    Logger.log(Log.ERROR, this, "Null Pointer while trying to set character data on UI");
	}
    }

    /**
     * @param character_id
     *            Character id of the character that wants to be download
     */
    public void downloadProfiles(String character_id) {
	this.setProgressButton(true);
	String url = SOECensus.generateGameDataRequest(
		Verb.GET,
		PS2Collection.CHARACTER,
		character_id,
		QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "outfit,world,online_status")
			.AddCommand(QueryCommand.JOIN, "type:world^inject_at:server")).toString();
	Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
	    @Override
	    public void onResponse(Character_list_response response) {
		setProgressButton(false);
		try {
		    profile = response.getCharacter_list().get(0);
		    profile.setCached(isCached);
		    updateUI(profile);
		    UpdateProfileToTable task = new UpdateProfileToTable();
		    setCurrentTask(task);
		    task.execute(profile);
		} catch (Exception e) {
		    Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
		}
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
		Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
	    }
	};

	SOECensus.sendGsonRequest(url, Character_list_response.class, success, error, this);
    }

    /**
     * Read the profile from the database and update the UI
     */
    private class UpdateProfileFromTable extends AsyncTask<String, Integer, CharacterProfile> {

	private String profile_id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected CharacterProfile doInBackground(String... args) {
	    this.profile_id = args[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    CharacterProfile profile = data.getCharacter(this.profile_id);
	    if (profile == null) {
		isCached = false;
	    } else {
		isCached = profile.isCached();
	    }
	    return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(CharacterProfile result) {
	    setProgressButton(false);
	    if (result == null) {
		downloadProfiles(profile_id);
	    } else {
		profile = result;
		updateUI(result);
		downloadProfiles(result.getCharacterId());
	    }
	}
    }

    /**
     * Save the profile to the database
     */
    private class UpdateProfileToTable extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected CharacterProfile doInBackground(CharacterProfile... args) {
	    CharacterProfile profile = null;
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		profile = args[0];
		if(data.getCharacter(profileId) != null){
			data.updateCharacter(profile, !profile.isCached());
		}else{
			data.insertCharacter(profile, !profile.isCached());
		}
		
		if(profile.getOutfit() != null){
			Outfit outfit = data.getOutfit(profile.getOutfit().getOutfit_Id());
			if(outfit == null){
				outfit = profile.getOutfit();
				data.insertOutfit(outfit, true);
			}
		}
		
	    } catch (Exception e) {

	    }
	    return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(CharacterProfile result) {
	    setProgressButton(false);
	}
    }

    /**
     * Save the profile in the database and set it as not temporary
     */
    private class CacheProfile extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected CharacterProfile doInBackground(CharacterProfile... args) {
	    CharacterProfile profile = args[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		if (data.getCharacter(profile.getCharacterId()) == null) {
		    data.insertCharacter(profile, false);
		} else {
		    data.updateCharacter(profile, false);
		}
		isCached = true;
	    } finally {

	    }
	    return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(CharacterProfile result) {
	    profile = result;
	    updateUI(result);
	    setProgressButton(false);
	}
    }

    /**
     * Update the database profile and set the profile as temporary
     */
    private class UnCacheProfile extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected CharacterProfile doInBackground(CharacterProfile... args) {
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		CharacterProfile profile = args[0];
		data.updateCharacter(profile, true);
		isCached = false;
	    } catch (Exception e) {

	    }
	    return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(CharacterProfile result) {
	    profile = result;
	    updateUI(result);
	    setProgressButton(false);
	}
    }

}
