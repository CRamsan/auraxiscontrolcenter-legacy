package com.cesarandres.ps2link.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.dbg.DBGCensus.Verb;
import com.cesarandres.ps2link.dbg.content.Outfit;
import com.cesarandres.ps2link.dbg.content.response.Outfit_response;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.dbg.view.LoadingItemAdapter;
import com.cesarandres.ps2link.dbg.view.OutfitItemAdapter;
import com.cesarandres.ps2link.module.ButtonSelectSource;
import com.cesarandres.ps2link.module.ObjectDataSource;

/**
 * 
 * 
 * This fragment will show the user a field to search for outfits based on their
 * tag or name. The tag has the limitation that it has to be at least three
 * characters long. When an outfit is found, it's content is cached into the
 * database.
 * 
 */
public class FragmentAddOutfit extends BaseFragment {

	private Namespace lastUsedNamespace;
	
    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_add_outfit, container, false);
	new ButtonSelectSource(getActivity(), (ViewGroup) getActivity().findViewById(R.id.linearLayoutTitle));
	return view;
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
	final ImageButton buttonOutfits = (ImageButton) getActivity().findViewById(R.id.imageButtonSearchOutfit);
	buttonOutfits.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		downloadOutfits();
	    }
	});

	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		downloadOutfits();
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
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_ADD_OUTFIT);
	this.fragmentUpdate.setVisibility(View.VISIBLE);
    }

    /**
     * This method will retrieve the outfits based on the criteria given by the
     * user in the text fields. The user needs to provide a name or a tag or
     * both to start a search. If a value is provided but it is less than three
     * characters long, then the user will see a toast asking to provide more
     * information.
     */
    public void downloadOutfits() {
    this.lastUsedNamespace = DBGCensus.currentNamespace;
    	
	EditText searchField = (EditText) getActivity().findViewById(R.id.fieldSearchOutfit);
	EditText searchTagField = (EditText) getActivity().findViewById(R.id.fieldSearchTag);
	String outfitName = searchField.getText().toString().toLowerCase(Locale.getDefault());
	String outfitTag = searchTagField.getText().toString().toLowerCase(Locale.getDefault());

	// Check if the input values are valid
	if (!outfitTag.isEmpty() && outfitTag.length() < 3) {
	    Toast.makeText(getActivity(), R.string.text_tag_too_short, Toast.LENGTH_SHORT).show();
	}
	if (!outfitName.isEmpty() && outfitName.length() < 3) {
	    Toast.makeText(getActivity(), R.string.text_outfit_name_too_short, Toast.LENGTH_SHORT).show();
	}
	if (outfitName.length() < 3 && outfitTag.length() < 3) {
	    // Clear the loading adapter
	    return;
	}

	ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
	listRoot.setOnItemClickListener(null);
	// Set the loading adapter while searching
	listRoot.setAdapter(new LoadingItemAdapter(getActivity()));

	QueryString query = QueryString.generateQeuryString();
	try {
	    if (outfitTag.length() >= 3) {
		query.AddComparison("alias_lower", SearchModifier.STARTSWITH, URLEncoder.encode(outfitTag, "UTF-8"));
	    }
	    if (outfitName.length() >= 3) {
		query.AddComparison("name_lower", SearchModifier.STARTSWITH, URLEncoder.encode(outfitName, "UTF-8"));
	    }
	} catch (UnsupportedEncodingException e1) {
	    Toast.makeText(getActivity(), R.string.text_problem_encoding, Toast.LENGTH_LONG).show();
	    ((ListView) getActivity().findViewById(R.id.listFoundOutfits)).setAdapter(null);
	    return;
	}

	query.AddCommand(QueryCommand.LIMIT, "15");

	String url = DBGCensus.generateGameDataRequest(Verb.GET, PS2Collection.OUTFIT, "", query).toString();

	Listener<Outfit_response> success = new Response.Listener<Outfit_response>() {
	    @SuppressWarnings("unchecked")
	    @Override
	    public void onResponse(Outfit_response response) {
		setProgressButton(false);
		try {
		    ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
		    listRoot.setAdapter(new OutfitItemAdapter(getActivity(), response.getOutfit_list()));

		    listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
			    mCallbacks.onItemSelected(ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
				    new String[] { ((Outfit) myAdapter.getItemAtPosition(myItemInt)).getOutfit_Id(), lastUsedNamespace.name() });
			}
		    });

		    // Add the new outfits to the local cache
		    UpdateTmpOutfitTable currentTask = new UpdateTmpOutfitTable();
		    setCurrentTask(currentTask);
		    currentTask.execute(response.getOutfit_list());
		    listRoot.setTextFilterEnabled(true);
		} catch (Exception e) {
		    Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
		}
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
		if (listRoot != null) {
		    listRoot.setAdapter(null);
		}
		Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
	    }
	};

	DBGCensus.sendGsonRequest(url, Outfit_response.class, success, error, this);
    }

    /**
     * 
     * 
     * This task will add the searched outfits to database. All outfits are
     * added to the database for the first time with the Temp flag set.
     * 
     */
    private class UpdateTmpOutfitTable extends AsyncTask<ArrayList<Outfit>, Integer, Boolean> {

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
	protected Boolean doInBackground(ArrayList<Outfit>... outfits) {
	    int count = outfits[0].size();
	    ArrayList<Outfit> list = outfits[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    Outfit outfit = null;
	    for (int i = 0; i < count; i++) {
		outfit = data.getOutfit(list.get(i).getOutfit_Id());
		// If outfit is not in cache
		if (outfit == null) {
		    data.insertOutfit(list.get(i), true);
		} else {
		    // If not, update the record
		    if (outfit.isCached()) {
			data.updateOutfit(list.get(i), false);
		    } else {
			data.updateOutfit(list.get(i), true);
		    }
		}
	    }
	    return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Boolean result) {
	    setProgressButton(false);
	}
    }
}
