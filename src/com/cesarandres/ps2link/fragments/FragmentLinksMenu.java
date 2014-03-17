package com.cesarandres.ps2link.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cesarandres.ps2link.ActivityAbout;
import com.cesarandres.ps2link.ActivityContainerSingle;
import com.cesarandres.ps2link.ActivityProfile;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.ApplicationPS2Link.ActivityMode;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.base.BitmapWorkerTask;
import com.cesarandres.ps2link.soe.content.CharacterProfile;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentLinksMenu extends BaseFragment {

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_links_menu, container, false);

		final Button buttonForums = (Button) root.findViewById(R.id.buttonForums);
		buttonForums.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "https://forums.station.sony.com/ps2/index.php";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonReddit = (Button) root.findViewById(R.id.buttonReddit);
		buttonReddit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://www.reddit.com/r/Planetside/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonPSU = (Button) root.findViewById(R.id.buttonPSU);
		buttonPSU.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://www.planetside-universe.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonWikia= (Button) root.findViewById(R.id.buttonWikia);
		buttonWikia.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://planetside.wikia.com/wiki/PlanetSide_2_Wiki";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonPS2Maps = (Button) root.findViewById(R.id.buttonPS2Maps);
		buttonPS2Maps.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://ps2maps.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonLore = (Button) root.findViewById(R.id.buttonLore);
		buttonLore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://www.reddit.com/r/Planetsidelore/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonCommClash = (Button) root.findViewById(R.id.buttonCommClash);
		buttonCommClash.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://ps2commclash.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		final Button buttonPS2Alerts = (Button) root.findViewById(R.id.buttonPS2Alerts);
		buttonPS2Alerts.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String url = "http://ps2alerts.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_links));
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
