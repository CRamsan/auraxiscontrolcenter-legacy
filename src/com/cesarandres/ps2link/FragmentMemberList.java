package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_member_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.volley.GsonRequest;
import com.google.gson.Gson;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMemberList extends BaseFragment {

	private String outfit_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		this.outfit_id = getActivity().getIntent().getExtras()
				.getString("outfit_id");

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
				// startActivity(intent);
			}
		});

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
											this.outfit_id)
									.AddCommand(QueryCommand.RESOLVE,
											"member_online_status,member,member_character(name,type.faction)"));

			Listener<Outfit_member_response> success = new Response.Listener<Outfit_member_response>() {
				@Override
				public void onResponse(Outfit_member_response response) {
					ListView listRoot = (ListView) getActivity().findViewById(
							R.id.listViewMemberList);
					listRoot.setAdapter(new memberItemAdapter(getActivity(),
							response.getOutfit_list().get(0).getMembers()));
					listRoot.setTextFilterEnabled(true);

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

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private static class memberItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Member> memberList;

		public memberItemAdapter(Context context, List<Member> memberList) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.memberList = new ArrayList<Member>(memberList);
		}

		@Override
		public int getCount() {
			return this.memberList.size();
		}

		@Override
		public Member getItem(int position) {
			return this.memberList.get(position);
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
						.inflate(R.layout.member_item_list, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.memberName = (TextView) convertView
						.findViewById(R.id.textViewMemberListName);
				holder.memberSince = (TextView) convertView
						.findViewById(R.id.textViewMemberListSince);
				holder.memberStatus = (TextView) convertView
						.findViewById(R.id.textViewMemberListStatus);
				holder.memberRank = (TextView) convertView
						.findViewById(R.id.textViewMemberListRank);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			holder.memberName.setText(this.memberList.get(position).getName()
					.getFirst());
			holder.memberRank.setText(this.memberList.get(position).getRank());
			holder.memberSince.setText(this.memberList.get(position)
					.getMember_since());

			holder.memberStatus.setText(this.memberList.get(position)
					.getOnline_status());
			return convertView;
		}

		static class ViewHolder {
			TextView memberName;
			TextView memberSince;
			TextView memberStatus;
			TextView memberRank;
		}

	}
}
