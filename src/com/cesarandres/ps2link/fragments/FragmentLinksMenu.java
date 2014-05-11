package com.cesarandres.ps2link.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;

/**
 * @author Cesar Ramirez This fragment contains a list of buttons to different
 *         webapges. Each button will send an intent to open the page.
 * 
 */
public class FragmentLinksMenu extends BaseFragment {

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_links_menu, container, false);
    }

    /* (non-Javadoc)
     * @see com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	this.fragmentTitle.setText(getString(R.string.title_links));
	final Button buttonForums = (Button) getActivity().findViewById(R.id.buttonForums);
	buttonForums.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "https://forums.station.sony.com/ps2/index.php";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonReddit = (Button) getActivity().findViewById(R.id.buttonReddit);
	buttonReddit.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://www.reddit.com/r/Planetside/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonPSU = (Button) getActivity().findViewById(R.id.buttonPSU);
	buttonPSU.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://www.planetside-universe.com/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonWikia = (Button) getActivity().findViewById(R.id.buttonWikia);
	buttonWikia.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://planetside.wikia.com/wiki/PlanetSide_2_Wiki";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonPS2Maps = (Button) getActivity().findViewById(R.id.buttonPS2Maps);
	buttonPS2Maps.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://ps2maps.com/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonLore = (Button) getActivity().findViewById(R.id.buttonLore);
	buttonLore.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://www.reddit.com/r/Planetsidelore/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonCommClash = (Button) getActivity().findViewById(R.id.buttonCommClash);
	buttonCommClash.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://ps2commclash.com/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

	final Button buttonPS2Alerts = (Button) getActivity().findViewById(R.id.buttonPS2Alerts);
	buttonPS2Alerts.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		String url = "http://ps2alerts.com/";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	    }
	});

    }

    /* (non-Javadoc)
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	getActivityContainer().setActivityMode(ActivityMode.ACTIVITY_MAIN_MENU);
    }
}
