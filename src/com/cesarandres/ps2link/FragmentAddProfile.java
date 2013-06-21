package com.cesarandres.ps2link;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentAddProfile extends BaseFragment implements
		OnClickListener {

	

	public interface NameToSearchListener {
		void onProfileSelected(CharacterProfile profile);
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_add_profile, container,
				false);
		final Button buttonCharacters = (Button) root.findViewById(
				R.id.buttonSearchProfile);/*
		buttonCharacters.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				@SuppressWarnings("unused")
				EditText searchField = (EditText) root.findViewById(R.id.fieldSearchProfile);
				RequestQueue volley = Volley.newRequestQueue(getActivity());
				URL url;
				try {
					url = SOECensus
							.generateGameDataRequest(
									Verb.GET,
									Game.PS2,
									PS2Collection.CHARACTER_NAME,
									"",
									QueryString
											.generateQeuryString()
											.AddComparison("name.first_lower",
													SearchModifier.STARTSWITH,
													"cramsa")
											.AddCommand(QueryCommand.LIMIT,
													"10"));

					JsonObjectRequest jsObjRequest = new JsonObjectRequest(
							Request.Method.GET, url.toString(), null,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									response.equals(new Object());
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									error.equals(new Object());
								}
							});

					volley.add(jsObjRequest);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});*/
		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}
}
