package com.cesarandres.ps2link.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by cesar on 6/16/13.
 */
public class BaseFragment extends Fragment {

	protected FragmentCallbacks mCallbacks = dummyCallbacks;

	public interface FragmentCallbacks {
		public void onItemSelected(String id, String args[]);
	}

	private static FragmentCallbacks dummyCallbacks = new FragmentCallbacks() {
		@Override
		public void onItemSelected(String id, String args[]) {
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof FragmentCallbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		mCallbacks = (FragmentCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = dummyCallbacks;
	}

}
