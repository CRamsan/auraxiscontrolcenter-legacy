package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.response.World_Event_response;
import com.cesarandres.ps2link.soe.view.AlertItemAdapter;

/**
 * This fragment will retrieve the current list of alerts as well as give the
 * user the option to register for push notifications.
 * 
 */
public class FragmentAlertList extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_alert_list, container, false);
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
	this.fragmentTitle.setText(getString(R.string.title_alerts));
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		downloadAlertList();
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
	downloadAlertList();
    }

    @Override
    public void onPause() {
	super.onPause();
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewAlertList);
	((AlertItemAdapter) listRoot.getAdapter()).stopWorker();
    }

    /**
     * It will download the latest alerts and populate the UI
     */
    public void downloadAlertList() {
	setProgressButton(true);
	String url = "http://census.soe.com/get/ps2:v2/world_event?c:limit=20&c:lang=en&type=METAGAME&c:join=type:world^inject_at:world&c:join=type:metagame_event^inject_at:event";

	Listener<World_Event_response> success = new Response.Listener<World_Event_response>() {
	    @Override
	    public void onResponse(World_Event_response response) {
		setProgressButton(false);
		try {
		    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewAlertList);
		    listRoot.setAdapter(new AlertItemAdapter(getActivity(), response.getWorld_event_list()));
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

	SOECensus.sendGsonRequest(url, World_Event_response.class, success, error, this);
    }
}