package com.cesarandres.ps2link.soe.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.content.CharacterEvent;
import com.cesarandres.ps2link.soe.content.Faction;

public class KillItemAdapter extends BaseAdapter {

	private ArrayList<CharacterEvent> events;
	protected LayoutInflater mInflater;
	private String characterId;
    private Bitmap icon_vs;
    private Bitmap icon_nc;
    private Bitmap icon_tr;
    
	public KillItemAdapter(Context context, ArrayList<CharacterEvent> events, String characterId) {
		this.mInflater = LayoutInflater.from(context);
		this.events = events;
		this.characterId = characterId;
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
			convertView = mInflater.inflate(R.layout.layout_kill_item, null);

			holder = new ViewHolder();
			holder.action = (TextView) convertView.findViewById(R.id.textViewKillItemAction);
			holder.faction = (ImageView) convertView.findViewById(R.id.imageViewKillItemFactionIcon);
			holder.name = (TextView) convertView.findViewById(R.id.TextViewKillItemCharacterName);
			holder.time = (TextView) convertView.findViewById(R.id.TextViewKillItemTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Date date = new Date(Long.parseLong(getItem(position).getTimestamp() + "000"));
		SimpleDateFormat format = new SimpleDateFormat("MMM dd 'at' hh:mm:ss a");
		holder.time.setText(format.format(date));
		
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
		}

		return convertView;
	}

	static class ViewHolder {
		TextView action;
		ImageView faction;
		TextView name;
		TextView time;
	}
}