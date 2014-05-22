package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.response.Server_Status_response;
import com.cesarandres.ps2link.soe.content.response.server.LiveServer;
import com.cesarandres.ps2link.soe.content.response.server.LiveServers;
import com.cesarandres.ps2link.soe.view.ServerItemAdapter;

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
	return inflater.inflate(R.layout.fragment_server_list, container, false);
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
     * Query the API and retrieve the latest status for all the servers and
     * update the UI. This method uses a non-standard API call
     */
    public void downloadServers() {
	setProgressButton(true);
	//This is not an standard API call
	String url = "https://census.soe.com/s:PS2Link/json/status/ps2";

	Listener<Server_Status_response> success = new Response.Listener<Server_Status_response>() {
	    @Override
	    public void onResponse(Server_Status_response response) {
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
		LiveServers servers = response.getPs2().getLive();
		ArrayList<LiveServer> serverList = new ArrayList<LiveServer>();
		servers.getBriggs().setName("Briggs");
		servers.getCeres().setName("Ceres");
		servers.getCobalt().setName("Cobalt");
		servers.getConnery().setName("Connery");
		servers.getMattherson().setName("Mattherson");
		servers.getMiller().setName("Miller");
		servers.getWaterson().setName("Waterson");
		servers.getWoodman().setName("Woodman");
		serverList.add(servers.getBriggs());
		serverList.add(servers.getCeres());
		serverList.add(servers.getCobalt());
		serverList.add(servers.getConnery());
		serverList.add(servers.getMattherson());
		serverList.add(servers.getMiller());
		serverList.add(servers.getWaterson());
		serverList.add(servers.getWoodman());
		listRoot.setAdapter(new ServerItemAdapter(getActivity(), serverList));
		setProgressButton(false);
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
	    }
	};

	SOECensus.sendGsonRequest(url, Server_Status_response.class, success, error, this);
	}
}