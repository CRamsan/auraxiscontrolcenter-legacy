package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.response.Character_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfile extends BaseFragment {

    private boolean isCached;
    private CharacterProfile profile;
    private String profileId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	View root = inflater.inflate(R.layout.fragment_profile, container, false);
	return root;
    }

    @Override
    public void onResume() {
	super.onResume();

	UpdateProfileFromTable task = new UpdateProfileFromTable();
	this.currentTask = task;
	this.profileId = getArguments().getString("PARAM_0");
	task.execute(this.profileId);

    }

    @Override
    public void onPause() {
	super.onPause();
    }

    private void updateUI(CharacterProfile character) {

	this.fragmentTitle.setText(character.getName().getFirst());

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
		if (character.getOutfit() == null) {
		    ((TextView) getActivity().findViewById(R.id.textViewOutfitText)).setText("NONE");
		} else {
		    ((TextView) getActivity().findViewById(R.id.textViewOutfitText)).setText(character.getOutfit().getName());
		}
	    } else {
		((TextView) getActivity().findViewById(R.id.textViewOutfitText)).setText(character.getOutfitName());
	    }

	    if (character.getServer() != null) {
		((TextView) getActivity().findViewById(R.id.textViewServerText)).setText(character.getServer().getName().getEn());
	    } else {
		((TextView) getActivity().findViewById(R.id.textViewServerText)).setText("UNKNOWN");
	    }

	}

	this.fragmentStar.setOnCheckedChangeListener(null);
	this.fragmentStar.setVisibility(View.VISIBLE);
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
	this.fragmentAppend.setVisibility(View.VISIBLE);
	this.fragmentAppend.setChecked(isCached);
	this.fragmentAppend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
		    CacheProfile task = new CacheProfile();
		    currentTask = task;
		    task.execute(profile);
		} else {
		    UnCacheProfile task = new UnCacheProfile();
		    currentTask = task;
		    task.execute(profile);
		}
	    }
	});
    }

    public void downloadProfiles(String character_id) {

	this.setProgressButton(true);
	URL url;
	try {
	    url = SOECensus.generateGameDataRequest(
		    Verb.GET,
		    Game.PS2V2,
		    PS2Collection.CHARACTER,
		    character_id,
		    QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "outfit,world,online_status")
			    .AddCommand(QueryCommand.JOIN, "type:world^inject_at:server"));
	    Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
		@Override
		public void onResponse(Character_list_response response) {
		    try {
			profile = response.getCharacter_list().get(0);
			setProgressButton(false);
			profile.setCached(isCached);
			updateUI(profile);
			UpdateProfileToTable task = new UpdateProfileToTable();
			currentTask = task;
			task.execute(profile);
		    } catch (Exception e) {
			Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
		    }
		}
	    };

	    ErrorListener error = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
		    error.equals(new Object());
		    setProgressButton(false);
		}
	    };

	    GsonRequest<Character_list_response> gsonOject = new GsonRequest<Character_list_response>(url.toString(), Character_list_response.class, null,
		    success, error);
	    gsonOject.setTag(this);
	    ApplicationPS2Link.volley.add(gsonOject);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private class UpdateProfileFromTable extends AsyncTask<String, Integer, CharacterProfile> {

	private String profile_id;

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected CharacterProfile doInBackground(String... args) {
	    this.profile_id = args[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    CharacterProfile profile = data.getCharacter(this.profile_id);
	    try {
		if (profile == null) {
		    isCached = false;
		} else {
		    isCached = profile.isCached();
		}
	    } finally {
	    }
	    return profile;
	}

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

    private class UpdateProfileToTable extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected CharacterProfile doInBackground(CharacterProfile... args) {
	    CharacterProfile profile = null;
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		profile = args[0];
		data.updateCharacter(profile, !profile.isCached());
	    } catch (Exception e) {

	    }
	    return profile;
	}

	@Override
	protected void onPostExecute(CharacterProfile result) {
	    setProgressButton(false);
	}
    }

    private class CacheProfile extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

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

	@Override
	protected void onPostExecute(CharacterProfile result) {
	    profile = result;
	    updateUI(result);
	    setProgressButton(false);
	}
    }

    private class UnCacheProfile extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

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

	@Override
	protected void onPostExecute(CharacterProfile result) {
	    profile = result;
	    updateUI(result);
	    setProgressButton(false);
	}
    }

}
