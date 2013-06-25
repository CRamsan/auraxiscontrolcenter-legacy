package com.cesarandres.ps2link;

import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentReddit extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View root = inflater.inflate(R.layout.fragment_reddit, container,
				false);

		((TextView) root.findViewById(R.id.textViewFragmentTitle))
				.setText("Reddit");
		
		Button updateButton = (Button) root
				.findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
