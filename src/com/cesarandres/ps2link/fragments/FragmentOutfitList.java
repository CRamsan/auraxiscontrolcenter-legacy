package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.content.Outfit;
import com.cesarandres.ps2link.dbg.view.OutfitItemAdapter;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * This fragment will read and display all the outfits that have been set as
 * non-temporary in the database.
 * 
 */
public class FragmentOutfitList extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_outfit_list, container, false);
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
	this.fragmentTitle.setText(getString(R.string.title_outfits));
	this.fragmentAdd.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_ADD_OUTFIT.toString(), null);
	    }
	});
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		new ReadOutfitsTable().execute();
	    }
	});
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewOutfitList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
			new String[] { ((Outfit) myAdapter.getItemAtPosition(myItemInt)).getOutfit_Id() });
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
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_OUTFIT_LIST);
	this.fragmentAdd.setVisibility(View.VISIBLE);
	this.fragmentUpdate.setVisibility(View.VISIBLE);
	ReadOutfitsTable task = new ReadOutfitsTable();
	setCurrentTask(task);
	task.execute();
    }

    /**
     * This task will read all outfits that have not been set as temporary in
     * the database
     * 
     */
    private class ReadOutfitsTable extends AsyncTask<Integer, Integer, ArrayList<Outfit>> {

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
	protected ArrayList<Outfit> doInBackground(Integer... params) {
	    ArrayList<Outfit> outfitList = new ArrayList<Outfit>();
	    ObjectDataSource data = getActivityContainer().getData();
	    outfitList = data.getAllOutfits(false);
	    data.deleteAllOutfit(false);
	    return outfitList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ArrayList<Outfit> result) {
	    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewOutfitList);
	    listRoot.setAdapter(new OutfitItemAdapter(getActivity(), result));
	    setProgressButton(false);
	}
    }
}
