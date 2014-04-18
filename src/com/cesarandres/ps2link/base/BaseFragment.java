package com.cesarandres.ps2link.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cesar on 6/16/13.
 */
public abstract class BaseFragment extends Fragment {

	protected FragmentCallbacks mCallbacks = dummyCallbacks;
	protected String loggingTag;
		
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
		Log.i(this.loggingTag, "Fragment onAttach");
		if (!(activity instanceof FragmentCallbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		mCallbacks = (FragmentCallbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.i(this.loggingTag, "Fragment onCreate");
		super.onCreate(savedInstanceState);
		this.loggingTag = getClass().getName();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Log.i(this.loggingTag, "Fragment onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		Log.i(this.loggingTag, "Fragment onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState){
		Log.i(this.loggingTag, "Fragment onViewStateRestored");
		super.onViewStateRestored(savedInstanceState);
	}
	
	@Override
	public void onStart(){
		Log.i(this.loggingTag, "Fragment onStart");
		super.onStart();
	}
	
	@Override
	public void onResume(){
		Log.i(this.loggingTag, "Fragment onResume");
		super.onResume();
	}
	
	@Override
	public void onPause(){
		Log.i(this.loggingTag, "Fragment onPause");
		super.onPause();
	}
	
	@Override
	public void onStop(){
		Log.i(this.loggingTag, "Fragment onStop");
		super.onStop();
	}
	
	@Override
	public void onDestroyView(){
		Log.i(this.loggingTag, "Fragment onDestroyView");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy(){
		Log.i(this.loggingTag, "Fragment onDestroy");
		super.onDestroy();
	}
	
	@Override
	public void onDetach() {
		Log.i(this.loggingTag, "Fragment onDetach");
		super.onDetach();
		mCallbacks = dummyCallbacks;
	}
}
