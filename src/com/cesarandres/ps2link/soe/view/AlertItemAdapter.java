package com.cesarandres.ps2link.soe.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.content.WorldEvent;

public class AlertItemAdapter extends BaseAdapter {

    private ArrayList<WorldEvent> events;
    protected LayoutInflater mInflater;

    public AlertItemAdapter(Context context, ArrayList<WorldEvent> events) {
	this.mInflater = LayoutInflater.from(context);
	this.events = events;
    }

    @Override
    public int getCount() {
	return this.events.size();
    }

    @Override
    public WorldEvent getItem(int position) {
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
	    convertView = mInflater.inflate(R.layout.layout_alert_item, null);

	    holder = new ViewHolder();
	    holder.serverName = (TextView) convertView.findViewById(R.id.textViewServerName);
	    holder.timeRemaining = (TextView) convertView.findViewById(R.id.textViewTimeRemaining);
	    holder.eventType= (TextView) convertView.findViewById(R.id.textViewEventType);
	    holder.continent = (TextView) convertView.findViewById(R.id.textViewContinent);
	    holder.TRScores = (TextView) convertView.findViewById(R.id.textViewTRScore);
	    holder.VSScores = (TextView) convertView.findViewById(R.id.textViewNCScore);
	    holder.NCScores = (TextView) convertView.findViewById(R.id.textViewVSScore);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	holder.serverName.setText(getItem(position).getWorld().getName().getEn());
	holder.timeRemaining.setText(getItem(position).getTimestamp());
	holder.eventType.setText(getItem(position).getEvent().getDescription().getEn());
	holder.continent.setText(getItem(position).getWorld().getName().getEn());
	holder.TRScores.setText(getItem(position).getFaction_tr());
	holder.NCScores.setText(getItem(position).getFaction_nc());
	holder.VSScores.setText(getItem(position).getFaction_vs());
	return convertView;
    }

    static class ViewHolder {
	TextView serverName;
	TextView timeRemaining;
	TextView eventType;
	TextView continent;
	TextView TRScores;
	TextView NCScores;
	TextView VSScores;
    }
}
