package com.cesarandres.ps2link.soe.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.content.character.Stat;
import com.cesarandres.ps2link.soe.content.character.Stat.Day;

public class StatItemAdapter extends BaseAdapter {

	private ArrayList<Stat> stats;
	protected LayoutInflater mInflater;

	public StatItemAdapter(Context context, ArrayList<Stat> stats, String characterId) {
		this.mInflater = LayoutInflater.from(context);
		this.stats = stats;
		Stat kills = null;
		Stat deaths = null;
		for (Stat stat : stats) {
			if (stat.getStat_name().equals("kills")) {
				kills = stat;
			} else if (stat.getStat_name().equals("deaths")) {
				deaths = stat;
			}
		}
		Stat kdr = new Stat();
		if (deaths.getAll_time().equals("0")) {
			deaths.setAll_time("1");
		}
		if (deaths.getToday() == 0) {
			deaths.setToday(1);
		}
		if (deaths.getThisWeek() == 0) {
			deaths.setThisWeek(1);
		}
		if (deaths.getThisMonth() == 0) {
			deaths.setThisWeek(1);
		}
		kdr.setDay(kdr.new Day());
		kdr.setWeek(kdr.new Week());
		kdr.setMonth(kdr.new Month());

		kdr.setStat_name("KDR");
		kdr.setAll_time(Float.toString(Float.parseFloat(kills.getAll_time()) / Float.parseFloat(deaths.getAll_time())));
		kdr.setToday(kills.getToday() / deaths.getToday());
		kdr.setThisWeek(kills.getThisWeek() / deaths.getThisWeek());
		kdr.setThisMonth(kills.getThisMonth() / deaths.getThisMonth());
		this.stats.add(0, kdr);

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
			convertView = mInflater.inflate(R.layout.layout_stat_item, null);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.TextViewStatItemName);
			holder.total = (TextView) convertView.findViewById(R.id.TextViewStatItemTotal);
			holder.today = (TextView) convertView.findViewById(R.id.TextViewStatItemToday);
			holder.week = (TextView) convertView.findViewById(R.id.TextViewStatItemWeek);
			holder.month = (TextView) convertView.findViewById(R.id.TextViewStatItemMonth);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (getItem(position).getStat_name().equals("time")) {
			holder.name.setText("TIME PLAYED");
			holder.total.setText("Total: " + (Integer.parseInt(getItem(position).getAll_time()) / 3600) + " hours");
			holder.today.setText("Today: " + (Integer.parseInt(getItem(position).getDay().d01) / 3600) + " hours");
			holder.week.setText("This week: " + (Integer.parseInt(getItem(position).getWeek().w01) / 3600) + " hours");
			holder.month.setText("This month: " + (Integer.parseInt(getItem(position).getMonth().m01) / 3600) + " hours");
		} else {
			holder.name.setText(getItem(position).getStat_name().toUpperCase().replaceAll("_", " "));
			holder.total.setText("All Time: " + getItem(position).getAll_time());
			holder.today.setText("Today: " + getItem(position).getDay().d01);
			holder.week.setText("This week: " + getItem(position).getWeek().w01);
			holder.month.setText("This month: " + getItem(position).getMonth().m01);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView total;
		TextView today;
		TextView week;
		TextView month;
	}
}