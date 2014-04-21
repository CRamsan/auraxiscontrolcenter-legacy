package com.cesarandres.ps2link.fragments;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ActivityContainer;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.view.LoadingItemAdapter;
import com.cesarandres.ps2link.soe.view.OutfitItemAdapter;

/**
 * @author Cesar Ramirez
 * 
 *         This fragment will show the user a field to search for outfits based
 *         on their tag or name. The tag has the limitation that it has to be at
 *         least three characters long. When an outfit is found, it's content is
 *         cached into the database.
 * 
 */
public class FragmentAddOutfit extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// Inflate the layout for this fragment
	View root = inflater.inflate(R.layout.fragment_add_outfit, container, false);
	return root;
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
	((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_outfits));
	final ImageButton buttonOutfits = (ImageButton) getActivity().findViewById(R.id.imageButtonSearchOutfit);
	buttonOutfits.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
		listRoot.setOnItemClickListener(null);
		listRoot.setAdapter(new LoadingItemAdapter(getActivity()));
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
	ImageButton fragmentUpdate = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
	ToggleButton showOffline = (ToggleButton) getActivity().findViewById(R.id.toggleButtonShowOffline);
	ImageButton fragmentAdd = (ImageButton) getActivity().findViewById(R.id.buttonFragmentAdd);
	ToggleButton fragmentStar = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentStar);
	ToggleButton fragmentAppend = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentAppend);

	fragmentUpdate.setVisibility(View.GONE);
	showOffline.setVisibility(View.GONE);
	fragmentAdd.setVisibility(View.GONE);
	fragmentStar.setVisibility(View.GONE);
	fragmentAppend.setVisibility(View.GONE);

	fragmentUpdate.setEnabled(true);
	showOffline.setEnabled(true);
	fragmentAdd.setEnabled(true);
	fragmentStar.setEnabled(true);
	fragmentAppend.setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onPause()
     */
    @Override
    public void onPause() {
	super.onPause();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onStop()
     */
    @Override
    public void onStop() {
	ApplicationPS2Link.volley.cancelAll(this);
	super.onStop();

    }

    /**
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * 
     */
    public void downloadOutfits() {
	EditText searchField = (EditText) getActivity().findViewById(R.id.fieldSearchOutfit);
	EditText searchTagField = (EditText) getActivity().findViewById(R.id.fieldSearchTag);
	String outfitName = searchField.getText().toString().toLowerCase();
	String outfitTag = searchTagField.getText().toString().toLowerCase();

	if (!outfitTag.isEmpty() && outfitTag.length() < 3) {
	    Toast.makeText(getActivity(), "Tag is too short.", Toast.LENGTH_SHORT).show();
	}
	if (!outfitName.isEmpty() && outfitName.length() < 3) {
	    Toast.makeText(getActivity(), "Outfit name is too short.", Toast.LENGTH_SHORT).show();
	}
	if (outfitName.length() < 3 && outfitTag.length() < 3) {
	    ((ListView) getActivity().findViewById(R.id.listFoundOutfits)).setAdapter(null);
	    return;
	}

	QueryString query = QueryString.generateQeuryString();
	try {
	    if (outfitTag.length() >= 3) {
		query.AddComparison("alias_lower", SearchModifier.STARTSWITH, URLEncoder.encode(outfitTag, "UTF-8"));
	    }
	    if (outfitName.length() >= 3) {
		query.AddComparison("name_lower", SearchModifier.STARTSWITH, URLEncoder.encode(outfitName, "UTF-8"));
	    }
	} catch (UnsupportedEncodingException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	query.AddCommand(QueryCommand.LIMIT, "15");

	String url = "";
	try {
	    url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, PS2Collection.OUTFIT, "", query).toString();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Listener<Outfit_response> success = new Response.Listener<Outfit_response>() {
	    @Override
	    public void onResponse(Outfit_response response) {
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
		listRoot.setAdapter(new OutfitItemAdapter(getActivity(), response.getOutfit_list()));

		listRoot.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
			mCallbacks.onItemSelected(ActivityMode.ACTIVITY_MEMBER_LIST.toString(),
				new String[] { ((Outfit) myAdapter.getItemAtPosition(myItemInt)).getOutfit_Id() });
		    }
		});

		new UpdateTmpOutfitTable().execute(response.getOutfit_list());
		listRoot.setTextFilterEnabled(true);

	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		error.equals(new Object());
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
		if (listRoot != null) {
		    listRoot.setAdapter(null);
		}
	    }
	};

	SOECensus.sendGsonRequest(url, Outfit_response.class, success, error, this);
    }

    /**
     * @author Cesar Ramirez
     * 
     *         This task will add the searched outfits to database. If the
     *         outfit has just been is a new record it will be kept with the
     *         'temporary' flag.
     * 
     */
    private class UpdateTmpOutfitTable extends AsyncTask<ArrayList<Outfit>, Integer, Boolean> {
	@Override
	protected Boolean doInBackground(ArrayList<Outfit>... outfits) {
	    int count = outfits[0].size();
	    ArrayList<Outfit> list = outfits[0];
	    ObjectDataSource data = ((ActivityContainer) getActivity()).getData();
	    Outfit outfit = null;
	    for (int i = 0; i < count; i++) {
		outfit = data.getOutfit(list.get(i).getOutfit_Id());
		if (outfit == null) {
		    data.insertOutfit(list.get(i), true);
		} else {
		    if (outfit.isCached()) {
			data.updateOutfit(list.get(i), false);
		    } else {
			data.updateOutfit(list.get(i), true);
		    }
		}
	    }
	    return true;
	}
    }
}
