package com.cesarandres.ps2link;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cesarandres.ps2link.module.FragmentBase;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentProfileList extends FragmentBase {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_profile_list, container, false);
	}
}
