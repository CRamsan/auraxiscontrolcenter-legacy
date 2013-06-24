package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.response.Character_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.volley.GsonRequest;
import com.google.gson.Gson;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentAddProfile extends BaseFragment implements OnClickListener {

	public interface NameToSearchListener {
		void onProfileSelected(CharacterProfile profile);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_add_profile, container,
				false);

		ListView listRoot = (ListView) root
				.findViewById(R.id.listFoundProfiles);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityProfile.class);
				intent.putExtra("profile", new Gson().toJson(myAdapter
						.getItemAtPosition(myItemInt)));
				startActivity(intent);
			}
		});

		final Button buttonCharacters = (Button) root
				.findViewById(R.id.buttonSearchProfile);
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				@SuppressWarnings("unused")
				EditText searchField = (EditText) getActivity().findViewById(
						R.id.fieldSearchProfile);
				URL url;
				try {
					url = SOECensus.generateGameDataRequest(
							Verb.GET,
							Game.PS2,
							PS2Collection.CHARACTER,
							"",
							QueryString
									.generateQeuryString()
									.AddComparison("name.first_lower",
											SearchModifier.STARTSWITH,
											searchField.getText().toString())
									.AddCommand(QueryCommand.LIMIT, "10"));

					Listener<Character_response> success = new Response.Listener<Character_response>() {
						@Override
						public void onResponse(Character_response response) {
							ListView listRoot = (ListView) getActivity()
									.findViewById(R.id.listFoundProfiles);
							listRoot.setAdapter(new ProfileItemAdapter(
									getActivity(), response.getCharacter_list()));
							listRoot.setTextFilterEnabled(true);

						}
					};

					ErrorListener error = new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							error.equals(new Object());
						}
					};

					GsonRequest<Character_response> gsonOject = new GsonRequest<Character_response>(
							url.toString(), Character_response.class, null,
							success, error);
					ActivityContainer.volley.add(gsonOject);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		((TextView)root.findViewById(R.id.textViewFragmentTitle)).setText("Profiles Found");
		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}

	private static class ProfileItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<CharacterProfile> charactersList;

		public ProfileItemAdapter(Context context,
				List<CharacterProfile> charactersList) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.charactersList = new ArrayList<CharacterProfile>(
					charactersList);
		}

		@Override
		public int getCount() {
			return this.charactersList.size();
		}

		@Override
		public CharacterProfile getItem(int position) {
			return this.charactersList.get(position);
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
				holder.characterName = (TextView) convertView
						.findViewById(R.id.textViewProfileCharacterName);
				holder.battleRank = (TextView) convertView
						.findViewById(R.id.textViewBattleRank);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			if (this.charactersList.get(position).getFaction_id()
					.equals(Faction.VS)) {
				holder.faction.setImageResource(R.drawable.vs);
			} else if (this.charactersList.get(position).getFaction_id()
					.equals(Faction.NC)) {
				holder.faction.setImageResource(R.drawable.nc);
			} else if (this.charactersList.get(position).getFaction_id()
					.equals(Faction.TR)) {
				holder.faction.setImageResource(R.drawable.tr);
			}

			holder.characterName.setText(this.charactersList.get(position)
					.getName().getFirst());
			holder.battleRank.setText(Integer.toString(this.charactersList
					.get(position).getBattle_rank().getValue()));

			return convertView;
		}

		static class ViewHolder {
			ImageView faction;
			TextView characterName;
			TextView battleRank;
		}

	}
}
