package com.cesarandres.ps2link.dbg.view;

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
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.content.Faction;
import com.cesarandres.ps2link.dbg.content.item.WeaponStat;
import com.cesarandres.ps2link.dbg.content.response.Item_list_response;
import com.cesarandres.ps2link.dbg.volley.GsonRequest;

import java.util.ArrayList;
import java.util.Hashtable;

public class WeaponItemAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    private boolean myWeapons;
    private Context context;
    private ArrayList<WeaponStat> weaponKills;
    private ArrayList<WeaponStat> weaponKilledBy;
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
        this.context = context;
    }

    @Override
    public int getCount() {
        if (this.myWeapons) {
            return this.weaponKills.size();
        } else {
            return this.weaponKilledBy.size();
        }
    }

    @Override
    public WeaponStat getItem(int position) {
        if (this.myWeapons) {
            return this.weaponKills.get(position);
        } else {
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
            holder.weaponImage.setErrorImageResId(R.drawable.image_not_found);
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

        if (stat.getVehicle() != null) {
            if (stat.getVehicle().equals(stat.getName())) {
                holder.name.setText(stat.getName());
            } else {
                holder.name.setText(stat.getName() + "(" + stat.getVehicle() + ")");
            }
        } else {
            holder.name.setText(stat.getName());
        }

        if (this.myWeapons) {
            holder.medal.setVisibility(View.VISIBLE);
            if (stat.getKills() < 10) {
                holder.medal.setImageBitmap(icon_empty);
            } else if (stat.getKills() < 60) {
                holder.medal.setImageBitmap(icon_copper);
            } else if (stat.getKills() < 160) {
                holder.medal.setImageBitmap(icon_silver);
            } else if (stat.getKills() <= 1160) {
                holder.medal.setImageBitmap(icon_gold);
            } else {
                holder.medal.setImageBitmap(icon_auraxium);
            }

            holder.kills.setText(this.context.getResources().getString(R.string.text_kills) + " " + stat.getKills());

            holder.ratios.setVisibility(View.VISIBLE);
            if (this.characterFaction.equals(Faction.VS)) {
                holder.ratios.setText(this.context.getResources().getString(R.string.text_nc_) + " " + Math.round(100 * stat.getNC() / (float) stat.getKills()) +
                        "% " + this.context.getResources().getString(R.string.text_tr_) + " " + Math.round(100 * stat.getTR() / (float) stat.getKills()) + "%");
            } else if (this.characterFaction.equals(Faction.NC)) {
                holder.ratios.setText(this.context.getResources().getString(R.string.text_tr_) + " " + Math.round(100 * stat.getTR() / (float) stat.getKills()) +
                        "% " + this.context.getResources().getString(R.string.text_vs_) + " " + Math.round(100 * stat.getVS() / (float) stat.getKills()) + "%");
            } else if (this.characterFaction.equals(Faction.TR)) {
                holder.ratios.setText(this.context.getResources().getString(R.string.text_nc_) + " " + Math.round(100 * stat.getNC() / (float) stat.getKills()) +
                        "% " + this.context.getResources().getString(R.string.text_vs_) + " " + Math.round(100 * stat.getVS() / (float) stat.getKills()) + "%");
            }

            holder.headshots.setVisibility(View.VISIBLE);
            holder.headshots.setText(this.context.getResources().getString(R.string.text_headshots_) + " " + stat.getHeadshots());

            holder.vehiclekills.setVisibility(View.VISIBLE);
            if (stat.getVehicleKills() > 0) {
                holder.vehiclekills.setText(this.context.getResources().getString(R.string.text_vehicle_kills_) + " " + stat.getVehicleKills());
            } else {
                holder.vehiclekills.setText("");
            }
        } else {
            holder.medal.setVisibility(View.GONE);
            holder.ratios.setVisibility(View.GONE);
            holder.kills.setText(this.context.getResources().getString(R.string.text_wia_killed_by) + " " + stat.getKills() + " " + this.context.getResources().getString(R.string.text_wia_times));
            holder.headshots.setVisibility(View.GONE);
            holder.vehiclekills.setVisibility(View.GONE);
        }

        holder.weaponImage.setImageUrl(DBGCensus.ENDPOINT_URL + "/" + stat.getImagePath(), ApplicationPS2Link.mImageLoader);

        return convertView;
    }

    public boolean isMyWeapons() {
        return myWeapons;
    }

    public void setMyWeapons(boolean myWeapons) {
        this.myWeapons = myWeapons;
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
}