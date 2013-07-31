package com.cesarandres.ps2link.soe.adapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import android.content.Context;
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
import com.cesarandres.ps2link.soe.SOECensus.Game;
import com.cesarandres.ps2link.soe.SOECensus.Verb;
import com.cesarandres.ps2link.soe.content.CharacterEvent;
import com.cesarandres.ps2link.soe.content.Faction;
import com.cesarandres.ps2link.soe.content.character.Stat;
import com.cesarandres.ps2link.soe.content.item.IContainDrawable;
import com.cesarandres.ps2link.soe.content.response.Item_list_response;
import com.cesarandres.ps2link.soe.util.Collections.PS2Collection;
import com.cesarandres.ps2link.soe.volley.GsonRequest;

public class StatItemAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*private ArrayList<Stat> stats;
	protected LayoutInflater mInflater;
	private String characterId;

	public StatItemAdapter(Context context, ArrayList<Stat> stats, String characterId) {
		this.mInflater = LayoutInflater.from(context);
		this.stats = stats;
		this.characterId = characterId;
	}

	@Override
	public int getCount() {
		return this.stats.size();
	}

	@Override
	public Stat getItem(int position) {
		return stats.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_kill_item, null);

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
			GsonRequest<?> request = (requestTable.get(convertView));
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
		SimpleDateFormat format = new SimpleDateFormat("MMM dd 'at' hh:mm:ss a");
		holder.time.setText(format.format(date));

		try {
			if (getItem(position).getAttacker().getId().equals(this.characterId)) {
				holder.name.setText(getItem(position).getCharacter().getName().getFirst());
				if (getItem(position).getCharacter().getId().equals(this.characterId)) {
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
				getItem(position).setImportant_character_id(getItem(position).getCharacter_id());

			} else if (getItem(position).getCharacter().getId().equals(this.characterId)) {
				holder.action.setText("KILLED BY");
				holder.action.setTextColor(Color.RED);
				holder.name.setText(getItem(position).getAttacker().getName().getFirst());
				if (getItem(position).getAttacker().getFaction_id().equals(Faction.VS)) {
					holder.faction.setImageBitmap(icon_vs);
				} else if (getItem(position).getAttacker().getFaction_id().equals(Faction.NC)) {
					holder.faction.setImageBitmap(icon_nc);
				} else if (getItem(position).getAttacker().getFaction_id().equals(Faction.TR)) {
					holder.faction.setImageBitmap(icon_tr);
				}
				getItem(position).setImportant_character_id(getItem(position).getAttacker_character_id());
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

		URL url;
		try {

			url = SOECensus.generateGameDataRequest(Verb.GET, Game.PS2V2, collection, resource_id, null);
			Listener<Item_list_response> success = new Response.Listener<Item_list_response>() {
				@Override
				public void onResponse(Item_list_response response) {
					IContainDrawable item = null;

					if (response.getItem_list() != null) {
						item = response.getItem_list().get(0);
					} else if (response.getVehicle_list() != null) {
						item = response.getVehicle_list().get(0);
					}

					events.get(position).setWeapon_name(item.getNameText());
					name.setText(item.getNameText());

					events.get(position).setImagePath(SOECensus.ENDPOINT_URL + "/" + item.getImagePath());
					image.setImageUrl(SOECensus.ENDPOINT_URL + "/" + item.getImagePath(), ApplicationPS2Link.mImageLoader);
				}
			};
			GsonRequest<Item_list_response> request = new GsonRequest<Item_list_response>(url.toString(), Item_list_response.class, null, success, null);
			requestTable.put(view, request);
			ApplicationPS2Link.volley.add(request);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}