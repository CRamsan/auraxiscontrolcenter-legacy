package com.cesarandres.ps2link.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.CharacterEvent;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.item.Weapon;
import com.cesarandres.ps2link.soe.content.item.WeaponStat;
import com.cesarandres.ps2link.soe.content.response.Weapon_list_response;
import com.cesarandres.ps2link.soe.util.Collections;
import com.cesarandres.ps2link.soe.view.WeaponItemAdapter;

/**
 * This fragment will retrieve the list of weapons for a player and display it.
 * 
 */
public class FragmentWeaponList extends BaseFragment {

    private String profileId;
    private String profileFaction;

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_weapon_list, container, false);
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
	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewWeaponList);
	listRoot.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterEvent) myAdapter.getItemAtPosition(myItemInt)).getImportant_character_id() });
	    }
	});

	this.profileId = getArguments().getString("PARAM_0");
	this.profileFaction = "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	downloadWeaponList(this.profileId);
    }

    /**
     * @param character_id
     *            retrieve the weapon list for a character with the given
     *            character_id and displays it.
     */
    public void downloadWeaponList(String character_id) {
    if("".equals(profileFaction)){
    	GetProfileFromTable task = new GetProfileFromTable();
        setCurrentTask(task);
        task.execute(this.profileId);
        return;
    }
    	
	setProgressButton(true);
	String url = 	"http://census.soe.com/get/ps2:v2/characters_weapon_stat_by_faction/?" +
					"character_id=" + character_id + "&c:join=item^show:image_path'name.en&c:limit=10000";
	Listener<Weapon_list_response> success = new Response.Listener<Weapon_list_response>() {
	    @Override
	    public void onResponse(Weapon_list_response response) {
		setProgressButton(false);
		try {
			new GenerateWeaponStats().execute(response);
		} catch (Exception e) {
		    Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
		}
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		setProgressButton(false);
		Toast.makeText(getActivity(), "Error retrieving data", Toast.LENGTH_SHORT).show();
	    }
	};
	SOECensus.sendGsonRequest(url, Weapon_list_response.class, success, error, this);
    }
    
    /**
     * Use this task to go over the data we recieved and generate the objects that 
     * we are going to pass to the adapter
     */
    private class GenerateWeaponStats extends AsyncTask<Weapon_list_response, Integer, ArrayList<WeaponStat>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected ArrayList<WeaponStat> doInBackground(Weapon_list_response... args) {
		Weapon_list_response response = args[0];
		ArrayList<WeaponStat> stats = new ArrayList<WeaponStat>();
		HashMap<String, WeaponStat> weaponMap = new HashMap<String, WeaponStat>();
		
		int statA = 0, statB = 0;
		String weaponName;
		for(Weapon weapon : response.getcharacters_weapon_stat_by_faction_list()){
			if(weapon.getItem_id_join_item() == null){
				weaponName = "";
			}else{
				weaponName = weapon.getItem_id_join_item().getName().getEn();
			}
			
			WeaponStat stat;
			if(!weaponMap.containsKey(weaponName)){
				stat = new WeaponStat();
				stat.setName(weaponName);
				weaponMap.put(weaponName, stat);
			}else{
				stat = weaponMap.get(weaponName);
			}
			if (profileFaction == Faction.VS){
				statA = stat.getTR();
				statB = stat.getNC();
			}else if(profileFaction == Faction.NC){
				statA = stat.getTR();
				statB = stat.getVS();
			}else if(profileFaction == Faction.TR){
				statA = stat.getVS();
				statB = stat.getNC();
			}
			if(weapon.getStat_name().equals("weapon_vehicle_kills")){
				stat.setVehicleKills(statA + statB);
			}else if(weapon.getStat_name().equals("weapon_headshots")){
				stat.setHeadshots(statA + statB);
			}else if(weapon.getStat_name().equals("weapon_kills")){
				stat.setKills(statA + statB);
			} 
		}
		java.util.Collections.sort(stats);
	    return stats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ArrayList<WeaponStat> result) {
	    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewWeaponList);
	    listRoot.setAdapter(new WeaponItemAdapter(getActivity(), result, profileId, profileFaction));
	    setProgressButton(false);
	}
    }
    
    /**
     * Read the profile from the database and retrieve the character data
     */
    private class GetProfileFromTable extends AsyncTask<String, Integer, CharacterProfile> {

	private String profile_id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    setProgressButton(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected CharacterProfile doInBackground(String... args) {
	    this.profile_id = args[0];
	    ObjectDataSource data = getActivityContainer().getData();
	    CharacterProfile profile = data.getCharacter(this.profile_id);
	    return profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(CharacterProfile result) {
	    setProgressButton(false);
		if(result != null){
			profileFaction = result.getFaction_id();
			downloadWeaponList(this.profile_id);
		}else{
			//TODO Externalize string
			Toast.makeText(getActivity(), "Profile data is being downloaded.", Toast.LENGTH_SHORT).show();;
		}
	}
	}
}