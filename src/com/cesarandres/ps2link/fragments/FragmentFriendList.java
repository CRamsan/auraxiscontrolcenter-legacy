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
import com.cesarandres.ps2link.soe.content.CharacterFriend;
import com.cesarandres.ps2link.soe.content.response.Character_friend_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.view.FriendItemAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentFriendList extends BaseFragment {

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
	root = inflater.inflate(R.layout.fragment_friend_list, container, false);
	ListView listRoot = (ListView) root.findViewById(R.id.listViewFriendList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterFriend) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
	    }
	});

	this.profileId = getArguments().getString("PARAM_0");

	return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
	super.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();
	downloadFriendsList(this.profileId);
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
    }


    public void downloadFriendsList(String character_id) {
	setProgressButton(true);
	URL url;
	try {
	    url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, PS2Collection.CHARACTERS_FRIEND, null, QueryString.generateQeuryString()
		    .AddComparison("character_id", SearchModifier.EQUALS, character_id).AddCommand(QueryCommand.RESOLVE, "character_name"));

	    Listener<Character_friend_list_response> success = new Response.Listener<Character_friend_list_response>() {
		@Override
		public void onResponse(Character_friend_list_response response) {
		    try {
			ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewFriendList);
			listRoot.setAdapter(new FriendItemAdapter(getActivity(), response.getCharacters_friend_list().get(0).getFriend_list()));
			setProgressButton(false);
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

	    GsonRequest<Character_friend_list_response> gsonOject = new GsonRequest<Character_friend_list_response>(url.toString(),
		    Character_friend_list_response.class, null, success, error);
	    gsonOject.setTag(this);
	    ApplicationPS2Link.volley.add(gsonOject);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}