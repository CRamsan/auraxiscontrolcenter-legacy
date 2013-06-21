package com.cesarandres.ps2link;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cesarandres.ps2link.base.BaseFragment;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentServerList extends BaseFragment {

	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_server_list, container, false);
	}
		
	@Override
	public void onPause (){
		super.onPause();
	}
}
