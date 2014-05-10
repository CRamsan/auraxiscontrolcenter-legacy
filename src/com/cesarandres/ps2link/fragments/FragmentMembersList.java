package com.cesarandres.ps2link.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.cesarandres.ps2link.ActivityContainer;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.Outfit;
import com.cesarandres.ps2link.soe.content.response.Outfit_member_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.util.QueryString;
import com.cesarandres.ps2link.soe.util.QueryString.QueryCommand;
import com.cesarandres.ps2link.soe.util.QueryString.SearchModifier;
import com.cesarandres.ps2link.soe.view.MemberItemAdapter;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentMembersList extends BaseFragment {

    private boolean isCached;
    private boolean shownOffline = false;;
    private int outfitSize;
    private String outfitId;
    private String outfitName;
    public static final int SUCCESS = 0;
    public static final int FAILED = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
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

	if (savedInstanceState == null) {
	    UpdateOutfitFromTable task = new UpdateOutfitFromTable();
	    this.currentTask = task;
	    this.outfitId = getArguments().getString("PARAM_0");
	    task.execute(this.outfitId);
	} else {
	    this.outfitSize = savedInstanceState.getInt("outfitSize", 0);
	    this.outfitId = savedInstanceState.getString("outfitId");
	    this.outfitName = savedInstanceState.getString("outfitName");
	    this.shownOffline = savedInstanceState.getBoolean("showOffline");
	}

	this.fragmentTitle.setText(outfitName);
    }

    @Override
    public void onResume() {
	super.onResume();
	if (outfitId != null) {
	    updateContent();
	}

    }

    @Override
    public void onPause() {
	super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	super.onSaveInstanceState(savedInstanceState);
	savedInstanceState.putInt("outfitSize", outfitSize);
	savedInstanceState.putString("outfitId", outfitId);
	savedInstanceState.putString("outfitName", outfitName);
	savedInstanceState.putBoolean("showOffline", shownOffline);
    }

    public void downloadOutfitMembers() {
	setProgressButton(true);
	URL url;
	try {
	    url = SOECensus.generateGameDataRequest(
		    Verb.GET,
		    Game.PS2V2,
		    PS2Collection.OUTFIT,
		    "",
		    QueryString.generateQeuryString().AddComparison("outfit_id", SearchModifier.EQUALS, this.outfitId)
			    .AddCommand(QueryCommand.RESOLVE, "member_online_status,member,member_character(name,type.faction)"));

	    Listener<Outfit_member_response> success = new Response.Listener<Outfit_member_response>() {
		@Override
		public void onResponse(Outfit_member_response response) {
		    try {
			UpdateMembers task = new UpdateMembers();
			currentTask = task;
			((UpdateMembers) task).execute(response.getOutfit_list().get(0).getMembers());
			setProgressButton(false);
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
		    setProgressButton(false);
		}
	    };

	    GsonRequest<Outfit_member_response> gsonOject = new GsonRequest<Outfit_member_response>(url.toString(), Outfit_member_response.class, null,
		    success, error);
	    gsonOject.setTag(this);
	    ApplicationPS2Link.volley.add(gsonOject);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void updateContent() {
	if (this.outfitId != null) {
	    ListView listRoot = (ListView) getView().findViewById(R.id.listViewMemberList);
	    ObjectDataSource data = getActivityContainer().getData();
	    listRoot.setAdapter(new MemberItemAdapter(getActivity(), outfitId, data, isCached, shownOffline));

	    listRoot.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		    mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			    new String[] { ((Member) myAdapter.getItemAtPosition(myItemInt)).getCharacter_id() });
		}
	    });

	    this.fragmentShowOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    shownOffline = isChecked;
		    updateContent();
		}
	    });

	    this.fragmentAppend.setOnCheckedChangeListener(null);
	    this.fragmentAppend.setChecked(isCached);
	    this.fragmentAppend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    if (isChecked) {
			CacheOutfit task = new CacheOutfit();
			currentTask = task;
			task.execute(outfitId);
		    } else {
			UnCacheOutfit task = new UnCacheOutfit();
			currentTask = task;
			task.execute(outfitId);
		    }
		}
	    });

	    this.fragmentStar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
			getActivityContainer().checkPreferedButtons();
		    }
		}
	    });

	    SharedPreferences settings = getActivity().getSharedPreferences("PREFERENCES", 0);
	    String preferedOutfitId = settings.getString("preferedOutfit", "");
	    if (preferedOutfitId.equals(outfitId)) {
		this.fragmentStar.setChecked(true);
	    } else {
		this.fragmentStar.setChecked(false);
	    }
	}
    }

    public class UpdateOutfitFromTable extends AsyncTask<String, Integer, Outfit> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected Outfit doInBackground(String... args) {
	    Outfit outfit = null;
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		outfit = data.getOutfit(args[0]);
		isCached = outfit.isCached();
	    } catch (Exception e) {
	    }
	    return outfit;
	}

	@Override
	protected void onPostExecute(Outfit result) {
	    outfitId = result.getOutfit_Id();
	    outfitName = result.getName();
	    outfitSize = result.getMember_count();
	    ((Button) getActivity().findViewById(R.id.buttonFragmentTitle)).setText(outfitName);
	    setProgressButton(false);
	    updateContent();
	    downloadOutfitMembers();
	}
    }

    public class UpdateMembers extends AsyncTask<ArrayList<Member>, Integer, Integer> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected Integer doInBackground(ArrayList<Member>... members) {
	    ArrayList<Member> newMembers = members[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		for (Member member : newMembers) {
		    if (data.getMember(member.getCharacter_id()) == null) {
			data.insertMember(member, outfitId, !isCached);
		    } else {
			data.updateMember(member, !isCached);
		    }
		    if (isCancelled()) {
			return null;
		    }
		}
	    } catch (Exception e) {
	    }
	    return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
	    setProgressButton(false);
	    updateContent();
	}
    }

    public class CacheOutfit extends AsyncTask<String, Integer, Integer> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected Integer doInBackground(String... args) {
	    ObjectDataSource data = getActivityContainer().getData();
	    Outfit outfit = data.getOutfit(args[0]);
	    try {
		data.updateOutfit(outfit, false);
		isCached = true;
	    } catch (Exception e) {
		return FAILED;
	    }
	    return SUCCESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
	    if (isCached) {
		updateContent();
	    }
	    setProgressButton(false);
	}
    }

    public class UnCacheOutfit extends AsyncTask<String, Integer, Integer> {

	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	@Override
	protected Integer doInBackground(String... args) {
	    ObjectDataSource data = getActivityContainer().getData();
	    try {
		Outfit outfit = data.getOutfit(args[0]);
		data.updateOutfit(outfit, true);
		isCached = false;
	    } catch (Exception e) {
		return FAILED;
	    }
	    return SUCCESS;
	}

	@Override
	protected void onPostExecute(Integer result) {
	    if (!isCached) {
		updateContent();
	    }
	    setProgressButton(false);
	}
    }

}
