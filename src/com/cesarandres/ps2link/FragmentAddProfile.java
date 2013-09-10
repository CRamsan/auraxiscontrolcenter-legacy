package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.response.Character_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.view.LoadingItemAdapter;
import com.cesarandres.ps2link.soe.view.ProfileItemAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentAddProfile extends Fragment implements OnClickListener {

	public interface NameToSearchListener {
		void onProfileSelected(CharacterProfile profile);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_add_profile, container, false);

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_profiles));
		final ImageButton buttonCharacters = (ImageButton) getActivity().findViewById(R.id.imageButtonSearchProfile);
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundProfiles);
				listRoot.setOnItemClickListener(null);
				listRoot.setAdapter(new LoadingItemAdapter(getActivity()));
				downloadProfiles();
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		ApplicationPS2Link.volley.cancelAll(this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}

	private void downloadProfiles() {
		EditText searchField = (EditText) getActivity().findViewById(R.id.fieldSearchProfile);
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(
					Verb.GET,
					Game.PS2V1,
					PS2Collection.CHARACTER_NAME,
					"",
					QueryString.generateQeuryString()
							.AddComparison("name.first_lower", SearchModifier.STARTSWITH, searchField.getText().toString().toLowerCase())
							.AddCommand(QueryCommand.LIMIT, "100"));

			Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
				@Override
				public void onResponse(Character_list_response response) {
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundProfiles);
					listRoot.setAdapter(new ProfileItemAdapter(getActivity(), response.getCharacter_name_list(), false));
					listRoot.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), ActivityProfile.class);
							intent.putExtra("profileId", ((CharacterProfile) myAdapter.getItemAtPosition(myItemInt)).getId());
							startActivity(intent);
						}
					});
				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundProfiles);
					if (listRoot != null) {
						listRoot.setAdapter(null);
					}
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

	private class UpdateTmpProfileTable extends AsyncTask<ArrayList<CharacterProfile>, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(ArrayList<CharacterProfile>... profiles) {
			int count = profiles[0].size();
			ArrayList<CharacterProfile> list = profiles[0];
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			CharacterProfile profile = null;
			for (int i = 0; i < count; i++) {
				profile = data.getCharacter(list.get(i).getId());
				if (profile == null) {
					data.insertCharacter(list.get(i), true);
				} else {
					if (profile.isCached()) {
						data.updateCharacter(list.get(i), false);
					} else {
						data.updateCharacter(list.get(i), true);
					}
				}
			}
			data.close();
			return true;
		}
	}
}
