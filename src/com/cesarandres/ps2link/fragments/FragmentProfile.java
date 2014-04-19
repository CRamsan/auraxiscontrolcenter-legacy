package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ActivityContainer;
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
import com.cesarandres.ps2link.soe.content.response.Server_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfile extends BaseFragment {

	private boolean isCached;
	private CharacterProfile profile;
	private String profileId;
	private ArrayList<AsyncTask> taskList;
	private ObjectDataSource data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.data = ((ActivityContainer) getActivity()).getData();

		taskList = new ArrayList<AsyncTask>();
		UpdateProfileFromTable task = new UpdateProfileFromTable();
		taskList.add(task);
		this.profileId = getArguments().getString("PARAM_0");
		task.execute(this.profileId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_profile, container, false);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		ImageButton fragmentUpdate = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
		ToggleButton showOffline = (ToggleButton) getActivity().findViewById(R.id.toggleButtonShowOffline);
		ImageButton fragmentAdd = (ImageButton) getActivity().findViewById(R.id.buttonFragmentAdd);
		ToggleButton fragmentStar = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentStar);
		ToggleButton fragmentAppend = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentAppend);
		
		fragmentUpdate.setVisibility(View.VISIBLE);
		showOffline.setVisibility(View.GONE);
		fragmentAdd.setVisibility(View.GONE);
		fragmentStar.setVisibility(View.VISIBLE);
		fragmentAppend.setVisibility(View.VISIBLE);

		fragmentUpdate.setEnabled(true);
		showOffline.setEnabled(true);
		fragmentAdd.setEnabled(true);
		fragmentStar.setEnabled(true);
		fragmentAppend.setEnabled(true);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ApplicationPS2Link.volley.cancelAll(this);
		for (AsyncTask task : taskList) {
			task.cancel(true);
		}
	}

	private void updateUI(CharacterProfile character) {

		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(character.getName().getFirst());

		if(this.getView() != null){
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
		
		ToggleButton star = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentStar);
		star.setVisibility(View.VISIBLE);
		star.setOnCheckedChangeListener(null);
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		String preferedProfileId = settings.getString("preferedProfile", "");
		if (preferedProfileId.equals(character.getCharacterId())) {
			star.setChecked(true);
		} else {
			star.setChecked(false);
		}

		star.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
			}
		});

		ToggleButton append = ((ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentAppend));
		append.setOnCheckedChangeListener(null);
		append.setChecked(isCached);
		append.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					CacheProfile task = new CacheProfile();
					taskList.add(task);
					task.execute(profile);
				} else {
					UnCacheProfile task = new UnCacheProfile();
					taskList.add(task);
					task.execute(profile);
				}
			}
		});
	}

	private void setActionBarEnabled(boolean enabled) {
		getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(enabled);
		getActivity().findViewById(R.id.toggleButtonFragmentAppend).setEnabled(enabled);
		getActivity().findViewById(R.id.toggleButtonFragmentStar).setEnabled(enabled);
		if (enabled) {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.GONE);
		} else {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.GONE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.VISIBLE);
		}
	}

	public void downloadProfiles(String character_id) {

		setActionBarEnabled(false);
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, PS2Collection.CHARACTER, character_id,
					QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "outfit,world,online_status").AddCommand(QueryCommand.JOIN, "type:world^inject_at:server"));
			Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
				@Override
				public void onResponse(Character_list_response response) {
					try {
						profile = response.getCharacter_list().get(0);
						setActionBarEnabled(true);
						profile.setCached(isCached);
						updateUI(profile);
						UpdateProfileToTable task = new UpdateProfileToTable();
						taskList.add(task);
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
					setActionBarEnabled(true);
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
			setActionBarEnabled(false);
		}

		@Override
		protected CharacterProfile doInBackground(String... args) {
			this.profile_id = args[0];
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
			setActionBarEnabled(true);
			if (result == null) {
				downloadProfiles(profile_id);
			} else {
				profile = result;
				updateUI(result);
				downloadProfiles(result.getCharacterId());
			}
			taskList.remove(this);
		}
	}

	private class UpdateProfileToTable extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

		@Override
		protected void onPreExecute() {
			setActionBarEnabled(false);
		}

		@Override
		protected CharacterProfile doInBackground(CharacterProfile... args) {
			CharacterProfile profile = null;
			try {
				profile = args[0];
				data.updateCharacter(profile, !profile.isCached());
			} finally {
			}
			return profile;
		}

		@Override
		protected void onPostExecute(CharacterProfile result) {
			setActionBarEnabled(true);
			taskList.remove(this);
		}
	}

	private class CacheProfile extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

		@Override
		protected void onPreExecute() {
			setActionBarEnabled(false);
		}

		@Override
		protected CharacterProfile doInBackground(CharacterProfile... args) {
			try {
				CharacterProfile profile = args[0];
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
			if (!this.isCancelled()) {
				profile = result;
				updateUI(result);
				setActionBarEnabled(true);
			}
			taskList.remove(this);
		}
	}

	private class UnCacheProfile extends AsyncTask<CharacterProfile, Integer, CharacterProfile> {

		@Override
		protected void onPreExecute() {
			setActionBarEnabled(false);
		}

		@Override
		protected CharacterProfile doInBackground(CharacterProfile... args) {
			try {
				CharacterProfile profile = args[0];
				data.updateCharacter(profile, true);
				isCached = false;
			} finally {
			}
			return profile;
		}

		@Override
		protected void onPostExecute(CharacterProfile result) {
			if (!this.isCancelled()) {
				profile = result;
				updateUI(result);
				setActionBarEnabled(true);
			}
			taskList.remove(this);

		}
	}

}
