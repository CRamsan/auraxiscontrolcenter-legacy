package com.cesarandres.ps2link.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.cesarandres.ps2link.ActivityContainer;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.util.Logger;

/**
 * 
 * 
 * This class extends fragment to add the support for a callback. All the
 * fragments should extend this class instead of the standard Fragment class.
 * All behavior that is shared across all fragments should also be implemented
 * here.
 * 
 */
public abstract class BaseFragment extends Fragment {

    protected FragmentCallbacks mCallbacks = emptyCallbacks;

    @SuppressWarnings("rawtypes")
    private AsyncTask currentTask;

    protected Button fragmentTitle;
    protected ProgressBar fragmentProgress;
    protected ImageButton fragmentUpdate;
    protected ToggleButton fragmentShowOffline;
    protected ImageButton fragmentAdd;
    protected ToggleButton fragmentStar;
    protected ToggleButton fragmentAppend;

    private static FragmentCallbacks emptyCallbacks = new FragmentCallbacks() {
	@Override
	public void onItemSelected(String id, String args[]) {
	    Logger.log(Log.WARN, this, "Item selected when no activity was set, this should never happen");
	}
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
	Logger.log(Log.INFO, this, "Fragment onAttach");
	super.onAttach(activity);
	if (!(activity instanceof FragmentCallbacks)) {
	    throw new IllegalStateException("Activity must implement fragment's callbacks.");
	}
	mCallbacks = (FragmentCallbacks) activity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	Logger.log(Log.INFO, this, "Fragment onCreate");
	super.onCreate(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	Logger.log(Log.INFO, this, "Fragment onCreateView");
	return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	Logger.log(Log.INFO, this, "Fragment onActivityCreated");
	super.onActivityCreated(savedInstanceState);

	this.fragmentTitle = (Button) getActivity().findViewById(R.id.buttonFragmentTitle);
	this.fragmentProgress = (ProgressBar) getActivity().findViewById(R.id.progressBarFragmentTitleLoading);
	this.fragmentUpdate = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
	this.fragmentShowOffline = (ToggleButton) getActivity().findViewById(R.id.toggleButtonShowOffline);
	this.fragmentAdd = (ImageButton) getActivity().findViewById(R.id.buttonFragmentAdd);
	this.fragmentStar = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentStar);
	this.fragmentAppend = (ToggleButton) getActivity().findViewById(R.id.toggleButtonFragmentAppend);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
	Logger.log(Log.INFO, this, "Fragment onViewStateRestored");
	super.onViewStateRestored(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
	Logger.log(Log.INFO, this, "Fragment onStart");
	super.onStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	Logger.log(Log.INFO, this, "Fragment onResume");
	super.onResume();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
	Logger.log(Log.INFO, this, "Fragment onPause");
	super.onPause();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onStop()
     */
    @Override
    public void onStop() {
	Logger.log(Log.INFO, this, "Fragment onStop");
	super.onStop();
	// When a fragment is stopped all tasks should be cancelled
	ApplicationPS2Link.volley.cancelAll(this);
	if (currentTask != null) {
	    currentTask.cancel(true);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
	Logger.log(Log.INFO, this, "Fragment onDestroyView");
	super.onDestroyView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
	Logger.log(Log.INFO, this, "Fragment onDestroy");
	super.onDestroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onDetach()
     */
    @Override
    public void onDetach() {
	Logger.log(Log.INFO, this, "Fragment onDetach");
	super.onDetach();
	mCallbacks = emptyCallbacks;
    }

    /**
     * @param enabled
     *            if set to true, the progress view is displayed and the update
     *            view is hidden. If set to false, the opposite will happen
     */
    protected void setProgressButton(boolean enabled) {
	if (enabled) {
	    this.fragmentUpdate.setVisibility(View.GONE);
	    this.fragmentProgress.setVisibility(View.VISIBLE);
	} else {
	    this.fragmentUpdate.setVisibility(View.VISIBLE);
	    this.fragmentProgress.setVisibility(View.GONE);
	}
    }

    /**
     * @return the ActivityContainer object that this class belongs to
     */
    protected ActivityContainer getActivityContainer() {
	return (ActivityContainer) getActivity();
    }

    /**
     * @param currentTask
     *            new ASyncTask to be attached to this fragment. If a task is
     *            already attached, the old one is cancelled and the new one is
     *            set
     */
    @SuppressWarnings("rawtypes")
    public void setCurrentTask(AsyncTask currentTask) {
	if (this.currentTask != null) {
	    this.currentTask.cancel(true);
	}
	this.currentTask = currentTask;
    }

    /**
     * 
     * 
     * This interface is used to send actions from the fragment back to the
     * activity that is attached to
     * 
     */
    public interface FragmentCallbacks {
	public void onItemSelected(String id, String args[]);
    }
}
