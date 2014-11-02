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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.CharacterProfile;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.item.Weapon;
import com.cesarandres.ps2link.soe.content.item.WeaponStat;
import com.cesarandres.ps2link.soe.content.response.Weapon_list_response;
import com.cesarandres.ps2link.soe.view.WeaponItemAdapter;

/**
 * This fragment will retrieve the list of weapons for a player and display it.
 * 
 */
public class FragmentWeaponList extends BaseFragment {

    private String profileId;
    private String profileFaction;

    private ArrayList<WeaponStat> weaponKills;
    private ArrayList<WeaponStat> weaponKilledBy;
    
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
	    	//TODO Give a correct behaviour to this item
	    	/*mCallbacks.onItemSelected(ApplicationPS2Link.ActivityMode.ACTIVITY_PROFILE.toString(),
			new String[] { ((CharacterEvent) myAdapter.getItemAtPosition(myItemInt)).getImportant_character_id() });*/
	    }
	});

	this.fragmentMyWeapons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    	ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewWeaponList);
		    WeaponItemAdapter weaponAdapter = (WeaponItemAdapter) listRoot.getAdapter();
		    if(weaponAdapter != null){
		    	weaponAdapter.setMyWeapons(isChecked);		    	
		 	    listRoot.setAdapter(weaponAdapter);
		    }
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
    private class GenerateWeaponStats extends AsyncTask<Weapon_list_response, Integer, Integer> {

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
	protected Integer doInBackground(Weapon_list_response... args) {
		Weapon_list_response response = args[0];
		ArrayList<WeaponStat> weaponKillStats = new ArrayList<WeaponStat>();
		ArrayList<WeaponStat> weaponKilledByStats = new ArrayList<WeaponStat>();
		
		HashMap<String, WeaponStat> weaponMap = new HashMap<String, WeaponStat>();
		HashMap<String, WeaponStat> weaponKilledMap = new HashMap<String, WeaponStat>();
		
		int statA = 0, statB = 0;
		String weaponName;
		for(Weapon weapon : response.getcharacters_weapon_stat_by_faction_list()){
			WeaponStat stat;
			HashMap<String, WeaponStat> statMap;
			if(weapon.getItem_id_join_item() == null){
				weaponName = "";
			}else{
				weaponName = weapon.getItem_id_join_item().getName().getEn();
			}
			
			if(		weapon.getStat_name().equals("weapon_vehicle_kills") || 
					weapon.getStat_name().equals("weapon_headshots")	||
					weapon.getStat_name().equals("weapon_kills")){
				statMap = weaponMap;
			} else if(weapon.getStat_name().equals("weapon_killed_by")){
				statMap = weaponKilledMap;
			}else {
				continue;
			}
			
			if(!statMap.containsKey(weaponName)){
				stat = new WeaponStat();
				if(weapon.getItem_id_join_item() != null){
					stat.setImagePath(weapon.getItem_id_join_item().getImage_path());
				}
				stat.setName(weaponName);
				statMap.put(weaponName, stat);
			}else{
				stat = statMap.get(weaponName);
			}

			if(statMap == weaponKilledMap){
				stat.setKills(	weapon.getValue_nc() + 
								weapon.getValue_tr() + 
								weapon.getValue_vs());
				
			}else{
				if (profileFaction.equals(Faction.VS)){
					statA = weapon.getValue_tr();
					statB = weapon.getValue_nc();
				}else if(profileFaction.equals(Faction.NC)){
					statA = weapon.getValue_tr();
					statB = weapon.getValue_vs();
				}else if(profileFaction.equals(Faction.TR)){
					statA = weapon.getValue_vs();
					statB = weapon.getValue_nc();
				}
							
				if(weapon.getStat_name().equals("weapon_vehicle_kills")){
					stat.setVehicleKills(statA + statB);
				}else if(weapon.getStat_name().equals("weapon_headshots")){
					stat.setHeadshots(statA + statB);
				}else if(weapon.getStat_name().equals("weapon_kills")){
					stat.setKills(statA + statB);
					stat.setVS(weapon.getValue_vs());
					stat.setTR(weapon.getValue_tr());
					stat.setNC(weapon.getValue_nc());
				} 				
			}
		}
		weaponKillStats = new ArrayList<WeaponStat>(weaponMap.values());
		weaponKilledByStats = new ArrayList<WeaponStat>(weaponKilledMap.values());
		
		java.util.Collections.sort(weaponKillStats);
		java.util.Collections.sort(weaponKilledByStats);
		
		for (int i = weaponKillStats.size() - 1; i >= 0; i--){
			if(weaponKillStats.get(i).getKills() <= 0){
				weaponKillStats.remove(i);
			} else if(weaponKillStats.get(i).getKills() > 0){
				break;
			}
		}
		
		weaponKills = weaponKillStats;
		weaponKilledBy = weaponKilledByStats;
		
	    return weaponKilledByStats.size() + weaponKilledByStats.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Integer result) {
	    ListView listRoot = (ListView) getActivity().findViewById(R.id.listViewWeaponList);
	    listRoot.setAdapter(new WeaponItemAdapter(getActivity(), weaponKills, weaponKilledBy, profileFaction, FragmentWeaponList.this.fragmentMyWeapons.isChecked()));
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