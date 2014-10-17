package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
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
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_member_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.view.MemberItemAdapter;

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
	    UpdateOutfitFromTable task = new UpdateOutfitFromTable();
	    this.outfitId = getArguments().getString("PARAM_0");
	    setCurrentTask(task);
	    task.execute(this.outfitId);
	} else {
	    this.outfitSize = savedInstanceState.getInt("outfitSize", 0);
	    this.outfitId = savedInstanceState.getString("outfitId");
	    this.outfitName = savedInstanceState.getString("outfitName");
	    this.shownOffline = savedInstanceState.getBoolean("showOffline");
	}

	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewMemberList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
	    }
	});

	this.fragmentShowOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		shownOffline = isChecked;
		updateContent();
	    }
	});

	this.fragmentStar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (outfitId != null && outfitName != null) {
		    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		    SharedPreferences.Editor editor = settings.edit();
		    if (isChecked) {
			editor.putString("preferedOutfit", outfitId);
			editor.putString("preferedOutfitName", outfitName);
		    } else {
			editor.putString("preferedOutfit", "");
			editor.putString("preferedOutfitName", "");
		    }
		    editor.commit();
		    getActivityContainer().checkPreferedButtons();
		}
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
	String url = SOECensus.generateGameDataRequest(
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
		    fragmentTitle.setText(response.getOutfit_list().get(0).getName());
		    ArrayList<Member> list = response.getOutfit_list().get(0).getMembers();
		    // Check this warning
		    task.execute(list);
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
	SOECensus.sendGsonRequest(url, Outfit_member_response.class, success, error, this);
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
			new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
	    }
	});

	this.fragmentAppend.setOnCheckedChangeListener(null);
	this.fragmentAppend.setChecked(isCached);
	this.fragmentAppend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
		    CacheOutfit task = new CacheOutfit();
		    setCurrentTask(task);
		    task.execute(outfitId, "true");
		} else {
		    CacheOutfit task = new CacheOutfit();
		    setCurrentTask(task);
		    task.execute(outfitId, "false");
		}
	    }
	});

	SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
	String preferedOutfitId = settings.getString("preferedOutfit", "");
	if (preferedOutfitId.equals(outfitId)) {
	    this.fragmentStar.setChecked(true);
	} else {
	    this.fragmentStar.setChecked(false);
	}
    }

    /**
     * 
     * 
     * This Async task will read the members of the outfit and some other outfit
     * information from the database. This is useful to show the user with some
     * information while the network calls are retrieving more up to date
     * information
     * 
     */
    private class UpdateOutfitFromTable extends AsyncTask<String, Integer, Outfit> {

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
	protected Outfit doInBackground(String... args) {
	    Outfit outfit = null;
	    ObjectDataSource data = getActivityContainer().getData();
	    outfit = data.getOutfit(args[0]);
	    isCached = outfit.isCached();
	    return outfit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Outfit result) {
	    outfitId = result.getOutfit_Id();
	    outfitName = result.getName();
	    outfitSize = result.getMember_count();
	    setProgressButton(false);
	    updateContent();
	    downloadOutfitMembers();
	}
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

    /**
     * This task will set an outfit as temp or not. The first argument needs to
     * be the outfit_id and the second a string with true or false, true will
     * save the outfit and display it on the outfit list, false will save the
     * outfit in the databse but it will not be displayed on the outfit list.
     * 
     */
    private class CacheOutfit extends AsyncTask<String, Integer, Integer> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected Integer doInBackground(String... args) {
	    ObjectDataSource data = getActivityContainer().getData();
	    Outfit outfit = data.getOutfit(args[0]);
	    boolean temp = !Boolean.parseBoolean(args[1]);
	    data.updateOutfit(outfit, temp);
	    isCached = true;
	    return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Integer result) {
	    if (isCached) {
		updateContent();
	    }
	    setProgressButton(false);
	}
    }
}
