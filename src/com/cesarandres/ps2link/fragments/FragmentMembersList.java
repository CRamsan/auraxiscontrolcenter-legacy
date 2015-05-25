package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
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
import com.cesarandres.ps2link.dbg.content.Member;
import com.cesarandres.ps2link.dbg.content.Outfit;
import com.cesarandres.ps2link.dbg.content.response.Outfit_member_response;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.dbg.view.MemberItemAdapter;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * Fargment that will retrieve and display all the members of an outfit in
 * alphabetical order. This fragment allows to display online member or all
 * members.
 * 
 */
public class FragmentMembersList extends BaseFragment {

    private boolean isCached;
    private boolean shownOffline = false;;
    private int outfitSize;
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

	// Check if outfit data has already been loaded
	if (savedInstanceState == null) {
	    this.outfitId = getArguments().getString("PARAM_0");
	} else {
	    this.outfitSize = savedInstanceState.getInt("outfitSize", 0);
	    this.outfitId = savedInstanceState.getString("outfitId");
	    this.outfitName = savedInstanceState.getString("outfitName");
	    this.shownOffline = savedInstanceState.getBoolean("showOffline");
	}

	this.fragmentShowOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		shownOffline = isChecked;
		updateContent();
	    }
	});
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
	savedInstanceState.putInt("outfitSize", outfitSize);
	savedInstanceState.putString("outfitId", outfitId);
	savedInstanceState.putString("outfitName", outfitName);
	savedInstanceState.putBoolean("showOffline", shownOffline);
    }

    /**
     * This method will retrieve the members for the given outfit_id and it will
     * start a task to cache that data
     */
    public void downloadOutfitMembers() {
	setProgressButton(true);
	String url = DBGCensus.generateGameDataRequest(
		Verb.GET,
		PS2Collection.OUTFIT,
		"",
		QueryString.generateQeuryString().AddComparison("outfit_id", SearchModifier.EQUALS, this.outfitId)
			.AddCommand(QueryCommand.RESOLVE, "member_online_status,member,member_character(name,type.faction)")).toString();

	Listener<Outfit_member_response> success = new Response.Listener<Outfit_member_response>() {
	    @SuppressWarnings("unchecked")
		@Override
	    public void onResponse(Outfit_member_response response) {
		setProgressButton(false);
		try {
		    UpdateMembers task = new UpdateMembers();
		    setCurrentTask(task);
		    ArrayList<Member> list = response.getOutfit_list().get(0).getMembers();
		    // Check this warning
		    task.execute(list);
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
	DBGCensus.sendGsonRequest(url, Outfit_member_response.class, success, error, this);
    }
    
    /**
     * This method will set the adapter that will read outfit members from the
     * database
     */
    private void updateContent() {
	ListView listRoot = (ListView) getView().findViewById(R.id.listViewMemberList);
	ObjectDataSource data = getActivityContainer().getData();
	listRoot.setAdapter(new MemberItemAdapter(getActivity(), outfitId, data, shownOffline));

	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { 	((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id(),
							DBGCensus.currentNamespace.name()});
	    }
	});

    }
    
    /**
     * This Async task will replace the old member information with new one. The
     * process will remove all previous members in the outfit and write the new
     * ones. This task can take a long time, specially on big outfits and on old
     * devices
     * 
     */
    private class UpdateMembers extends AsyncTask<ArrayList<Member>, Integer, Integer> {

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
	protected Integer doInBackground(ArrayList<Member>... members) {
	    ArrayList<Member> newMembers = members[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    Outfit outfit = data.getOutfit(outfitId);
	    outfit.setMember_count(newMembers.size());
	    data.updateOutfit(outfit, !outfit.isCached());

	    data.deleteAllMembers(outfitId);
	    for (Member member : newMembers) {
		data.insertMember(member, outfitId, !isCached);
		if (isCancelled()) {
		    return null;
		}
	    }
	    return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Integer result) {
	    setProgressButton(false);
	    updateContent();
	}
    }
}
