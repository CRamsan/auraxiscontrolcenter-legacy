package com.cesarandres.ps2link;

import com.cesarandres.ps2link.module.FragmentBase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMemberList extends FragmentBase {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_member_list, container, false);
	}
}
