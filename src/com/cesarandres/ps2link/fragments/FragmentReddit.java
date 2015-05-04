package com.cesarandres.ps2link.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.volley.GsonRequest;
import com.cesarandres.ps2link.module.reddit.Child;
import com.cesarandres.ps2link.module.reddit.Content;
import com.cesarandres.ps2link.module.reddit.RedditItemAdapter;

/**
 * Fragment that retrieves the hottest Reddit post
 */
public class FragmentReddit extends BaseFragment {

	private static final String REDDIT_ENDPOINT = "http://www.reddit.com/r/Planetside/hot.json";
	private static final String REDDIT_URL = "http://www.reddit.com/r/Planetside/";
	private Button goToReddit;
	
    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @SuppressLint("InflateParams")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    goToReddit = (Button)inflater.inflate(R.layout.layout_go_to_reddit, null);
	goToReddit.setOnClickListener(new View.OnClickListener() {		
		@Override
		public void onClick(View v) {
	    	Intent openRedditIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(REDDIT_URL));
	    	startActivity(openRedditIntent);
		}
	});
	
    return inflater.inflate(R.layout.fragment_reddit, container, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	this.fragmentTitle.setText(getString(R.string.title_reddit));
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewRedditList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
	    	Child child = ((Child) myAdapter.getItemAtPosition(myItemInt));
	    	Intent openRedditIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(child.getData().getUrl()));
	    	startActivity(openRedditIntent);
	    }
	});
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_REDDIT);
	LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTitle);
	titleLayout.addView(goToReddit);
	LinearLayout.LayoutParams params = (LayoutParams) goToReddit.getLayoutParams();
	params.gravity = Gravity.CENTER_VERTICAL;
	goToReddit.setLayoutParams(params);
	titleLayout.invalidate();
	updatePosts();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onPause()
     */
    @Override
    public void onPause() {
	super.onPause();
	LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutTitle);
	titleLayout.removeView(goToReddit);
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
	super.onDestroyView();
    }

    /**
     * Send a request to get all the information from the Reddit server
     */
    @SuppressWarnings("unchecked")
	private void updatePosts() {
    	setProgressButton(true);
    	String url = REDDIT_ENDPOINT;

    	Listener<Content> success = new Response.Listener<Content>() {
    	    @Override
    	    public void onResponse(Content response) {
    		setProgressButton(false);
    		try {
    		    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewRedditList);
    		    listRoot.setAdapter(new RedditItemAdapter(getActivity(), response.getData().getChildren()));
    		} catch (Exception e) {
    		    Toast.makeText(getActivity(), getView().getResources().getString(R.string.toast_error_retrieving_data), Toast.LENGTH_SHORT).show();
    		}
    	    }
    	};

    	ErrorListener error = new Response.ErrorListener() {
    	    @Override
    	    public void onErrorResponse(VolleyError error) {
    		setProgressButton(false);
    		Toast.makeText(getActivity(), getView().getResources().getString(R.string.toast_error_retrieving_data), Toast.LENGTH_SHORT).show();
    	    }
    	};

    	@SuppressWarnings({ "rawtypes" })
		GsonRequest gsonOject = new GsonRequest(url, Content.class, null, success, error);
    	gsonOject.setTag(this);
    	ApplicationPS2Link.volley.add(gsonOject);
    }
}
