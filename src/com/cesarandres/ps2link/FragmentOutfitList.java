package com.cesarandres.ps2link;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cesarandres.ps2link.base.BaseFragment;

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
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_outfit_list, container,
				false);
		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("List of Outfits");

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
		inflater.inflate(R.menu.fragment_outfit_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.outfit_list_add:
			Intent intent = new Intent();
			intent.setClass(getActivity(), ActivityAddOutfit.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
