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
import android.widget.ImageView;
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
	    holder.alertObjective = (ImageView) convertView.findViewById(R.id.imageViewAlertTarget);
	    holder.alertTerritory = (ImageView) convertView.findViewById(R.id.imageViewAlertLocation);
	    holder.alertStatus = (ImageView) convertView.findViewById(R.id.ImageViewAlertStatus);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	holder.serverName.setText(getItem(position).getWorld().getName().getEn());

	int alertDuration = 3600;
	String metagameId = getItem(position).getMetagame_event_id();
	if (metagameId.equals("1")) {
	    holder.eventType.setText("Territory - Indar");
	    holder.alertTerritory.setImageResource(R.drawable.indar);
	    holder.alertObjective.setImageResource(R.drawable.hex);
	    alertDuration *= 2;
	} else if (metagameId.equals("2")) {
	    holder.eventType.setText("Territory - Esamir");
	    holder.alertTerritory.setImageResource(R.drawable.esamir);
	    holder.alertObjective.setImageResource(R.drawable.hex);
	    alertDuration *= 2;
	} else if (metagameId.equals("3")) {
	    holder.eventType.setText("Territory - Amerish");
	    holder.alertTerritory.setImageResource(R.drawable.amerish);
	    holder.alertObjective.setImageResource(R.drawable.hex);
	    alertDuration *= 2;
	} else if (metagameId.equals("4")) {
	    holder.eventType.setText("Biolabs - Worldwide");
	    holder.alertTerritory.setImageResource(R.drawable.world);
	    holder.alertObjective.setImageResource(R.drawable.biolab);
	    alertDuration *= 2;
	} else if (metagameId.equals("5")) {
	    holder.eventType.setText("Tech Plants - Worldwide");
	    holder.alertTerritory.setImageResource(R.drawable.world);
	    holder.alertObjective.setImageResource(R.drawable.techplant);
	    alertDuration *= 2;
	} else if (metagameId.equals("6")) {
	    holder.eventType.setText("Amp Stations - Worldwide");
	    holder.alertTerritory.setImageResource(R.drawable.world);
	    holder.alertObjective.setImageResource(R.drawable.ampstation);
	    alertDuration *= 2;
	} else if (metagameId.equals("7")) {
	    holder.eventType.setText("Biolabs - Amerish");
	    holder.alertTerritory.setImageResource(R.drawable.amerish);
	    holder.alertObjective.setImageResource(R.drawable.biolab);
	} else if (metagameId.equals("8")) {
	    holder.eventType.setText("Tech Plants - Amerish");
	    holder.alertTerritory.setImageResource(R.drawable.amerish);
	    holder.alertObjective.setImageResource(R.drawable.techplant);
	} else if (metagameId.equals("9")) {
	    holder.eventType.setText("Amp Stations - Amerish");
	    holder.alertTerritory.setImageResource(R.drawable.amerish);
	    holder.alertObjective.setImageResource(R.drawable.ampstation);
	} else if (metagameId.equals("10")) {
	    holder.eventType.setText("Biolabs - Indar");
	    holder.alertTerritory.setImageResource(R.drawable.indar);
	    holder.alertObjective.setImageResource(R.drawable.biolab);
	} else if (metagameId.equals("11")) {
	    holder.eventType.setText("Tech Plants - Indar");
	    holder.alertTerritory.setImageResource(R.drawable.indar);
	    holder.alertObjective.setImageResource(R.drawable.techplant);
	} else if (metagameId.equals("12")) {
	    holder.eventType.setText("Amp Stations - Indar");
	    holder.alertTerritory.setImageResource(R.drawable.indar);
	    holder.alertObjective.setImageResource(R.drawable.ampstation);
	} else if (metagameId.equals("13")) {
	    holder.eventType.setText("Biolabs - Esamir");
	    holder.alertTerritory.setImageResource(R.drawable.esamir);
	    holder.alertObjective.setImageResource(R.drawable.biolab);
	} else if (metagameId.equals("14")) {
	    holder.eventType.setText("Amp Stations - Esamir");
	    holder.alertTerritory.setImageResource(R.drawable.esamir);
	    holder.alertObjective.setImageResource(R.drawable.ampstation);
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
		holder.alertStatus.setImageResource(R.drawable.alert);
	    } else {
		holder.status.setText("Done");
		holder.alertStatus.setImageResource(R.drawable.alertoff);
		holder.timeRemaining.setText(null);
	    }
	} else if (getItem(position).getMetagame_event_state().equals("138")) {
	    holder.status.setText("Done");
	    holder.timeRemaining.setText(null);
	    holder.alertStatus.setImageResource(R.drawable.alertoff);
	} else {
	    holder.alertStatus.setImageResource(R.drawable.alertoff);
	}

	if (metagameId.equals("1") || metagameId.equals("2") || metagameId.equals("3")) {
	    holder.TRScores.setText("TR: " + Math.round(Float.parseFloat(getItem(position).getFaction_tr())) + "%");
	    holder.NCScores.setText("NC: " + Math.round(Float.parseFloat(getItem(position).getFaction_nc())) + "%");
	    holder.VSScores.setText("VS: " + Math.round(Float.parseFloat(getItem(position).getFaction_vs())) + "%");
	} else {
	    holder.TRScores.setText("Score Not Available");
	    holder.NCScores.setText(null);
	    holder.VSScores.setText(null);
	}
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
	ImageView alertObjective;
	ImageView alertTerritory;
	ImageView alertStatus;
    }
}
