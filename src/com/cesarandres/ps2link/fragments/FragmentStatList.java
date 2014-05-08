package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
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
import com.cesarandres.ps2link.soe.content.StatItemAdapter;
import com.cesarandres.ps2link.soe.content.character.Stats;
import com.cesarandres.ps2link.soe.content.response.Character_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentStatList extends BaseFragment{

    private String profileId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);

	// Inflate the layout for this fragment
	View root;
	root = inflater.inflate(R.layout.fragment_stat_list, container, false);
	this.profileId = getArguments().getString("PARAM_0");

	return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	downloadStatList(this.profileId);
    }

    @Override
    public void onPause() {
	super.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();

	fragmentUpdate.setVisibility(View.VISIBLE);
	fragmentStar.setVisibility(View.VISIBLE);
	fragmentAppend.setVisibility(View.VISIBLE);
    }

    public void downloadStatList(String character_id) {
	setProgressButton(true);
	URL url;
	try {
	    url = SOECensus.generateGameDataRequest(
		    Verb.GET,
		    Game.PS2V2,
		    PS2Collection.CHARACTER,
		    character_id,
		    QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "stat_history")
			    .AddCommand(QueryCommand.HIDE, "name,battle_rank,certs,times,daily_ribbon"));
	    Listener<Character_list_response> success = new Response.Listener<Character_list_response>() {
		@Override
		public void onResponse(Character_list_response response) {
		    try {
			ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewStatList);
			CharacterProfile profile = response.getCharacter_list().get(0);
			Stats stats = profile.getStats();
			listRoot.setAdapter(new StatItemAdapter(getActivity(), stats.getStat_history(), profileId));
		    } catch (Exception e) {
			Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
		    }
		    setProgressButton(false);
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
}