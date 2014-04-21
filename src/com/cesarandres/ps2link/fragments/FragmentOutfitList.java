package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

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

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.ActivityContainer;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.view.OutfitItemAdapter;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentOutfitList extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);
	// Inflate the layout for this fragment
	View root = inflater.inflate(R.layout.fragment_outfit_list, container, false);

	ListView listRoot = (ListView) root.findViewById(R.id.listViewOutfitList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
			new String[] { ((Outfit) myAdapter.getItemAtPosition(myItemInt)).getOutfit_Id() });
	    }
	});

	return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	this.fragmentTitle.setText(getString(R.string.title_outfits));
	this.fragmentAdd.setVisibility(View.VISIBLE);
	this.fragmentAdd.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		mCallbacks.onItemSelected(ActivityMode.ACTIVITY_ADD_OUTFIT.toString(), null);
	    }
	});

	this.fragmentUpdate.setVisibility(View.VISIBLE);
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		new ReadOutfitsTable().execute();
	    }
	});

    }

    @Override
    public void onResume() {
	super.onResume();
	new ReadOutfitsTable().execute();
    }

    @Override
    public void onPause() {
	super.onPause();
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
    }

    private class ReadOutfitsTable extends AsyncTask<Integer, Integer, ArrayList<Outfit>> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected ArrayList<Outfit> doInBackground(Integer... params) {
	    ArrayList<Outfit> outfitList = new ArrayList<Outfit>();
	    try {
		ObjectDataSource data = ((ActivityContainer) getActivity()).getData();
		outfitList = data.getAllOutfits(false);
	    } finally {
	    }
	    return outfitList;
	}

	@Override
	protected void onPostExecute(ArrayList<Outfit> result) {
	    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewOutfitList);
	    listRoot.setAdapter(new OutfitItemAdapter(getActivity(), result));
	    setProgressButton(false);
	}
    }
}
