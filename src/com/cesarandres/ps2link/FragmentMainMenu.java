package com.cesarandres.ps2link;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cesarandres.ps2link.module.FragmentBase;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMainMenu extends FragmentBase {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_main_menu, container, false);
		
		final Button buttonCharacters = (Button) root.findViewById(R.id.button_characters);
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.OnFragmentEvent(R.id.button_characters);
            }
        });
		
		final Button buttonServers = (Button) root.findViewById(R.id.button_servers);
		buttonServers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.OnFragmentEvent(R.id.button_servers);
            }
        });
		
		final Button buttonOutfit = (Button) root.findViewById(R.id.button_outfit);
		buttonOutfit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.OnFragmentEvent(R.id.button_outfit);
            }
        });
		
		
		return root;
	}

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

}
