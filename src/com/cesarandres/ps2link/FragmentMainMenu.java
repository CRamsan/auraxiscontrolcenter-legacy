package com.cesarandres.ps2link;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cesarandres.ps2link.base.BaseFragment;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMainMenu extends BaseFragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_main_menu, container,
				false);

		final Button buttonCharacters = (Button) root
				.findViewById(R.id.buttonCharacters);
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityProfileList.class);
				startActivity(intent);
			}
		});

		final Button buttonServers = (Button) root
				.findViewById(R.id.buttonServers);
		buttonServers.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityServerList.class);
				startActivity(intent);
			}
		});

		final Button buttonOutfit = (Button) root
				.findViewById(R.id.buttonOutfit);
		buttonOutfit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityOutfitList.class);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
