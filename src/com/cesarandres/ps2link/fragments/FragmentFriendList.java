package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Verb;
import com.cesarandres.ps2link.dbg.content.CharacterFriend;
import com.cesarandres.ps2link.dbg.content.response.Character_friend_list_response;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.dbg.view.FriendItemAdapter;

/**
 * This fragment will display the friends of a given user. This fragment is
 * designed to be part of a profile pager.
 * 
 */
public class FragmentFriendList extends BaseFragment {

    private String profileId;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_friend_list, container, false);
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
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewFriendList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterFriend) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id(),
							DBGCensus.currentNamespace.name()});
	    }
	});

	this.profileId = getArguments().getString("PARAM_0");

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	downloadFriendsList(this.profileId);
    }

    /**
     * @param character_id
     *            Character id that will be used to request the list of friends
     */
    public void downloadFriendsList(String character_id) {
	setProgressButton(true);
	String url = DBGCensus.generateGameDataRequest(
		Verb.GET,
		PS2Collection.CHARACTERS_FRIEND,
		null,
		QueryString.generateQeuryString().AddComparison("character_id", SearchModifier.EQUALS, character_id)
			.AddCommand(QueryCommand.RESOLVE, "character_name")).toString();

	Listener<Character_friend_list_response> success = new Response.Listener<Character_friend_list_response>() {
	    @Override
	    public void onResponse(Character_friend_list_response response) {
		setProgressButton(false);
		try {
		    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewFriendList);
		    listRoot.setAdapter(new FriendItemAdapter(getActivity(), response.getCharacters_friend_list().get(0).getFriend_list()));
		} catch (Exception e) {
		    Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
		}
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
		Toast.makeText(getActivity(), R.string.toast_error_retrieving_data, Toast.LENGTH_SHORT).show();
	    }
	};

	DBGCensus.sendGsonRequest(url, Character_friend_list_response.class, success, error, this);
    }
}