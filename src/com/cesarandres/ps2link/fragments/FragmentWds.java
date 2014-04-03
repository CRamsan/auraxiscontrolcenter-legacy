package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.R.drawable;
import com.cesarandres.ps2link.R.id;
import com.cesarandres.ps2link.R.layout;
import com.cesarandres.ps2link.R.string;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.WDS_Stat;
import com.cesarandres.ps2link.soe.content.response.World_Stat_History_Server_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.view.WdsAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentWds extends BaseFragment {

	private static final String NC = "nc";
	private static final String TR = "tr";
	private static final String VS = "vs";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_wds, container, false);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(getString(R.string.title_wds));
		ImageButton updateButton = (ImageButton) getActivity().findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);
		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				downloadWDSStats();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		downloadWDSStats();
	}

	@Override
	public void onStop() {
		super.onStop();
		ApplicationPS2Link.volley.cancelAll(this);
	}

	public void downloadWDSStats() {
		setUpdateButton(false);
		URL url;
		try {
			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, PS2Collection.WORLD_STAT_HISTORY, "",
					QueryString.generateQeuryString().AddCommand(QueryCommand.LIMIT, "100"));

			Listener<World_Stat_History_Server_response> success = new Response.Listener<World_Stat_History_Server_response>() {
				@Override
				public void onResponse(World_Stat_History_Server_response response) {
					GridView gridRoot = (GridView) getActivity().findViewById(R.id.gridViewWdsScore);
					gridRoot.setAdapter(new WdsAdapter(getActivity(), response.getWorld_stat_history_list()));
					configureTotal(response.getWorld_stat_history_list());
					setUpdateButton(true);
				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
					setUpdateButton(true);
				}
			};
			GsonRequest<World_Stat_History_Server_response> gsonOject = new GsonRequest<World_Stat_History_Server_response>(url.toString(),
					World_Stat_History_Server_response.class, null, success, error);
			gsonOject.setTag(this);
			ApplicationPS2Link.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void configureTotal(ArrayList<WDS_Stat> wdsStats) {
		WDS_Stat total = new WDS_Stat();
		total.setWorld_name("TOTAL:");

		WDS_Stat totalNC = new WDS_Stat();
		totalNC.setFaction(NC);
		int totalToday = 0, totalWeek = 0, totalMonth = 0;
		for (int i = 1; i < wdsStats.size(); i += 4) {
			totalToday += wdsStats.get(i).getToday();
			totalWeek += wdsStats.get(i).getThisWeek();
			totalMonth += wdsStats.get(i).getThisMonth();
		}
		totalNC.setDay(totalNC.new Day());
		totalNC.setWeek(totalNC.new Week());
		totalNC.setMonth(totalNC.new Month());
		totalNC.setToday(totalToday);
		totalNC.setThisWeek(totalWeek);
		totalNC.setThisMonth(totalMonth);

		WDS_Stat totalTR = new WDS_Stat();
		totalTR.setFaction(TR);
		totalToday = 0;
		totalWeek = 0;
		totalMonth = 0;
		for (int i = 2; i < wdsStats.size(); i += 4) {
			totalToday += wdsStats.get(i).getToday();
			totalWeek += wdsStats.get(i).getThisWeek();
			totalMonth += wdsStats.get(i).getThisMonth();
		}
		totalTR.setDay(totalTR.new Day());
		totalTR.setWeek(totalTR.new Week());
		totalTR.setMonth(totalTR.new Month());
		totalTR.setToday(totalToday);
		totalTR.setThisWeek(totalWeek);
		totalTR.setThisMonth(totalMonth);

		WDS_Stat totalVS = new WDS_Stat();
		totalVS.setFaction(VS);
		totalToday = 0;
		totalWeek = 0;
		totalMonth = 0;
		for (int i = 3; i < wdsStats.size(); i += 4) {
			totalToday += wdsStats.get(i).getToday();
			totalWeek += wdsStats.get(i).getThisWeek();
			totalMonth += wdsStats.get(i).getThisMonth();
		}
		totalVS.setDay(totalVS.new Day());
		totalVS.setWeek(totalVS.new Week());
		totalVS.setMonth(totalVS.new Month());
		totalVS.setToday(totalToday);
		totalVS.setThisWeek(totalWeek);
		totalVS.setThisMonth(totalMonth);

		View viewTotal = getActivity().findViewById(R.id.wdsItemTotal);
		View viewTotalNC = getActivity().findViewById(R.id.wdsItemTotalNC);
		View viewTotalTR = getActivity().findViewById(R.id.wdsItemTotalTR);
		View viewTotalVS = getActivity().findViewById(R.id.wdsItemTotalVS);

		((TextView) viewTotal.findViewById(R.id.TextViewWdsStatItemServer)).setText("TOTAL:");
		((TextView) viewTotal.findViewById(R.id.TextViewWdsStatItemToday)).setText("TODAY:");
		((TextView) viewTotal.findViewById(R.id.TextViewWdsStatItemWeek)).setText("WEEK:");
		((TextView) viewTotal.findViewById(R.id.TextViewWdsStatItemMonth)).setText("SEASON:");

		((ImageView) viewTotalNC.findViewById(R.id.imageViewWdsStatItemFaction)).setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),
				R.drawable.icon_faction_nc));
		;
		((TextView) viewTotalNC.findViewById(R.id.TextViewWdsStatItemServer)).setText(totalNC.getAll_time());
		((TextView) viewTotalNC.findViewById(R.id.TextViewWdsStatItemToday)).setText(Integer.toString(totalNC.getToday()));
		((TextView) viewTotalNC.findViewById(R.id.TextViewWdsStatItemWeek)).setText(Integer.toString(totalNC.getThisWeek()));
		((TextView) viewTotalNC.findViewById(R.id.TextViewWdsStatItemMonth)).setText(Integer.toString(totalNC.getThisMonth()));

		((ImageView) viewTotalTR.findViewById(R.id.imageViewWdsStatItemFaction)).setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),
				R.drawable.icon_faction_tr));
		;
		((TextView) viewTotalTR.findViewById(R.id.TextViewWdsStatItemServer)).setText(totalTR.getAll_time());
		((TextView) viewTotalTR.findViewById(R.id.TextViewWdsStatItemToday)).setText(Integer.toString(totalTR.getToday()));
		((TextView) viewTotalTR.findViewById(R.id.TextViewWdsStatItemWeek)).setText(Integer.toString(totalTR.getThisWeek()));
		((TextView) viewTotalTR.findViewById(R.id.TextViewWdsStatItemMonth)).setText(Integer.toString(totalTR.getThisMonth()));

		((ImageView) viewTotalVS.findViewById(R.id.imageViewWdsStatItemFaction)).setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(),
				R.drawable.icon_faction_vs));
		;
		((TextView) viewTotalVS.findViewById(R.id.TextViewWdsStatItemServer)).setText(totalVS.getAll_time());
		((TextView) viewTotalVS.findViewById(R.id.TextViewWdsStatItemToday)).setText(Integer.toString(totalVS.getToday()));
		((TextView) viewTotalVS.findViewById(R.id.TextViewWdsStatItemWeek)).setText(Integer.toString(totalVS.getThisWeek()));
		((TextView) viewTotalVS.findViewById(R.id.TextViewWdsStatItemMonth)).setText(Integer.toString(totalVS.getThisMonth()));
	}

	private void setUpdateButton(boolean enabled) {
		getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(enabled);
		if (enabled) {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.GONE);
		} else {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.GONE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}