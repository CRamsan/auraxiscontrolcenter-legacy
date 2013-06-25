package com.cesarandres.ps2link;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentAddOutfit extends BaseFragment implements OnClickListener {

	public interface NameToSearchListener {
		void onoutfitSelected(Outfit outfit);
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
		View root = inflater.inflate(R.layout.fragment_add_outfit, container,
				false);

		ListView listRoot = (ListView) root.findViewById(R.id.listFoundOutfits);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityMermberList.class);
				intent.putExtra("outfit_id", ((Outfit) myAdapter
						.getItemAtPosition(myItemInt)).getId());
				startActivity(intent);
			}
		});

		final ImageButton buttonOutfits = (ImageButton) root
				.findViewById(R.id.imageButtonSearchOutfit);
		buttonOutfits.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				@SuppressWarnings("unused")
				EditText searchField = (EditText) getActivity().findViewById(
						R.id.fieldSearchOutfit);
				URL url;
				try {
					url = SOECensus.generateGameDataRequest(
							Verb.GET,
							Game.PS2,
							PS2Collection.OUTFIT,
							"",
							QueryString
									.generateQeuryString()
									.AddComparison(
											"name",
											SearchModifier.STARTSWITH,
											URLEncoder.encode(searchField
													.getText().toString(),
													"UTF-8"))
									.AddCommand(QueryCommand.LIMIT, "10"));

					Listener<Outfit_response> success = new Response.Listener<Outfit_response>() {
						@Override
						public void onResponse(Outfit_response response) {
							ListView listRoot = (ListView) getActivity()
									.findViewById(R.id.listFoundOutfits);
							listRoot.setAdapter(new outfitItemAdapter(
									getActivity(), response.getOutfit_list()));
							listRoot.setTextFilterEnabled(true);

						}
					};

					ErrorListener error = new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							error.equals(new Object());
						}
					};

					GsonRequest<Outfit_response> gsonOject = new GsonRequest<Outfit_response>(
							url.toString(), Outfit_response.class, null,
							success, error);
					ApplicationPS2Link.volley.add(gsonOject);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		((TextView)root.findViewById(R.id.textViewFragmentTitle)).setText("Outfits Found");
		
		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}

	private static class outfitItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<Outfit> outfitList;

		public outfitItemAdapter(Context context, List<Outfit> outfitList) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.outfitList = new ArrayList<Outfit>(outfitList);
		}

		@Override
		public int getCount() {
			return this.outfitList.size();
		}

		@Override
		public Outfit getItem(int position) {
			return this.outfitList.get(position);
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
						.inflate(R.layout.outfit_item_list, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.outfitName = (TextView) convertView
						.findViewById(R.id.textViewOutfitName);
				holder.outfitAlias = (TextView) convertView
						.findViewById(R.id.textViewOutfitAlias);
				holder.memberCount = (TextView) convertView
						.findViewById(R.id.textViewOutfitCount);
				holder.serverName = (TextView) convertView
						.findViewById(R.id.textViewOutfitServer);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			holder.outfitName.setText(this.outfitList.get(position).getName());
			holder.memberCount.setText(this.outfitList.get(position)
					.getMember_count());
			holder.outfitAlias
					.setText(this.outfitList.get(position).getAlias());

			holder.serverName.setText(this.outfitList.get(position)
					.getWorld_id());
			return convertView;
		}

		static class ViewHolder {
			TextView outfitName;
			TextView outfitAlias;
			TextView memberCount;
			TextView serverName;
		}

	}
}
