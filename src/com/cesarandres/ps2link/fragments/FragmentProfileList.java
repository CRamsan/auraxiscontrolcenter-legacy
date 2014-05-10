package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.cesarandres.ps2link.ActivityContainer;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.view.ProfileItemAdapter;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfileList extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// Inflate the layout for this fragment
	View root = inflater.inflate(R.layout.fragment_profile_list, container, false);

	ListView listRoot = (ListView) root.findViewById(R.id.listViewProfileList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterProfile) myAdapter.getItemAtPosition(myItemInt)).getCharacterId() });
	    }
	});
	return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	this.fragmentTitle.setText(getString(R.string.title_profiles));
	this.fragmentUpdate.setVisibility(View.VISIBLE);
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		currentTask = new ReadProfilesTable();
		((ReadProfilesTable) currentTask).execute();
	    }
	});

	this.fragmentAdd.setVisibility(View.VISIBLE);
	this.fragmentAdd.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_ADD_PROFILE.toString(), null);
	    }
	});

    }

    @Override
    public void onPause() {
	super.onPause();
    }

    @Override
    public void onResume() {
	super.onResume();
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_PROFILE_LIST);
	currentTask = new ReadProfilesTable();
	((ReadProfilesTable) currentTask).execute();
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
    }

    private class ReadProfilesTable extends AsyncTask<Integer, Integer, ArrayList<CharacterProfile>> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected ArrayList<CharacterProfile> doInBackground(Integer... params) {
	    ArrayList<CharacterProfile> tmpProfileList = null;
	    try {
		ObjectDataSource data = getActivityContainer().getData();
		tmpProfileList = data.getAllCharacterProfiles(false);
	    } catch (Exception e) {
		return null;
	    }
	    return tmpProfileList;
	}

	@Override
	protected void onPostExecute(ArrayList<CharacterProfile> result) {
	    if (result != null) {
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewProfileList);
		listRoot.setAdapter(new ProfileItemAdapter(getActivity(), result, true));
	    }
	    setProgressButton(false);
	}
    }
}