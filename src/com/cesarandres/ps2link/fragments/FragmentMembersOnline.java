package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ActivityOutfit;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.response.Outfit_member_response;
import com.cesarandres.ps2link.soe.view.OnlineMemberItemAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMembersOnline extends BaseFragment {

	private boolean isCached;
	private String outfitId;
	private String outfitName;
	private FragmentMembersOnline tag = this;
	public static final int SUCCESS = 0;
	public static final int FAILED = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_member_list, container, false);

		ListView listRoot = (ListView) root.findViewById(R.id.listViewMemberList);
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
						new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
			}
		});

		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ToggleButton append = ((ToggleButton) getActivity().findViewById(R.id.buttonFragmentAppend));
		append.setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.buttonFragmentStar).setVisibility(View.VISIBLE);

		if (savedInstanceState == null) {
			this.outfitId = getArguments().getString("PARAM_0");
		} else {
			this.outfitId = savedInstanceState.getString("outfitId");
		}

		((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(outfitName);

		((ToggleButton) getActivity().findViewById(R.id.buttonFragmentStar)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (outfitId != null && outfitName != null) {
					SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
					SharedPreferences.Editor editor = settings.edit();
					if (isChecked) {
						editor.putString("preferedOutfit", outfitId);
						editor.putString("preferedOutfitName", outfitName);
					} else {
						editor.putString("preferedOutfit", "");
						editor.putString("preferedOutfitName", "");
					}
					editor.commit();
				}
			}
		
		});
		downloadOutfitMembers();
		
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("outfitId", outfitId);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ApplicationPS2Link.volley.cancelAll(tag);
	}

	public void downloadOutfitMembers() {

		setUpdateButton(false);
		setAppendButtonVisibility(false);
		URL url;
		try {
			url = new URL(
					"http://census.soe.com/get/ps2:v2/outfit_member?c:limit=1000&c:resolve=online_status,character(name,battle_rank,profile_id)&c:join=type:profile^list:0^inject_at:profile^show:name.en^on:character.profile_id^to:profile_id&outfit_id="
							+ this.outfitId);

			Listener<Outfit_member_response> success = new Response.Listener<Outfit_member_response>() {
				@Override
				public void onResponse(Outfit_member_response response) {
					try {
						updateContent(response.getOutfit_member_list());
					} catch (Exception e) {
						Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
					}
				}
			};

			ErrorListener error = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					error.equals(new Object());
					Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
					setUpdateButton(true);
				}
			};

			GsonRequest<Outfit_member_response> gsonOject = new GsonRequest<Outfit_member_response>(url.toString(), Outfit_member_response.class, null,
					success, error);
			gsonOject.setTag(tag);
			ApplicationPS2Link.volley.add(gsonOject);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUpdateButton(boolean enabled) {
		getActivity().findViewById(R.id.buttonFragmentUpdate).setEnabled(enabled);
		getActivity().findViewById(R.id.buttonFragmentStar).setEnabled(enabled);
		getActivity().findViewById(R.id.buttonFragmentAppend).setEnabled(enabled);
		if (enabled) {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.VISIBLE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.GONE);
		} else {
			getActivity().findViewById(R.id.buttonFragmentUpdate).setVisibility(View.GONE);
			getActivity().findViewById(R.id.progressBarFragmentTitleLoading).setVisibility(View.VISIBLE);
		}
	}

	private void setAppendButtonVisibility(boolean visible) {
		ToggleButton star = (ToggleButton) getActivity().findViewById(R.id.buttonFragmentStar);
		SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
		String preferedOutfitId = settings.getString("preferedOutfit", "");
		if (preferedOutfitId.equals(outfitId)) {
			star.setChecked(true);
		} else {
			star.setChecked(false);
		}

		getActivity().findViewById(R.id.buttonFragmentAppend).setEnabled(visible);
		star.setEnabled(visible);
	}

	private void updateContent(ArrayList<Member> members) {
		ListView listRoot = (ListView) getView().findViewById(R.id.listViewMemberList);

		listRoot.setAdapter(new OnlineMemberItemAdapter(members, getActivity()));
		listRoot.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
				mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
						new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
			}
		});

		ToggleButton append = ((ToggleButton) getActivity().findViewById(R.id.buttonFragmentAppend));
		append.setOnCheckedChangeListener(null);
		append.setChecked(isCached);
	}
}
