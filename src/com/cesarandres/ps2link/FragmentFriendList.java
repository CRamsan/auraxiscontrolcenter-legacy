package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterFriend;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.response.Character_friend_list_response;
import com.cesarandres.ps2link.soe.content.response.Character_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.view.FriendItemAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentFriendList extends Fragment {

	private String profileId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_friend_list, container, false);

		ListView listRoot = (ListView) root.findViewById(R.id.listViewFriendList);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityContainerSingle.class);
				intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_PROFILE.toString());
				intent.putExtra("profileId", ((CharacterFriend) myAdapter.getItemAtPosition(myItemInt)).getId());
				startActivity(intent);
			}
		});
		
		this.profileId = getActivity().getIntent().getExtras().getString("profileId");
		
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.text_friends));
		ImageButton updateButton = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Set update method
				// new ReadProfilesTable().execute();
			}
		});
		downloadOutfitMembers(this.profileId);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ApplicationPS2Link.volley.cancelAll(this);
	}

	private void setUpdateButton(boolean enabled) {
		getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(enabled);
	}

	private void downloadOutfitMembers(String character_id) {

		setUpdateButton(false);
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2, PS2Collection.CHARACTERS_FRIEND, character_id, QueryString.generateQeuryString()
					.AddCommand(QueryCommand.RESOLVE, "character_name"));

			Listener<Character_friend_list_response> success = new Response.Listener<Character_friend_list_response>() {
				@Override
				public void onResponse(Character_friend_list_response response) {
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewFriendList);
					listRoot.setAdapter(new FriendItemAdapter(getActivity(), response.getCharacters_friend_list().get(0).getFriend_list()));

					listRoot.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), ActivityContainerSingle.class);
							intent.putExtra("profileId", ((CharacterFriend) myAdapter.getItemAtPosition(myItemInt)).getCharacted_id());
							intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_PROFILE.toString());
							startActivity(intent);
						}
					});
				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
					setUpdateButton(true);
				}
			};

			GsonRequest<Character_friend_list_response> gsonOject = new GsonRequest<Character_friend_list_response>(url.toString(),
					Character_friend_list_response.class, null, success, error);
			gsonOject.setTag(this);
			ApplicationPS2Link.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}