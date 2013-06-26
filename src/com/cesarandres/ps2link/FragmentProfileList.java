package com.cesarandres.ps2link;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfileList extends BaseFragment {

	public static Bitmap vs_icon;
	public static Bitmap nc_icon;
	public static Bitmap tr_icon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		vs_icon = BitmapFactory.decodeResource(getActivity().getResources(),
				R.drawable.vs_icon);
		tr_icon = BitmapFactory.decodeResource(getActivity().getResources(),
				R.drawable.tr_icon);
		nc_icon = BitmapFactory.decodeResource(getActivity().getResources(),
				R.drawable.nc_icon);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_profile_list, container,
				false);

		new ReadProfilesTable().execute();

		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("List of Profiles");

		Button updateButton = (Button) root
				.findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});

		Button searchButton = (Button) root
				.findViewById(R.id.buttonFragmentAdd);
		searchButton.setVisibility(View.VISIBLE);

		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityAddProfile.class);
				startActivity(intent);
			}
		});

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_profile_list_menu, menu);
	}

	private class ReadProfilesTable extends
			AsyncTask<Integer, Integer, ArrayList<CharacterProfile>> {

		@Override
		protected ArrayList<CharacterProfile> doInBackground(Integer... params) {
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			ArrayList<CharacterProfile> tmpServerList = data
					.getAllCharacterProfiles();
			data.close();
			return tmpServerList;
		}

		@Override
		protected void onPostExecute(ArrayList<CharacterProfile> result) {
			Toast.makeText(getActivity(), "Used profiles from DB",
					Toast.LENGTH_SHORT).show();
			ListView listRoot = (ListView) getActivity().findViewById(
					R.id.listViewProfileList);
			listRoot.setAdapter(new ProfileItemAdapter(getActivity(), result));
		}

	}

	private static class ProfileItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<CharacterProfile> profileList;

		public ProfileItemAdapter(Context context,
				List<CharacterProfile> profileList) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.profileList = new ArrayList<CharacterProfile>(profileList);
		}

		@Override
		public int getCount() {
			return this.profileList.size();
		}

		@Override
		public CharacterProfile getItem(int position) {
			return this.profileList.get(position);
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
				convertView = mInflater.inflate(R.layout.profile_item_list,
						null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.faction = (ImageView) convertView
						.findViewById(R.id.imageViewFaction);
				holder.battleRank = (TextView) convertView
						.findViewById(R.id.textViewBattleRank);
				holder.name = (TextView) convertView
						.findViewById(R.id.textViewProfileCharacterName);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			if (this.profileList.get(position).getFaction_id()
					.equals(Faction.VS)) {
				holder.faction.setImageBitmap(vs_icon);
			} else if (this.profileList.get(position).getFaction_id()
					.equals(Faction.NC)) {
				holder.faction.setImageBitmap(nc_icon);
			} else if (this.profileList.get(position).getFaction_id()
					.equals(Faction.TR)) {
				holder.faction.setImageBitmap(tr_icon);
			}

			holder.name.setText(this.profileList.get(position).getName()
					.getFirst());
			holder.battleRank.setText(Integer.toString(this.profileList
					.get(position).getBattle_rank().getValue()));

			return convertView;
		}

		static class ViewHolder {
			ImageView faction;
			TextView battleRank;
			TextView name;
		}
	}
}
