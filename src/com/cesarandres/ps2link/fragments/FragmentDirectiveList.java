package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.view.DirectiveListAdapter;

/**
 * This fragment will display the directives of a given user. This fragment is
 * designed to be part of a profile pager.
 * 
 */
public class FragmentDirectiveList extends BaseFragment {

    private String profileId;
    private DirectiveListAdapter adapter;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_directives_list, container, false);
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
	this.profileId = getArguments().getString("PARAM_0");
	this.adapter = new DirectiveListAdapter(this, (ExpandableListView) getActivity().findViewById(R.id.expandableListViewDirectiveList),profileId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	downloadDirectivesList();
    }

    /**
     * @param character_id
     *            Character id that will be used to request the list of directives
     */
    public void downloadDirectivesList() {
    	this.adapter.downloadDirectivesList();
    }
}