package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.response.Characters_directive_list;
import com.cesarandres.ps2link.soe.content.response.Characters_directive_objective_list;
import com.cesarandres.ps2link.soe.content.response.Directive_tree_list;
import com.cesarandres.ps2link.soe.view.DirectiveCategoryTreeListAdapter;

/**
 * This fragment will display the directives of a given user. This fragment is
 * designed to be part of a profile pager.
 * 
 */
public class FragmentDirectiveList extends BaseFragment {

    private String profileId;
    private DirectiveCategoryTreeListAdapter adapter;
    
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
	this.adapter = new DirectiveCategoryTreeListAdapter(this, (ExpandableListView) getActivity().findViewById(R.id.expandableListViewDirectiveList),profileId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	downloadDirectivesList(this.profileId);
    }

    /**
     * @param character_id
     *            Character id that will be used to request the list of directives
     */
    public void downloadDirectivesList(String profileId) {
    	this.setProgressButton(true);
    	String url = 	"http://census.soe.com/get/ps2:v2/" + 
    					"characters_directive?character_id=" + profileId +
    					"&c:lang=en&c:limit=5000&c:join=directive";

    	Listener<Characters_directive_list> success = new Response.Listener<Characters_directive_list>() {
    	    @Override
    	    public void onResponse(Characters_directive_list response) {
    		setProgressButton(false);
    		try {

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    	    }
    	};

    	SOECensus.sendGsonRequest(url, Characters_directive_list.class, success, error, this);
    }
    
    /**
     * @param character_id
     *            Character id that will be used to request the list of objectives
     */
    public void downloadDirectivesObjectives(String profileId) {
    	this.setProgressButton(true);
    	String url =	"http://census.soe.com/get/ps2:v2/" + 
						"characters_directive_objective?character_id=" + profileId +
						"&c:lang=en&c:limit=5000&c:join=objective";


    		
    	Listener<Characters_directive_objective_list> success = new Response.Listener<Characters_directive_objective_list>() {
    	    @Override
    	    public void onResponse(Characters_directive_objective_list response) {
    		setProgressButton(false);
    		try {

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    	    }
    	};

    	SOECensus.sendGsonRequest(url, Characters_directive_objective_list.class, success, error, this);
    }
    
    /**
     * @param character_id
     *            Character id that will be used to request the list of directive trees
     */
    public void downloadDirectiveTrees(String profileId) {
    	this.setProgressButton(true);
    	String url =	"http://census.soe.com/get/ps2:v2/" + 
    					"characters_directive_tree?character_id=" + profileId + 
    					"&c:lang=en&c:limit=5000&c:join=directive_tree"; 					
    		
    	Listener<Directive_tree_list> success = new Response.Listener<Directive_tree_list>() {
    	    @Override
    	    public void onResponse(Directive_tree_list response) {
    		setProgressButton(false);
    		try {

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    	    }
    	};

    	SOECensus.sendGsonRequest(url, Directive_tree_list.class, success, error, this);
    }
    
    /**
     * @param character_id
     *            Character id that will be used to request the list of directive tiers
     */
    public void downloadDirectiveTiers(String profileId) {
    	this.setProgressButton(true);
    	String url =	"http://census.soe.com/get/ps2:v2/" + 
    					"characters_directive_tree?character_id=" + profileId + 
    					"&c:lang=en&c:limit=5000&c:join=directive_tree"; 					
    		
    	Listener<Directive_tree_list> success = new Response.Listener<Directive_tree_list>() {
    	    @Override
    	    public void onResponse(Directive_tree_list response) {
    		setProgressButton(false);
    		try {

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    	    }
    	};

    	SOECensus.sendGsonRequest(url, Directive_tree_list.class, success, error, this);
    }
}