package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

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
import com.cesarandres.ps2link.dbg.content.Member;
import com.cesarandres.ps2link.dbg.content.response.Outfit_member_response;
import com.cesarandres.ps2link.dbg.view.OnlineMemberItemAdapter;

/**
 * This fragment will do a request to retrieve all members for the given outfit
 * and resolve the class they are using. This is very useful to show who is
 * online and display their current class
 * 
 */
public class FragmentMembersOnline extends BaseFragment {

    private String outfitId;
    private String outfitName;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_member_list, container, false);
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

	if (savedInstanceState == null) {
	    this.outfitId = getArguments().getString("PARAM_0");
	} else {
	    this.outfitId = savedInstanceState.getString("outfitId");
	}
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewMemberList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
	    }
	});

	this.fragmentTitle.setText(outfitName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	downloadOutfitMembers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	savedInstanceState.putString("outfitId", outfitId);
    }

    /**
     * Do a request to retrieve all the members of the given outfit with their
     * classes already resolved
     */
    public void downloadOutfitMembers() {
	setProgressButton(true);
	String url = DBGCensus.generateGameDataRequest("outfit_member?c:limit=10000&c:resolve=online_status,character(name,battle_rank,profile_id)&c:join=type:profile^list:0^inject_at:profile^show:name.en^on:character.profile_id^to:profile_id&outfit_id=" + this.outfitId).toString();
	
	Listener<Outfit_member_response> success = new Response.Listener<Outfit_member_response>() {
	    @Override
	    public void onResponse(Outfit_member_response response) {
		setProgressButton(false);
		try{
		    updateContent(response.getOutfit_member_list());
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

	DBGCensus.sendGsonRequest(url, Outfit_member_response.class, success, error, this);
    }

    /**
     * @param members
     *            An array list with all the members found. The adapter will
     *            retrieve all online members and it will only display those
     */
    private void updateContent(ArrayList<Member> members) {
	ListView listRoot = (ListView) getView().findViewById(R.id.listViewMemberList);

	listRoot.setAdapter(new OnlineMemberItemAdapter(members, getActivity()));
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
	    }
	});
    }
}
