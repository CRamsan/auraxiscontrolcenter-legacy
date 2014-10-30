package com.cesarandres.ps2link.soe.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterEvent;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.item.IContainDrawable;
import com.cesarandres.ps2link.soe.content.response.Item_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

public class KillItemAdapter extends BaseAdapter {

    private ArrayList<CharacterEvent> events;
    protected LayoutInflater mInflater;
    private String characterId;
    private Bitmap icon_vs;
    private Bitmap icon_nc;
    private Bitmap icon_tr;

    private Hashtable<View, GsonRequest<Item_list_response>> requestTable;

    public KillItemAdapter(Context context, ArrayList<CharacterEvent> events, String characterId) {
	this.mInflater = LayoutInflater.from(context);
	this.events = events;
	this.characterId = characterId;
	requestTable = new Hashtable<View, GsonRequest<Item_list_response>>(20);
	icon_vs = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_faction_vs);
	icon_tr = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_faction_tr);
	icon_nc = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_faction_nc);
    }

    @Override
    public int getCount() {
	return this.events.size();
    }

    @Override
    public CharacterEvent getItem(int position) {
	return events.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.layout_kill_item, parent, false);

	    holder = new ViewHolder();
	    holder.action = (TextView) convertView.findViewById(R.id.textViewKillItemAction);
	    holder.faction = (ImageView) convertView.findViewById(R.id.imageViewKillItemFactionIcon);
	    holder.name = (TextView) convertView.findViewById(R.id.TextViewKillItemCharacterName);
	    holder.time = (TextView) convertView.findViewById(R.id.TextViewKillItemTime);
	    holder.weaponName = (TextView) convertView.findViewById(R.id.TextViewKillItemWeaponName);
	    holder.weaponImage = (NetworkImageView) convertView.findViewById(R.id.ImageViewKillItemWeaponImage);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	    GsonRequest<Item_list_response> request = (requestTable.get(convertView));
	    if (request != null) {
		request.cancel();
	    }
	}

	if (getItem(position).getWeapon_name() != null) {
	    holder.weaponName.setText(getItem(position).getWeapon_name());
	    holder.weaponImage.setImageUrl(getItem(position).getImagePath(), ApplicationPS2Link.mImageLoader);
	} else {
	    holder.weaponName.setText("Loading...");
	    holder.weaponImage.setImageUrl("", null);

	    CharacterEvent event = getItem(position);
	    String weapongId = event.getAttacker_weapon_id();
	    if (!weapongId.equals("0")) {
		downloadPictures(getItem(position).getAttacker_weapon_id(), PS2Collection.ITEM, holder.weaponName, holder.weaponImage, position, convertView);
	    } else if (!getItem(position).getAttacker_vehicle_id().equals("0")) {
		downloadPictures(getItem(position).getAttacker_vehicle_id(), PS2Collection.VEHICLE, holder.weaponName, holder.weaponImage, position,
			convertView);
	    } else {
	    }
	}

	Date date = new Date(Long.parseLong(getItem(position).getTimestamp() + "000"));
	SimpleDateFormat format = new SimpleDateFormat("MMM dd 'at' hh:mm:ss a", Locale.getDefault() );
	holder.time.setText(format.format(date));

	try {
	    if (getItem(position).getAttacker().getCharacter_Id().equals(this.characterId)) {
		holder.name.setText(getItem(position).getCharacter().getName().getFirst());
		getItem(position).setImportant_character_id(getItem(position).getCharacter_id());
		if (getItem(position).getCharacter_id().equals(this.characterId)) {
		    holder.action.setText("SUICIDE");
		    holder.action.setTextColor(Color.RED);
		} else {
		    holder.action.setText("KILLED");
		    holder.action.setTextColor(Color.GREEN);
		}
		if (getItem(position).getCharacter().getFaction_id().equals(Faction.VS)) {
		    holder.faction.setImageBitmap(icon_vs);
		} else if (getItem(position).getCharacter().getFaction_id().equals(Faction.NC)) {
		    holder.faction.setImageBitmap(icon_nc);
		} else if (getItem(position).getCharacter().getFaction_id().equals(Faction.TR)) {
		    holder.faction.setImageBitmap(icon_tr);
		}
	    } else if (getItem(position).getCharacter_id().equals(this.characterId)) {
		holder.action.setText("KILLED BY");
		holder.action.setTextColor(Color.RED);
		holder.name.setText(getItem(position).getAttacker().getName().getFirst());
		getItem(position).setImportant_character_id(getItem(position).getAttacker_character_id());
		if (getItem(position).getAttacker().getFaction_id().equals(Faction.VS)) {
		    holder.faction.setImageBitmap(icon_vs);
		} else if (getItem(position).getAttacker().getFaction_id().equals(Faction.NC)) {
		    holder.faction.setImageBitmap(icon_nc);
		} else if (getItem(position).getAttacker().getFaction_id().equals(Faction.TR)) {
		    holder.faction.setImageBitmap(icon_tr);
		}
	    }
	} catch (NullPointerException e) {

	}

	return convertView;
    }

    static class ViewHolder {
	TextView action;
	ImageView faction;
	TextView name;
	TextView weaponName;
	NetworkImageView weaponImage;
	TextView time;
    }

    public void downloadPictures(String resource_id, PS2Collection collection, final TextView name, final NetworkImageView image, final int position, View view) {
	String url = SOECensus.generateGameDataRequest(Verb.GET, collection, resource_id, null).toString();
	Listener<Item_list_response> success = new Response.Listener<Item_list_response>() {
	    @Override
	    public void onResponse(Item_list_response response) {
		IContainDrawable item = null;

		if (response.getItem_list() != null && response.getItem_list().size() > 0) {
			item = response.getItem_list().get(0);
		} else if (response.getVehicle_list() != null && response.getVehicle_list().size() > 0) {
			item = response.getVehicle_list().get(0);
		}

		events.get(position).setWeapon_name(item.getNameText());
		name.setText(item.getNameText());

		events.get(position).setImagePath(SOECensus.ENDPOINT_URL + "/" + item.getImagePath());
		//image.setImageUrl(SOECensus.ENDPOINT_URL + "/" + item.getImagePath(), ApplicationPS2Link.mImageLoader);
	    }
	};
	GsonRequest<Item_list_response> request = new GsonRequest<Item_list_response>(url.toString(), Item_list_response.class, null, success, null);
	requestTable.put(view, request);
	ApplicationPS2Link.volley.add(request);
    }
}