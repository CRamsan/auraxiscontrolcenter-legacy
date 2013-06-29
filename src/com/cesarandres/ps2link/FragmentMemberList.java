package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_member_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMemberList extends BaseFragment {

	private static boolean isCached;
	private Outfit outfit;
	private ObjectDataSource data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		data = new ObjectDataSource(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_member_list, container,
				false);

		ListView listRoot = (ListView) root
				.findViewById(R.id.listViewMemberList);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityProfile.class);
				intent.putExtra("member_id", ((Member) myAdapter
						.getItemAtPosition(myItemInt)).getCharacter_id());
				startActivity(intent);
			}
		});

		Button updateButton = (Button) root
				.findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});

		root.findViewById(R.id.buttonFragmentRemoveContact).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						new UnCacheOutfit().execute(outfit.getId());
					}
				});

		root.findViewById(R.id.buttonFragmentAddContact).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						new CacheOutfit().execute(outfit.getId());
					}
				});

		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("List of Members");

		return root;
	}

	@Override
	public void onResume() {
		data.open();
		new UpdateOutfitFromTable().execute(getActivity().getIntent()
				.getExtras().getString("outfit_id"));
		super.onResume();
	}

	@Override
	public void onPause() {
		data.close();
		super.onPause();
	}

	private void downloadOutfitMembers(String outfit_id) {

		RequestQueue volley = Volley.newRequestQueue(getActivity());
		URL url;
		try {
			url = SOECensus
					.generateGameDataRequest(
							Verb.GET,
							Game.PS2,
							PS2Collection.OUTFIT,
							"",
							QueryString
									.generateQeuryString()
									.AddComparison("id", SearchModifier.EQUALS,
											outfit_id)
									.AddCommand(QueryCommand.RESOLVE,
											"member_online_status,member,member_character(name,type.faction)"));

			Listener<Outfit_member_response> success = new Response.Listener<Outfit_member_response>() {
				@Override
				public void onResponse(Outfit_member_response response) {
					new UpdateMembers().execute(response.getOutfit_list()
							.get(0).getMembers());

				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
				}
			};

			GsonRequest<Outfit_member_response> gsonOject = new GsonRequest<Outfit_member_response>(
					url.toString(), Outfit_member_response.class, null,
					success, error);
			volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class memberItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Cursor cursor;
		private int size;

		/*
		 * private int size; private int cacheFirst; private int cacheSize;
		 * private ArrayList<Member> cacheA; private ArrayList<Member> cacheB;
		 */

		public memberItemAdapter(Context context, Outfit outfitView,
				ObjectDataSource data, boolean isCache) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.size = outfitView.getMember_count();
			this.cursor = data.getMembersCursor(outfitView.getId(), !isCache);
			/*
			 * this.size = size; this.cacheFirst = 0; this.cacheSize = 2 *
			 * this.size; this.cacheA = new ArrayList<Member>(size); this.cacheB
			 * = new ArrayList<Member>(size);
			 */
		}

		@Override
		public int getCount() {
			return this.size;
		}

		@Override
		public Member getItem(int position) {
			return ObjectDataSource.cursorToMember(ObjectDataSource
					.cursorToPosition(cursor, position));
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.member_item_list, null);

				holder = new ViewHolder();
				holder.memberName = (TextView) convertView
						.findViewById(R.id.textViewMemberListName);
				holder.memberStatus = (TextView) convertView
						.findViewById(R.id.textViewMemberListStatus);
				holder.memberRank = (TextView) convertView
						.findViewById(R.id.textViewMemberListRank);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.memberName.setText(getItem(position).getName().getFirst());
			holder.memberRank.setText(getItem(position).getRank());
			if (getItem(position).getOnline_status().equals("0")) {
				holder.memberStatus.setText("Offline");
			} else {
				holder.memberStatus.setText("Online");
			}

			return convertView;
		}

		static class ViewHolder {
			TextView memberName;
			TextView memberStatus;
			TextView memberRank;
		}
	}

	private void updateUI() {
		if (isCached) {
			getActivity().findViewById(R.id.buttonFragmentRemoveContact)
					.setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.buttonFragmentAddContact)
					.setVisibility(View.GONE);
		} else {
			getActivity().findViewById(R.id.buttonFragmentRemoveContact)
					.setVisibility(View.GONE);
			getActivity().findViewById(R.id.buttonFragmentAddContact)
					.setVisibility(View.VISIBLE);
		}
	}

	private void updateContent() {
		ListView listRoot = (ListView) getActivity().findViewById(
				R.id.listViewMemberList);
		listRoot.setAdapter(new memberItemAdapter(getActivity(), outfit, data,
				isCached));

	}

	private class UpdateOutfitFromTable extends
			AsyncTask<String, Integer, Outfit> {
		@Override
		protected Outfit doInBackground(String... args) {
			Outfit outfit = data.getOutfit(args[0], false);
			if (outfit == null) {
				outfit = data.getOutfit(args[0], true);
				isCached = false;
			} else {
				isCached = true;
			}
			return outfit;
		}

		@Override
		protected void onPostExecute(Outfit result) {
			outfit = result;
			updateUI();
			downloadOutfitMembers(outfit.getId());
		}
	}

	private class UpdateMembers extends
			AsyncTask<ArrayList<Member>, Integer, Integer> {
		@Override
		protected Integer doInBackground(ArrayList<Member>... members) {
			Member member;
			boolean found = false;
			int count = members[0].size();
			ArrayList<Member> newMembers = new ArrayList<Member>(0);
			ArrayList<Member> oldMembers = data.getAllMembers(outfit.getId(),
					!isCached);
			/*
			 * if (oldMembers.size() == 0) { for (int i = 0; i < count; i++) {
			 * data.insertMember(members[0].get(i), !isCached); } }
			 */
			for (int i = 0; i < count; i++) {
				member = members[0].get(i);
				for (int j = 0; j < oldMembers.size(); j++) {
					if (oldMembers.get(j).getCharacter_id()
							.equals(member.getCharacter_id())) {
						data.updateMember(member, !isCached);
						newMembers.add(oldMembers.get(j));
						found = true;
					}
				}
				if (!found) {
					data.insertMember(member, outfit.getId(), !isCached);
				}
				found = false;
			}
			for (int i = 0; i < newMembers.size(); i++) {
				member = newMembers.get(i);
				for (int j = 0; j < oldMembers.size(); j++) {
					if (oldMembers.get(j).getCharacter_id()
							.equals(member.getCharacter_id())) {
						oldMembers.remove(j);
					}
				}
			}
			for (int i = 0; i < oldMembers.size(); i++) {
				data.deleteMember(oldMembers.get(i), !isCached);
			}

			return data.countAllMembers(outfit.getId(), !isCached);
		}

		@Override
		protected void onPostExecute(Integer result) {
			updateContent();
		}
	}

	private class CacheOutfit extends AsyncTask<String, Integer, Integer> {
		@Override
		protected Integer doInBackground(String... args) {
			Outfit outfit = data.getOutfit(args[0], true);
			data.insertOutfit(outfit, false);
			data.deleteOutfit(outfit, true);

			ArrayList<Member> members = data.getAllMembers(args[0], true);
			data.insertAllMembers(members, args[0], false);
			for (Member delMember : members) {
				data.deleteMember(delMember, true);
			}
			isCached = true;
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			updateContent();
			updateUI();
		}
	}

	private class UnCacheOutfit extends AsyncTask<String, Integer, Integer> {
		@Override
		protected Integer doInBackground(String... args) {
			Outfit outfit = data.getOutfit(args[0], false);
			data.insertOutfit(outfit, true);
			data.deleteOutfit(outfit, false);

			ArrayList<Member> members = data.getAllMembers(args[0], false);
			data.insertAllMembers(members, args[0], true);
			for (Member delMember : members) {
				data.deleteMember(delMember, false);
			}
			isCached = false;
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			updateContent();
			updateUI();
		}
	}

}
