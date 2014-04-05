package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ActivityContainerSingle;
import com.cesarandres.ps2link.ApplicationPS2Link;
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
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentServerList extends BaseFragment {

	public static final int SQL_READER = 235;
	private ArrayList<AsyncTask> taskList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskList = new ArrayList<AsyncTask>();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_server_list, container, false);

		ListView listRoot = (ListView) root.findViewById(R.id.listViewServers);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityContainerSingle.class);
				intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_MAP.toString());
				//startActivity(intent);
			}
		});

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_servers));
		ImageButton updateButton = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);
		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				downloadServers();
			}
		});

		new ReadServerTable().execute();
	}

	@Override
	public void onStop() {
		super.onStop();
		ApplicationPS2Link.volley.cancelAll(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.toggleButtonShowOffline).setVisibility(View.GONE);
		getActivity().findViewById(R.id.buttonFragmentAdd).setVisibility(View.GONE);
		getActivity().findViewById(R.id.toggleButtonFragmentStar).setVisibility(View.GONE);
		getActivity().findViewById(R.id.toggleButtonFragmentAppend).setVisibility(View.GONE);
	}
	
	public void downloadServers() {
		setUpdateButton(false);
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, PS2Collection.WORLD, "",
					QueryString.generateQeuryString().AddCommand(QueryCommand.LIMIT, "10"));

			Listener<Server_response> success = new Response.Listener<Server_response>() {
				@Override
				public void onResponse(Server_response response) {
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
					listRoot.setAdapter(new ServerItemAdapter(getActivity(), response.getWorld_list()));
					new UpdateServerTable().execute(response.getWorld_list());
				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
					setUpdateButton(true);
				}
			};
			GsonRequest<Server_response> gsonOject = new GsonRequest<Server_response>(url.toString(), Server_response.class, null, success, error);
			gsonOject.setTag(this);
			ApplicationPS2Link.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setUpdateButton(boolean enabled) {
		getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(enabled);
		if (enabled) {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.GONE);
		} else {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.GONE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.VISIBLE);
		}
	}

	private static class ServerItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<World> serverList;

		public ServerItemAdapter(Context context, List<World> serverList) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.serverList = new ArrayList<World>(serverList);
		}

		@Override
		public int getCount() {
			return this.serverList.size();
		}

		@Override
		public World getItem(int position) {
			return this.serverList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.layout_server_item, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.serverstatus = (TextView) convertView.findViewById(R.id.textViewServerStatus);
				holder.serverName = (TextView) convertView.findViewById(R.id.textViewServerListName);
				holder.serverRegion = (TextView) convertView.findViewById(R.id.textViewServerListRegion);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			String serverState = this.serverList.get(position).getState();
			if (serverState.equals("online")) {
				holder.serverstatus.setText(serverState.toUpperCase());
				holder.serverstatus.setTextColor(Color.GREEN);
			} else {
				holder.serverstatus.setText(serverState.toUpperCase());
				holder.serverstatus.setTextColor(Color.RED);
			}

			String name = this.serverList.get(position).getName().getEn();

			if (name.equals("Briggs")) {
				holder.serverRegion.setText("(AU)");
			} else if (name.equals("Waterson") || name.equals("Mattherson")) {
				holder.serverRegion.setText("(US EAST)");
			} else if (name.equals("Connery")) {
				holder.serverRegion.setText("(US WEST)");
			} else if (name.equals("Mallory") || name.equals("Ceres") || name.equals("Miller") || name.equals("Cobalt") || name.equals("Woodman")) {
				holder.serverRegion.setText("(EU)");
			} else {
				holder.serverRegion.setText("");
			}

			holder.serverName.setText(name);

			return convertView;
		}

		static class ViewHolder {

			TextView serverstatus;
			TextView serverName;
			TextView serverRegion;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		for (AsyncTask task : taskList) {
			task.cancel(true);
		}
	}

	private class UpdateServerTable extends AsyncTask<ArrayList<World>, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			taskList.add(this);
			setUpdateButton(false);
		}

		@Override
		protected Boolean doInBackground(ArrayList<World>... worlds) {
			int count = worlds[0].size();
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
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
			data.close();
			return success > 0;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!this.isCancelled()) {
				setUpdateButton(true);
			}
			taskList.remove(this);
		}
	}

	private class ReadServerTable extends AsyncTask<Integer, Integer, ArrayList<World>> {

		@Override
		protected void onPreExecute() {
			taskList.add(this);
			setUpdateButton(false);
		}

		@Override
		protected ArrayList<World> doInBackground(Integer... params) {
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			ArrayList<World> tmpServerList = data.getAllWorlds();
			data.close();
			return tmpServerList;
		}

		@Override
		protected void onPostExecute(ArrayList<World> result) {
			if (!this.isCancelled()) {
				if (result.size() == 0) {
					downloadServers();
				} else {
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewServers);
					listRoot.setAdapter(new ServerItemAdapter(getActivity(), result));
					downloadServers();
				}
				setUpdateButton(true);
			}
			taskList.remove(this);
		}

	}

}