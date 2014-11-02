package com.cesarandres.ps2link.soe.view;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.item.WeaponStat;
import com.cesarandres.ps2link.soe.content.response.Item_list_response;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

public class WeaponItemAdapter extends BaseAdapter {

	private boolean myWeapons;
	
    private ArrayList<WeaponStat> weaponKills;
    private ArrayList<WeaponStat> weaponKilledBy;
	
    protected LayoutInflater mInflater;
    private String characterFaction;
    private Bitmap icon_auraxium;
    private Bitmap icon_gold;
    private Bitmap icon_silver;
    private Bitmap icon_copper;
    private Bitmap icon_empty;

    private Hashtable<View, GsonRequest<Item_list_response>> requestTable;

    public WeaponItemAdapter(Context context, ArrayList<WeaponStat> weaponKills, ArrayList<WeaponStat> weaponKilledBy, String faction, boolean myKills) {
    this.myWeapons = myKills;
    this.mInflater = LayoutInflater.from(context);
	this.weaponKills = weaponKills;
	this.weaponKilledBy = weaponKilledBy;
	this.characterFaction = faction;
	requestTable = new Hashtable<View, GsonRequest<Item_list_response>>(20);
	icon_auraxium = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_araxium);
	icon_gold = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_gold);
	icon_silver = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_silver);
	icon_copper = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_copper);
	icon_empty = BitmapFactory.decodeResource(context.getResources(), R.drawable.medal_empty); 
	}
    
    @Override
    public int getCount() {
    	if(this.myWeapons){
    		return this.weaponKills.size();
    	}else{
    		return this.weaponKilledBy.size();
    	}
    }

    @Override
    public WeaponStat getItem(int position) {
    	if(this.myWeapons){
    		return this.weaponKills.get(position);
    	}else{
    		return this.weaponKilledBy.get(position);
    	}
	}

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.layout_weapon_item, parent, false);

	    holder = new ViewHolder();
	    holder.name = (TextView) convertView.findViewById(R.id.TextViewWeaponItemName);
	    holder.weaponImage = (NetworkImageView) convertView.findViewById(R.id.ImageViewWeaponItemImage);
	    holder.kills = (TextView) convertView.findViewById(R.id.TextViewWeaponItemKiils);
	    holder.ratios = (TextView) convertView.findViewById(R.id.TextViewWeaponItemRatio);
	    holder.headshots = (TextView) convertView.findViewById(R.id.TextViewWeaponItemHeadshots);
	    holder.vehiclekills = (TextView) convertView.findViewById(R.id.TextViewWeaponItemVehicleKills);
	    holder.medal = (ImageView) convertView.findViewById(R.id.imageViewWeaponItemMedal);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	    GsonRequest<Item_list_response> request = (requestTable.get(convertView));
	    if (request != null) {
		request.cancel();
	    }
	}

	WeaponStat stat = getItem(position);
	
	holder.name.setText(stat.getName());
	
	if(this.myWeapons){
		holder.medal.setVisibility(View.VISIBLE);
		if(stat.getKills() < 10){
			holder.medal.setImageBitmap(icon_empty);
		}else if(stat.getKills() < 60){
			holder.medal.setImageBitmap(icon_copper);
		}else if(stat.getKills() < 160){
			holder.medal.setImageBitmap(icon_silver);
		}else if(stat.getKills() <= 1160){
			holder.medal.setImageBitmap(icon_gold);
		}else {
			holder.medal.setImageBitmap(icon_auraxium);
		}
		
		holder.kills.setText("Kills: " + stat.getKills());
		
		holder.ratios.setVisibility(View.VISIBLE);
		if (this.characterFaction.equals(Faction.VS)){
			holder.ratios.setText(	"NC: " + Math.round(100 * stat.getNC() / (float)stat.getKills()) +
									"% TR: " + Math.round(100 * stat.getTR() / (float)stat.getKills()) + "%");
		}else if(this.characterFaction.equals(Faction.NC)){
			holder.ratios.setText(	"TR: " + Math.round( 100 * stat.getTR() / (float)stat.getKills()) + 
									"% VS: " + Math.round( 100 * stat.getVS() / (float)stat.getKills()) + "%");
		}else if(this.characterFaction.equals(Faction.TR)){
			holder.ratios.setText(	"NC: " + Math.round( 100 * stat.getNC() / (float)stat.getKills()) + 
									"% VS: " + Math.round( 100 * stat.getVS() / (float)stat.getKills()) + "%");
		}
		
		holder.headshots.setVisibility(View.VISIBLE);
		holder.headshots.setText("Headshots: " + stat.getHeadshots());

		//TODO Externalize strings
		holder.vehiclekills.setVisibility(View.VISIBLE);
		if(stat.getVehicleKills() > 0){
			holder.vehiclekills.setText("Vehicle Kills: " + stat.getVehicleKills());
		}else{
			holder.vehiclekills.setText("");
		}
	}else{
		holder.medal.setVisibility(View.GONE);
		holder.ratios.setVisibility(View.GONE);
		holder.kills.setText("Killed: " + stat.getKills());
		holder.headshots.setVisibility(View.GONE);
		holder.vehiclekills.setVisibility(View.GONE);
	}

	//TODO Externalize strings
	holder.weaponImage.setImageUrl(SOECensus.ENDPOINT_URL + "/" + stat.getImagePath(), ApplicationPS2Link.mImageLoader);
	
	return convertView;
    }

    static class ViewHolder {
	TextView name;
	NetworkImageView weaponImage;
	ImageView medal;
	TextView kills;
	TextView ratios;
	TextView headshots;
	TextView vehiclekills;
    }

	public boolean isMyWeapons() {
		return myWeapons;
	}

	public void setMyWeapons(boolean myWeapons) {
		this.myWeapons = myWeapons;
	}
}