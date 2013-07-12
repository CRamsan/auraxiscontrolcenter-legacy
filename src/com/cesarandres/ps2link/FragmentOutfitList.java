package com.cesarandres.ps2link;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.view.OutfitItemAdapter;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentOutfitList extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_outfit_list, container,
				false);

		ListView listRoot = (ListView) root
				.findViewById(R.id.listViewOutfitList);
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

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.text_menu_outfits));
		ImageButton searchButton = (ImageButton) getActivity().findViewById(R.id.buttonFragmentAdd);
		searchButton.setVisibility(View.VISIBLE);

		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityAddOutfit.class);
				startActivity(intent);
			}
		});

		ImageButton updateButton = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);
		updateButton.setEnabled(false);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new ReadOutfitsTable().execute();
			}
		});


	}

	@Override
	public void onResume() {
		super.onResume();
		new ReadOutfitsTable().execute();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private class ReadOutfitsTable extends
			AsyncTask<Integer, Integer, ArrayList<Outfit>> {

		@Override
		protected void onPreExecute() {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(
					false);
		}

		@Override
		protected ArrayList<Outfit> doInBackground(Integer... params) {
			ObjectDataSource data = new ObjectDataSource(getActivity());
			data.open();
			ArrayList<Outfit> outfitList = data.getAllOutfits(false);
			data.close();
			return outfitList;
		}

		@Override
		protected void onPostExecute(ArrayList<Outfit> result) {
			ListView listRoot = (ListView) getActivity().findViewById(
					R.id.listViewOutfitList);
			listRoot.setAdapter(new OutfitItemAdapter(getActivity(), result));
			getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(
					true);
		}
	}
}
