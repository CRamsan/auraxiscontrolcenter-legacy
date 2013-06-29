package com.cesarandres.ps2link;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.view.OutfitItemAdapter;
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
							listRoot.setAdapter(new OutfitItemAdapter(
									getActivity(), response.getOutfit_list()));
							new UpdateTmpOutfitTable().execute(response
									.getOutfit_list());
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

		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("Outfits Found");

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}

	private class UpdateTmpOutfitTable extends
			AsyncTask<ArrayList<Outfit>, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(ArrayList<Outfit>... outfits) {
			int count = outfits[0].size();
			ArrayList<Outfit> list = outfits[0];
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			for (int i = 0; i < count; i++) {
				if (data.getOutfit(list.get(i).getId(), true) == null) {
					data.insertOutfit(list.get(i), true);
				} else {
					data.updateOutfit(list.get(i), true);
				}
			}
			data.close();
			return true;
		}
	}
}
