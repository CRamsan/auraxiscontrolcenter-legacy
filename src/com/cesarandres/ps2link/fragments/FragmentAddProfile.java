package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
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
public class FragmentAddProfile extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
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
		downloadProfiles();
	    }
	});

    }

    @Override
    public void onResume() {
	super.onResume();
    }

    @Override
    public void onStop() {
	super.onStop();
    }

    private void downloadProfiles() {
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundProfiles);
	listRoot.setOnItemClickListener(null);
	listRoot.setAdapter(new LoadingItemAdapter(getActivity()));

	EditText searchField = (EditText) getActivity().findViewById(R.id.fieldSearchProfile);
	URL url;
	try {
	    url = SOECensus.generateGameDataRequest(
		    Verb.GET,
		    Game.PS2V2,
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
			    mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
				    new String[] { ((CharacterProfile) myAdapter.getItemAtPosition(myItemInt)).getCharacterId() });
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
}
