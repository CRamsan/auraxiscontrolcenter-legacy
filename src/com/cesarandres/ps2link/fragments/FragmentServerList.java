package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;

import android.os.AsyncTask;
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
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.World;
import com.cesarandres.ps2link.soe.content.response.Server_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
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

	ReadServerTable task = new ReadServerTable();
	setCurrentTask(task);
	task.execute();
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
    }

    /**
     * Query the API and retrieve the latest status for all the servers and
     * update the UI
     */
    public void downloadServers() {
	setProgressButton(true);
	String url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, PS2Collection.WORLD, "",
		QueryString.generateQeuryString().AddCommand(QueryCommand.LIMIT, "10")).toString();

	Listener<Server_response> success = new Response.Listener<Server_response>() {
	    @Override
	    public void onResponse(Server_response response) {
		ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
		listRoot.setAdapter(new ServerItemAdapter(getActivity(), response.getWorld_list()));
		UpdateServerTable task = new UpdateServerTable();
		setCurrentTask(task);
		setProgressButton(true);
		// TODO Check this
		task.execute(response.getWorld_list());
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
	    }
	};

	SOECensus.sendGsonRequest(url, Server_response.class, success, error, this);
    }

    /**
     * This task will read all the servers from the database, it will remove
     * servers that do not exist and it will add new ones
     * 
     */
    private class UpdateServerTable extends AsyncTask<ArrayList<World>, Integer, Boolean> {

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
	protected Boolean doInBackground(ArrayList<World>... worlds) {
	    int count = worlds[0].size();
	    ObjectDataSource data = getActivityContainer().getData();
	    int success = 0;
	    World world;
	    boolean found = false;
	    ArrayList<World> newWorlds = new ArrayList<World>(0);
	    ArrayList<World> oldWorlds = data.getAllWorlds();
	    for (int i = 0; i < count; i++) {
		world = worlds[0].get(i);
		for (int j = 0; j < oldWorlds.size(); j++) {
		    if (oldWorlds.get(j).getWorld_id().equals(world.getWorld_id())) {
			data.updateWorld(world);
			newWorlds.add(oldWorlds.get(j));
			found = true;
		    }
		}
		if (!found) {
		    data.insertWorld(world);
		}
		found = false;
	    }
	    for (int i = 0; i < newWorlds.size(); i++) {
		world = newWorlds.get(i);
		for (int j = 0; j < oldWorlds.size(); j++) {
		    if (oldWorlds.get(j).getWorld_id().equals(world.getWorld_id())) {
			oldWorlds.remove(j);
		    }
		}
	    }
	    for (int i = 0; i < oldWorlds.size(); i++) {
		data.deleteWorld(oldWorlds.get(i));
	    }
	    return success > 0;
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

    /**
     * Read and retrieve all the servers in the database
     */
    private class ReadServerTable extends AsyncTask<Integer, Integer, ArrayList<World>> {

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
	protected ArrayList<World> doInBackground(Integer... params) {
	    ObjectDataSource data = getActivityContainer().getData();
	    ArrayList<World> tmpServerList = data.getAllWorlds();
	    for (World world : tmpServerList) {
		world.setState("UNKOWN");
	    }
	    return tmpServerList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ArrayList<World> result) {
	    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
	    listRoot.setAdapter(new ServerItemAdapter(getActivity(), result));
	    setProgressButton(false);
	    downloadServers();
	}

    }
}