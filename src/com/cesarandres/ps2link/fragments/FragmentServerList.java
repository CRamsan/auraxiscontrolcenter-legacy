package com.cesarandres.ps2link.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.cesarandres.ps2link.dbg.DBGCensus.Verb;
import com.cesarandres.ps2link.dbg.content.response.Server_Status_response;
import com.cesarandres.ps2link.dbg.content.response.Server_response;
import com.cesarandres.ps2link.dbg.content.response.server.LiveServers;
import com.cesarandres.ps2link.dbg.util.Collections.PS2Collection;
import com.cesarandres.ps2link.dbg.util.QueryString;
import com.cesarandres.ps2link.dbg.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.dbg.view.ServerItemAdapter;
import com.cesarandres.ps2link.module.ButtonSelectSource;

/**
 * This fragment will display the servers and theirs status
 */
public class FragmentServerList extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_server_list, container, false);
	new ButtonSelectSource(getActivity(), (ViewGroup) getActivity().findViewById(R.id.linearLayoutTitle));
	return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	this.fragmentTitle.setText(getString(R.string.title_servers));
	this.fragmentUpdate.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		downloadServers();
	    }
	});

	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
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
	this.fragmentUpdate.setVisibility(View.VISIBLE);
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_SERVER_LIST);
	downloadServers();
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    public void downloadServers() {
	setProgressButton(true);
	String url = DBGCensus.generateGameDataRequest(Verb.GET, PS2Collection.WORLD, "",
		QueryString.generateQeuryString().AddCommand(QueryCommand.LIMIT, "10")).toString();

	Listener<Server_response> success = new Response.Listener<Server_response>() {
	    @Override
	    public void onResponse(Server_response response) {
		try {
		    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
		    listRoot.setAdapter(new ServerItemAdapter(getActivity(), response.getWorld_list()));
		} catch (Exception e) {
		    Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
		}
		setProgressButton(true);
		downloadServerPopulation();
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
		Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
	    }
	};
	DBGCensus.sendGsonRequest(url, Server_response.class, success, error, this);
    }

    /**
     * Query the API and retrieve the latest population info for all the servers
     * and update the UI. This method uses a non-standard API call. This API
     * call has been unreliable in the past.
     */
    public void downloadServerPopulation() {
	setProgressButton(true);
	// This is not an standard API call
	String url = "http://census.daybreakgames.com/s:PS2Link/json/status/ps2";

	Listener<Server_Status_response> success = new Response.Listener<Server_Status_response>() {
	    @Override
	    public void onResponse(Server_Status_response response) {
		setProgressButton(false);
		try {
		    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
		    LiveServers servers = response.getPs2().getLive();
		    ((ServerItemAdapter) listRoot.getAdapter()).setServerPopulation(servers);
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

	DBGCensus.sendGsonRequest(url, Server_Status_response.class, success, error, this);
    }
}