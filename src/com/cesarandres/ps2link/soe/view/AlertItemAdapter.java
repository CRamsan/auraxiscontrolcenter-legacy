package com.cesarandres.ps2link.soe.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;

import android.content.Context;
import android.os.Handler;
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
    protected Date currentDate;
    protected Handler mHandler;
    protected Thread worker;
    protected boolean runWorker = true;

    public AlertItemAdapter(Context context, ArrayList<WorldEvent> events) {
	this.mInflater = LayoutInflater.from(context);
	this.events = new ArrayList<WorldEvent>(6);
	LinkedHashSet<String> eventMap = new LinkedHashSet<String>();
	for (WorldEvent event : events) {
	    String worldName = event.getWorld().getName().getEn();
	    if (!eventMap.contains(worldName)) {
		eventMap.add(worldName);
		this.events.add(event);
	    }
	}

	this.currentDate = new Date();

	this.mHandler = new Handler();
	this.worker = new Thread(new Runnable() {
	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		while (runWorker) {
		    try {
			Thread.sleep(1000);
			mHandler.post(new Runnable() {

			    @Override
			    public void run() {
				AlertItemAdapter.this.currentDate = new Date();
				AlertItemAdapter.this.notifyDataSetChanged();
			    }
			});
		    } catch (Exception e) {
			// TODO: handle exception
		    }
		}
	    }
	});
	this.worker.start();
    }

    public void stopWorker() {
	this.runWorker = false;
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
	    holder.eventType = (TextView) convertView.findViewById(R.id.textViewEventType);
	    holder.status = (TextView) convertView.findViewById(R.id.textViewStatus);
	    holder.TRScores = (TextView) convertView.findViewById(R.id.textViewTRScore);
	    holder.VSScores = (TextView) convertView.findViewById(R.id.textViewNCScore);
	    holder.NCScores = (TextView) convertView.findViewById(R.id.textViewVSScore);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	holder.serverName.setText(getItem(position).getWorld().getName().getEn());

	int alertDuration = 3600;
	String metagameId = getItem(position).getMetagame_event_id();
	if (metagameId.equals("1")) {
	    holder.eventType.setText("Territory - Indar");
	    alertDuration *= 2;
	} else if (metagameId.equals("2")) {
	    holder.eventType.setText("Territory - Esamir");
	    alertDuration *= 2;
	} else if (metagameId.equals("3")) {
	    holder.eventType.setText("Territory - Amerish");
	    alertDuration *= 2;
	} else if (metagameId.equals("4")) {
	    holder.eventType.setText("Biolabs - Worldwide");
	    alertDuration *= 2;
	} else if (metagameId.equals("5")) {
	    holder.eventType.setText("Tech Plants - Worldwide");
	    alertDuration *= 2;
	} else if (metagameId.equals("6")) {
	    holder.eventType.setText("Amp Stations - Worldwide");
	    alertDuration *= 2;
	} else if (metagameId.equals("7")) {
	    holder.eventType.setText("Biolabs - Amerish");
	} else if (metagameId.equals("8")) {
	    holder.eventType.setText("Tech Plants - Amerish");
	} else if (metagameId.equals("9")) {
	    holder.eventType.setText("Amp Stations - Amerish");
	} else if (metagameId.equals("10")) {
	    holder.eventType.setText("Biolabs - Indar");
	} else if (metagameId.equals("11")) {
	    holder.eventType.setText("Tech Plants - Indar");
	} else if (metagameId.equals("12")) {
	    holder.eventType.setText("Amp Stations - Indar");
	} else if (metagameId.equals("13")) {
	    holder.eventType.setText("Biolabs - Esamir");
	} else if (metagameId.equals("14")) {
	    holder.eventType.setText("Amp Stations - Esamir");
	} else {
	}

	if (getItem(position).getMetagame_event_state().equals("135")) {
	    holder.status.setText("Ongoing");
	    int timeInSeconds = alertDuration - (int) ((currentDate.getTime() / 1000 - Integer.parseInt(getItem(position).getTimestamp())));
	    if (timeInSeconds > 0) {
		int hours = (timeInSeconds / 3600);
		int minutes = (timeInSeconds % 3600) / 60;
		int seconds = (timeInSeconds % 3600) % 60;
		holder.timeRemaining.setText(hours + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds));
	    } else {
		holder.status.setText("Done");
		holder.timeRemaining.setText(null);
	    }
	} else if (getItem(position).getMetagame_event_state().equals("138")) {
	    holder.status.setText("Done");
	    holder.timeRemaining.setText(null);
	} else {
	    holder.status.setText("SOMETHING");
	}

	holder.TRScores.setText(getItem(position).getFaction_tr());
	holder.NCScores.setText(getItem(position).getFaction_nc());
	holder.VSScores.setText(getItem(position).getFaction_vs());
	return convertView;
    }

    static class ViewHolder {
	TextView serverName;
	TextView timeRemaining;
	TextView eventType;
	TextView status;
	TextView TRScores;
	TextView NCScores;
	TextView VSScores;
    }
}
