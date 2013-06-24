package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.World;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.WorldLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
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
import com.google.gson.Gson;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentServerList extends BaseFragment implements
		LoaderManager.LoaderCallbacks<World> {

	public static final int SQL_READER = 235;

	private ArrayList<World> serverList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_server_list, container,
				false);

		ListView listRoot = (ListView) root.findViewById(R.id.listViewServers);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityServer.class);
				intent.putExtra("server", new Gson().toJson(myAdapter
						.getItemAtPosition(myItemInt)));
				startActivity(intent);
			}
		});

		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("List of servers");

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(SQL_READER, null, this);
	}

	public void downloadServers() {
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2,
					PS2Collection.WORLD, "", QueryString.generateQeuryString()
							.AddCommand(QueryCommand.LIMIT, "10"));

			Listener<Server_response> success = new Response.Listener<Server_response>() {
				@Override
				public void onResponse(Server_response response) {
					ListView listRoot = (ListView) getActivity().findViewById(
							R.id.listViewServers);
					listRoot.setAdapter(new ServerItemAdapter(getActivity(),
							response.getWorld_list()));
					listRoot.setTextFilterEnabled(true);

				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
				}
			};
			GsonRequest<Server_response> gsonOject = new GsonRequest<Server_response>(
					url.toString(), Server_response.class, null, success, error);
			gsonOject.setTag(this);
			ActivityContainer.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readServers() {
		ObjectDataSource data = new ObjectDataSource(getActivity());
		data.open();
		this.serverList = data.getAllWorlds();
		data.close();
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
				convertView = mInflater
						.inflate(R.layout.server_item_list, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.status = (ImageView) convertView
						.findViewById(R.id.imageViewServerListStatus);
				holder.serverName = (TextView) convertView
						.findViewById(R.id.textViewServerListName);
				holder.serverRegion = (TextView) convertView
						.findViewById(R.id.textViewServerListRegion);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			if (this.serverList.get(position).getState().equals("online")) {
				holder.status.setImageResource(R.drawable.vs);
			} else {
				holder.status.setImageResource(R.drawable.tr);
			}

			holder.serverName.setText(this.serverList.get(position).getName()
					.getEn());
			holder.serverRegion.setText("Not implemented yet");

			return convertView;
		}

		static class ViewHolder {
			ImageView status;
			TextView serverName;
			TextView serverRegion;
		}

	}

	@Override
	public Loader<World> onCreateLoader(int arg0, Bundle arg1) {
		return new WorldLoader(getActivity(), baseUri,
				CONTACTS_SUMMARY_PROJECTION, select, null,
				Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
	}

	@Override
	public void onLoadFinished(Loader<World> arg0, World arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<World> arg0) {
		// TODO Auto-generated method stub

	}
}
