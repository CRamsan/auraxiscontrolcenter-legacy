package com.cesarandres.ps2link.fragments;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Verb;
import com.cesarandres.ps2link.dbg.content.Faction;
import com.cesarandres.ps2link.dbg.content.Outfit;
import com.cesarandres.ps2link.dbg.content.response.Outfit_response;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.Logger;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * This fragment will read the outfit from the database to display it to the user. Then a network 
 * request will be made to get updated information.
 */
public class FragmentOutfit extends BaseFragment {

    private boolean isCached;
    private String outfitId;
    private Outfit outfit;

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
	UpdateOutfitFromTable task = new UpdateOutfitFromTable();
	setCurrentTask(task);
	this.outfitId = getArguments().getString("PARAM_0");
	task.execute(this.outfitId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_outfit, container, false);
    }

    /**
     * @param character
     *            Character that contains all the data to populate the UI
     */
	private void updateUI(final Outfit outfit) {
	this.fragmentTitle.setText(outfit.getName());
	try {
	    if (this.getView() != null) {
	    	TextView outfitName = ((TextView) getActivity().findViewById(R.id.textViewFragmentOutfitName));
	    	outfitName.setText("[" + outfit.getAlias() + "] " + outfit.getName());
    	
	    	TextView outfitSize = ((TextView) getActivity().findViewById(R.id.textViewMembersText));
	    	outfitSize.setText(Integer.toString(outfit.getMember_count()));
	    		    	
	    	TextView outfitCreation = ((TextView) getActivity().findViewById(R.id.TextViewOutfitCreationText));
	    	Date date = new Date(Long.parseLong(outfit.getTime_created()) * 1000);
	    	
	    	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());	    	
	    	outfitCreation.setText(df.format(date));	    	
	    	
	    	Button leaderButton = (Button)getActivity().findViewById(R.id.buttonOutfitToLeader);

	    	if(outfit.getLeader() != null){
		    	ImageView faction = ((ImageView) getActivity().findViewById(R.id.imageViewOutfitFaction));
				if (outfit.getLeader().getFaction_id().equals(Faction.VS)) {
				    faction.setImageResource(R.drawable.icon_faction_vs);
				} else if (outfit.getLeader().getFaction_id().equals(Faction.NC)) {
				    faction.setImageResource(R.drawable.icon_faction_nc);
				} else if (outfit.getLeader().getFaction_id().equals(Faction.TR)) {
				    faction.setImageResource(R.drawable.icon_faction_tr);
				}

	    		leaderButton.setText(outfit.getLeader().getName().getFirst());
	    	}
	    	
		    if (outfit.getLeader_character_id() != null) {
		    	leaderButton.setEnabled(true);
		    	leaderButton.setAlpha(1);
		    	leaderButton.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
				    	mCallbacks.onItemSelected(ActivityMode.ACTIVITY_PROFILE.toString(), new String[] {
				    		outfit.getLeader_character_id(),
				    		outfit.getNamespace().name()});
					}
				});	
		    }
	    	
	    }
	    
	    this.fragmentStar.setOnCheckedChangeListener(null);
	    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
	    String preferedOutfitId = settings.getString("preferedOutfit", "");
	    if (preferedOutfitId.equals(outfit.getOutfit_Id())) {
		this.fragmentStar.setChecked(true);
	    } else {
		this.fragmentStar.setChecked(false);
	    }

	    this.fragmentStar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		    SharedPreferences.Editor editor = settings.edit();
		    if (isChecked) {
			editor.putString("preferedOutfit", outfit.getOutfit_Id());
			editor.putString("preferedOutfitName", outfit.getName());
			editor.putString("preferedOutfitNamespace", DBGCensus.currentNamespace.name());
		    } else {
			editor.putString("preferedOutfitName", "");
			editor.putString("preferedOutfit", "");
			editor.putString("preferedOutfitNamespace", "");
		    }
		    editor.commit();
		    getActivityContainer().checkPreferedButtons();
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
	} catch (NullPointerException e) {
	    Logger.log(Log.ERROR, this, "Null Pointer while trying to set character data on UI");
	}
    }

    /**
     * @param character_id
     *            Character id of the character that wants to be download
     */
    public void downloadOutfit(String outfitId) {
	this.setProgressButton(true);
	String url = DBGCensus.generateGameDataRequest(
		Verb.GET,
		PS2Collection.OUTFIT,
		outfitId,
		QueryString.generateQeuryString().AddCommand(QueryCommand.RESOLVE, "leader")).toString();
	Listener<Outfit_response> success = new Response.Listener<Outfit_response>() {
	    @Override
	    public void onResponse(Outfit_response response) {
		setProgressButton(false);
		try {
		    outfit = response.getOutfit_list().get(0);
		    outfit.setNamespace(DBGCensus.currentNamespace);
		    outfit.setCached(isCached);
		    updateUI(outfit);
		    UpdateOutfitToTable task = new UpdateOutfitToTable();
		    setCurrentTask(task);
		    task.execute(outfit);
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

	DBGCensus.sendGsonRequest(url, Outfit_response.class, success, error, this);
    }

    /**
     * Read the profile from the database and update the UI
     */
    private class UpdateOutfitFromTable extends AsyncTask<String, Integer, Outfit> {

	private String outfit_id;

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
	    this.outfit_id = args[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    Outfit outfit = data.getOutfit(this.outfit_id);
	    if (outfit == null) {
		isCached = false;
	    } else {
		isCached = outfit.isCached();
	    }
	    return outfit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Outfit result) {
	    setProgressButton(false);
	    if (result == null) {
	    	downloadOutfit(this.outfit_id);
	    } else {
	    	outfit = result;
	    	updateUI(result);
	    	downloadOutfit(result.getOutfit_Id());
	    }
	}
    }

    /**
     * Save the profile to the database
     */
    private class UpdateOutfitToTable extends AsyncTask<Outfit, Integer, Outfit> {

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
	protected Outfit doInBackground(Outfit... args) {
		Outfit outfit = null;
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
	    outfit = args[0];
		if(data.getOutfit(outfitId) != null){
			data.updateOutfit(outfit, !outfit.isCached());
		}else{
			data.insertOutfit(outfit, !outfit.isCached());
		}
		
	    } catch (Exception e) {

	    }
	    return outfit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Outfit result) {
	    setProgressButton(false);
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
	    isCached = !temp;
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
		updateUI(outfit);
	    }
	    setProgressButton(false);
	}
    }
}
