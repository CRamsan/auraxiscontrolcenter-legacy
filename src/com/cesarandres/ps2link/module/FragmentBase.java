package com.cesarandres.ps2link.module;

import android.support.v4.app.Fragment;

/**
 * Created by cesar on 6/16/13.
 */
public abstract class FragmentBase extends Fragment {

	protected OnFragmentEventListener mListener;
	
	public interface OnFragmentEventListener {
		public void OnFragmentEvent(int id);
	}
}
