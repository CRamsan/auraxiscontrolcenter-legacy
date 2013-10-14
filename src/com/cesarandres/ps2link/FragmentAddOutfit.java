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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
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
import com.cesarandres.ps2link.soe.view.LoadingItemAdapter;
import com.cesarandres.ps2link.soe.view.OutfitItemAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentAddOutfit extends Fragment implements OnClickListener {

	public interface NameToSearchListener {
		void onoutfitSelected(Outfit outfit);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_add_outfit, container, false);

		ApplicationPS2Link.volley.cancelAll(this);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_outfits));
		final ImageButton buttonOutfits = (ImageButton) getActivity().findViewById(R.id.imageButtonSearchOutfit);
		buttonOutfits.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
				listRoot.setOnItemClickListener(null);
				listRoot.setAdapter(new LoadingItemAdapter(getActivity()));
				downloadOutfits();
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		ApplicationPS2Link.volley.cancelAll(this);
		super.onStop();

	}

	public void downloadOutfits() {
		EditText searchField = (EditText) getActivity().findViewById(R.id.fieldSearchOutfit);
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(
					Verb.GET,
					Game.PS2V2,
					PS2Collection.OUTFIT,
					"",
					QueryString.generateQeuryString()
							.AddComparison("name", SearchModifier.STARTSWITH, URLEncoder.encode(searchField.getText().toString(), "UTF-8"))
							.AddCommand(QueryCommand.LIMIT, "10"));

			Listener<Outfit_response> success = new Response.Listener<Outfit_response>() {
				@Override
				public void onResponse(Outfit_response response) {
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
					listRoot.setAdapter(new OutfitItemAdapter(getActivity(), response.getOutfit_list()));

					listRoot.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), ActivityContainerSingle.class);
							intent.putExtra(ApplicationPS2Link.ACTIVITY_MODE_KEY, ActivityMode.ACTIVITY_MEMBER_LIST.toString());
							intent.putExtra("outfit_id", ((Outfit) myAdapter.getItemAtPosition(myItemInt)).getOutfit_Id());
							startActivity(intent);
						}
					});

					new UpdateTmpOutfitTable().execute(response.getOutfit_list());
					listRoot.setTextFilterEnabled(true);

				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
					ListView listRoot = (ListView) getActivity().findViewById(R.id.listFoundOutfits);
					if (listRoot != null) {
						listRoot.setAdapter(null);
					}
				}
			};

			GsonRequest<Outfit_response> gsonOject = new GsonRequest<Outfit_response>(url.toString(), Outfit_response.class, null, success, error);
			gsonOject.setTag(this);
			ApplicationPS2Link.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class UpdateTmpOutfitTable extends AsyncTask<ArrayList<Outfit>, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(ArrayList<Outfit>... outfits) {
			int count = outfits[0].size();
			ArrayList<Outfit> list = outfits[0];
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			Outfit outfit = null;
			for (int i = 0; i < count; i++) {
				outfit = data.getOutfit(list.get(i).getOutfit_Id());
				if (outfit == null) {
					data.insertOutfit(list.get(i), true);
				} else {
					if (outfit.isCached()) {
						data.updateOutfit(list.get(i), false);
					} else {
						data.updateOutfit(list.get(i), true);
					}
				}
			}
			data.close();
			return true;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}
}
