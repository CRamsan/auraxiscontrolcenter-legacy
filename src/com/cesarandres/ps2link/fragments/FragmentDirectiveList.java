package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.cesarandres.ps2link.soe.content.CharacterDirective;
import com.cesarandres.ps2link.soe.content.CharacterDirectiveObjective;
import com.cesarandres.ps2link.soe.content.CharacterDirectiveTier;
import com.cesarandres.ps2link.soe.content.CharacterDirectiveTree;
import com.cesarandres.ps2link.soe.content.DirectiveTier;
import com.cesarandres.ps2link.soe.content.DirectiveTree;
import com.cesarandres.ps2link.soe.content.DirectiveTreeCategory;
import com.cesarandres.ps2link.soe.content.Name___;
import com.cesarandres.ps2link.soe.content.response.Characters_directive_list;
import com.cesarandres.ps2link.soe.content.response.Characters_directive_objective_list;
import com.cesarandres.ps2link.soe.content.response.Characters_directive_tier_list;
import com.cesarandres.ps2link.soe.content.response.Characters_directive_tree_list;
import com.cesarandres.ps2link.soe.view.DirectiveTreeCategoryListAdapter;

/**
 * This fragment will display the directives of a given user. This fragment is
 * designed to be part of a profile pager.
 * 
 */
public class FragmentDirectiveList extends BaseFragment {

    private String profileId;
    private DirectiveTreeCategoryListAdapter adapter;
    private ExpandableListView expandableListView;
    
    private ArrayList<CharacterDirective> charactersDirective;
    private ArrayList<CharacterDirectiveObjective> charactersDirectiveObjective;
    private ArrayList<CharacterDirectiveTree> charactersDirectiveTrees;
    private ArrayList<CharacterDirectiveTier> charactersDirectiveTiers;
    private ArrayList<DirectiveTreeCategory> charactersDirectiveTreeCategories;
    
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
	this.expandableListView = (ExpandableListView) getActivity().findViewById(R.id.expandableListViewDirectiveList);
	this.adapter = new DirectiveTreeCategoryListAdapter(this, expandableListView);
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
    public void downloadDirectivesList(final String profileId) {
    	this.setProgressButton(true);
    	String url = 	"http://census.soe.com/get/ps2:v2/" + 
    					"characters_directive?character_id=" + profileId +
    					"&c:lang=en&c:limit=5000&c:join=directive";

    	Listener<Characters_directive_list> success = new Response.Listener<Characters_directive_list>() {
    	    @Override
    	    public void onResponse(Characters_directive_list response) {
    	    	charactersDirective = response.getCharacters_directive_list();
    	    	downloadDirectivesObjectives(profileId);
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
    public void downloadDirectivesObjectives(final String profileId) {
    	this.setProgressButton(true);
    	String url =	"http://census.soe.com/get/ps2:v2/" + 
						"characters_directive_objective?character_id=" + profileId +
						"&c:lang=en&c:limit=5000&c:join=objective";


    		
    	Listener<Characters_directive_objective_list> success = new Response.Listener<Characters_directive_objective_list>() {
    	    @Override
    	    public void onResponse(Characters_directive_objective_list response) {
    	    	charactersDirectiveObjective = response.getCharacters_directive_objective_list();
    	    	downloadDirectiveTrees(profileId);
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
    public void downloadDirectiveTrees(final String profileId) {
    	this.setProgressButton(true);
    	String url =	"http://census.soe.com/get/ps2:v2/" + 
    					"characters_directive_tree?character_id=" + profileId + 
    					"&c:lang=en&c:limit=5000&c:join=directive_tree"; 					
    		
    	Listener<Characters_directive_tree_list> success = new Response.Listener<Characters_directive_tree_list>(){
    		@Override
    	    public void onResponse(Characters_directive_tree_list response) {
    	    	charactersDirectiveTrees = response.getCharacters_directive_tree_list();
    	    	downloadDirectiveTiers(profileId);
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    	    }
    	};

    	SOECensus.sendGsonRequest(url, Characters_directive_tree_list.class, success, error, this);
    }
    
    /**
     * @param character_id
     *            Character id that will be used to request the list of directive tiers
     */
    public void downloadDirectiveTiers(final String profileId) {
    	this.setProgressButton(true);
    	String url =	"http://census.soe.com/get/ps2:v2/" + 
    					"characters_directive_tier?character_id=" + profileId + 
    					"&c:lang=en&c:limit=5000"; 					
    		
    	Listener<Characters_directive_tier_list> success = new Response.Listener<Characters_directive_tier_list>() {
    	    @Override
    	    public void onResponse(Characters_directive_tier_list response) {
    	    	charactersDirectiveTiers = response.getCharacters_directive_tier_list();
    	    	generateDirectiveMap();
    	    	adapter.setCategories(charactersDirectiveTreeCategories);
    	    	expandableListView.setAdapter(adapter);
        		setProgressButton(false);
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    	    }
    	};

    	SOECensus.sendGsonRequest(url, Characters_directive_tier_list.class, success, error, this);
    }
    
    public boolean generateDirectiveMap(){
    	//TODO Completely refactor this method
    	this.charactersDirectiveTreeCategories = new ArrayList<DirectiveTreeCategory>();
    	
    	HashMap<String, CharacterDirectiveTree> treeMap = new HashMap<String, CharacterDirectiveTree>();
    	HashMap<String, CharacterDirectiveTier> tierMap = new HashMap<String, CharacterDirectiveTier>();
    	HashMap<String, DirectiveTreeCategory> categoryMap = new HashMap<String, DirectiveTreeCategory>();

    	//Generate TreeMap
    	for(CharacterDirectiveTree directiveTree : charactersDirectiveTrees){
			String newCategoryId = directiveTree.getDirective_tree_id_join_directive_tree().getDirectiveTreeCategoryId();
			String newDirectiveTreeId = directiveTree.getDirective_tree_id();
			
			if(!categoryMap.containsKey(newCategoryId)){
    			Name___ name = new Name___();
    			
    			DirectiveTreeCategory newDirectiveTreeCategory = new DirectiveTreeCategory();
    			if(newCategoryId.equalsIgnoreCase("3")){
    				name.setEn("Infantry");
				}else if(newCategoryId.equalsIgnoreCase("4")){
    				name.setEn("Vehicle");
				}else if(newCategoryId.equalsIgnoreCase("5")){
    				name.setEn("Strategic");
				}else if(newCategoryId.equalsIgnoreCase("6")){
    				name.setEn("Prestige");
				}else if(newCategoryId.equalsIgnoreCase("7")){
    				name.setEn("Weapons");
				}else if(newCategoryId.equalsIgnoreCase("8")){
    				name.setEn("Events");
				}else{
    				name.setEn("Others");
				}
				newDirectiveTreeCategory.setName(name);
				newDirectiveTreeCategory.setDirectiveTreeCategoryId(newCategoryId);
				newDirectiveTreeCategory.registerCharacterDirectiveTreeList(directiveTree);
				categoryMap.put(newCategoryId, newDirectiveTreeCategory);
    			this.charactersDirectiveTreeCategories.add(newDirectiveTreeCategory);
    		}
			
			treeMap.put(newDirectiveTreeId, directiveTree);
    	}
    	
    	for(CharacterDirectiveTier directiveTier : charactersDirectiveTiers){
    		String newDirectiveTierId = directiveTier.getDirectiveTierId();
    		tierMap.put(newDirectiveTierId, directiveTier);
    		CharacterDirectiveTree parentDirectiveTree = treeMap.get(directiveTier.getDirectiveTreeId());
    		parentDirectiveTree.registerDirectiveTiers(directiveTier);
    	}
    	
    	for(CharacterDirective directive : charactersDirective){    		
    		for(int i=0; i < charactersDirectiveObjective.size(); i++){
    			if(charactersDirectiveObjective.get(i).getDirective_id().equalsIgnoreCase(directive.getDirective_id())){
    				directive.setDirectiveObjective(charactersDirectiveObjective.get(i));
    				charactersDirectiveObjective.remove(i);
    				break;
    			}
    		}
    		CharacterDirectiveTier parentDirectiveTier = tierMap.get(directive.getDirective_id_join_directive().getDirectiveTierId());
    		parentDirectiveTier.registerDirective(directive);    		
        }
    	return true;
    }
}