package com.cesarandres.ps2link.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by cesar on 6/16/13.
 */
public abstract class BaseFragment extends Fragment {

	protected OnFragmentEventListener mListener;

	public interface OnFragmentEventListener {
		public void OnFragmentEvent(int id);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
}
