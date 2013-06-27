package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.backlog.Profile;
import com.cesarandres.ps2link.soe.content.response.Character_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfile extends BaseFragment {

	private static boolean isCached;
	private CharacterProfile profile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new UpdateProfileFromTable().execute(getActivity().getIntent()
				.getExtras().getString("profileId"));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View root = inflater.inflate(R.layout.fragment_profile, container,
				false);

		Button updateButton = (Button) root
				.findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				downloadProfiles(profile.getId());
			}
		});

		root.findViewById(R.id.buttonFragmentRemoveContact).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						new UnCacheProfile().execute(profile);
					}
				});

		root.findViewById(R.id.buttonFragmentAddContact).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						new CacheProfile().execute(profile);
					}
				});

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateUI(CharacterProfile character) {

		((TextView) getActivity().findViewById(R.id.textViewFragmentTitle))
				.setText(character.getName().getFirst());

		TextView faction = ((TextView) getActivity().findViewById(
				R.id.textViewProfileServer));
		faction.setText("");
		if (character.getFaction_id().equals(Faction.VS)) {
			faction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vs_icon,
					0, 0, 0);
		} else if (character.getFaction_id().equals(Faction.NC)) {
			faction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.nc_icon,
					0, 0, 0);
		} else if (character.getFaction_id().equals(Faction.TR)) {
			faction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tr_icon,
					0, 0, 0);
		}

		TextView initialBR = ((TextView) getActivity().findViewById(
				R.id.textViewCurrentRank));
		initialBR.setText(Integer.toString(character.getBattle_rank()
				.getValue()));
		initialBR.setTextColor(Color.BLACK);

		TextView nextBR = ((TextView) getActivity().findViewById(
				R.id.textViewNextRank));
		nextBR.setText(Integer
				.toString(character.getBattle_rank().getValue() + 1));
		nextBR.setTextColor(Color.BLACK);

		int progressBR = (character.getBattle_rank().getPercent_to_next());
		((ProgressBar) getActivity().findViewById(
				R.id.progressBarProfileBRProgress))
				.setProgress((int) (progressBR));

		Float progressCerts = Float.parseFloat(character.getCerts()
				.getPercent_to_next());
		((ProgressBar) getActivity().findViewById(
				R.id.progressBarProfileCertsProgress))
				.setProgress((int) (progressCerts * 100));
		TextView certs = ((TextView) getActivity().findViewById(
				R.id.textViewProfileCertsValue));
		certs.setText(character.getCerts().getAvailable_points());

		((TextView) getActivity().findViewById(
				R.id.textViewProfileMinutesPlayed)).setText(character
				.getTimes().getMinutes_played());
		((TextView) getActivity().findViewById(R.id.textViewProfileLastLogin))
				.setText(character.getTimes().getLast_login());

		((Button) getActivity().findViewById(R.id.buttonProfileFriends))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Toast.makeText(getActivity(),
								"This will be implemented later",
								Toast.LENGTH_SHORT).show();
					}
				});

		if (isCached) {
			getActivity().findViewById(R.id.buttonFragmentRemoveContact)
					.setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.buttonFragmentAddContact)
					.setVisibility(View.GONE);
		} else {
			getActivity().findViewById(R.id.buttonFragmentRemoveContact)
					.setVisibility(View.GONE);
			getActivity().findViewById(R.id.buttonFragmentAddContact)
					.setVisibility(View.VISIBLE);
		}
	}

	private void downloadProfiles(String character_id) {

		URL url;
		try {
			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2,
					PS2Collection.CHARACTER, character_id, null);

			Listener<Character_response> success = new Response.Listener<Character_response>() {
				@Override
				public void onResponse(Character_response response) {
					updateUI(response.getCharacter_list().get(0));
				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
				}
			};

			GsonRequest<Character_response> gsonOject = new GsonRequest<Character_response>(
					url.toString(), Character_response.class, null, success,
					error);
			ApplicationPS2Link.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class UpdateProfileFromTable extends
			AsyncTask<String, Integer, CharacterProfile> {
		@Override
		protected CharacterProfile doInBackground(String... args) {
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			CharacterProfile profile = data.getCharacter(args[0], false);
			if (profile == null) {
				profile = data.getCharacter(args[0], true);
				isCached = false;
			} else {
				isCached = true;
			}
			data.close();
			return profile;
		}

		@Override
		protected void onPostExecute(CharacterProfile result) {
			profile = result;
			updateUI(result);
		}
	}

	private class CacheProfile extends
			AsyncTask<CharacterProfile, Integer, CharacterProfile> {
		@Override
		protected CharacterProfile doInBackground(CharacterProfile... args) {
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			CharacterProfile profile = args[0];
			data.insertCharacter(profile, false);
			data.deleteCharacter(profile, true);
			isCached = true;

			data.close();
			return profile;
		}

		@Override
		protected void onPostExecute(CharacterProfile result) {
			profile = result;
			updateUI(result);
		}
	}

	private class UnCacheProfile extends
			AsyncTask<CharacterProfile, Integer, CharacterProfile> {
		@Override
		protected CharacterProfile doInBackground(CharacterProfile... args) {
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			CharacterProfile profile = args[0];
			data.insertCharacter(profile, true);
			data.deleteCharacter(profile, false);
			isCached = false;

			data.close();
			return profile;
		}

		@Override
		protected void onPostExecute(CharacterProfile result) {
			profile = result;
			updateUI(result);
		}
	}

}
