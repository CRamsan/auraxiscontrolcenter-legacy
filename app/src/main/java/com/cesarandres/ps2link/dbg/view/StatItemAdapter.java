package com.cesarandres.ps2link.dbg.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.dbg.content.character.Stat;

import java.util.ArrayList;

public class StatItemAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    private ArrayList<Stat> stats;
    private Context context;

    public StatItemAdapter(Context context, ArrayList<Stat> stats, String characterId) {
        this.mInflater = LayoutInflater.from(context);
        this.stats = stats;
        this.context = context;
        Stat kills = null;
        Stat deaths = null;
        Stat score = null;
        Stat time = null;
        for (Stat stat : stats) {
            if (stat.getStat_name().equals("kills")) {
                kills = stat;
            } else if (stat.getStat_name().equals("deaths")) {
                deaths = stat;
            } else if (stat.getStat_name().equals("score")) {
                score = stat;
            } else if (stat.getStat_name().equals("time")) {
                time = stat;
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

        kdr.setStat_name("kdr");
        kdr.setAll_time(Float.toString(Float.parseFloat(kills.getAll_time()) / Float.parseFloat(deaths.getAll_time())));
        kdr.setToday(kills.getToday() / deaths.getToday());
        kdr.setThisWeek(kills.getThisWeek() / deaths.getThisWeek());
        kdr.setThisMonth(kills.getThisMonth() / deaths.getThisMonth());
        this.stats.add(0, kdr);

        Stat sph = new Stat();
        if (time.getAll_time().equals("0")) {
            time.setAll_time("1");
        }
        if (time.getToday() == 0) {
            time.setToday(1);
        }
        if (time.getThisWeek() == 0) {
            time.setThisWeek(1);
        }
        if (time.getThisMonth() == 0) {
            time.setThisWeek(1);
        }
        sph.setDay(sph.new Day());
        sph.setWeek(sph.new Week());
        sph.setMonth(sph.new Month());

        sph.setStat_name("scorehour");
        sph.setAll_time(Float.toString(Float.parseFloat(score.getAll_time()) / (Float.parseFloat(time.getAll_time()) / 3600f)));
        sph.setToday(score.getToday() / (time.getToday() / 3600f));
        sph.setThisWeek(score.getThisWeek() / (time.getThisWeek() / 3600f));
        sph.setThisMonth(score.getThisMonth() / (time.getThisMonth() / 3600f));
        this.stats.add(0, sph);

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
            convertView = mInflater.inflate(R.layout.layout_stat_item, parent, false);

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

        Stat stat = getItem(position);
        if (stat.getStat_name().equals("time")) {
            holder.name.setText(R.string.text_time_played_caps);
            String hours = context.getResources().getString(R.string.text_hours);
            holder.total.setText(context.getResources().getString(R.string.text_stat_all) + " " + (Float.valueOf(stat.getAll_time()).intValue() / 3600) + " " + hours);
            holder.today.setText(context.getResources().getString(R.string.text_stat_today) + " " + (Float.valueOf(stat.getDay().d01).intValue() / 3600) + " " + hours);
            holder.week.setText(context.getResources().getString(R.string.text_stat_week) + " " + (Float.valueOf(stat.getWeek().w01).intValue() / 3600) + " " + hours);
            holder.month.setText(context.getResources().getString(R.string.text_stat_month) + " " + (Float.valueOf(stat.getMonth().m01).intValue() / 3600) + " " + hours);
        } else {
            String statName = "";

            if (stat.getStat_name().equalsIgnoreCase("battle_rank")) {
                statName = context.getResources().getString(R.string.text_stat_battle_rank);
            } else if (stat.getStat_name().equalsIgnoreCase("certs")) {
                statName = context.getResources().getString(R.string.text_stat_certs);
            } else if (stat.getStat_name().equalsIgnoreCase("deaths")) {
                statName = context.getResources().getString(R.string.text_stat_deaths);
            } else if (stat.getStat_name().equalsIgnoreCase("facility_capture")) {
                statName = context.getResources().getString(R.string.text_stat_fac_captured);
            } else if (stat.getStat_name().equalsIgnoreCase("facility_defend")) {
                statName = context.getResources().getString(R.string.text_stat_fac_defended);
            } else if (stat.getStat_name().equalsIgnoreCase("kills")) {
                statName = context.getResources().getString(R.string.text_stat_kills);
            } else if (stat.getStat_name().equalsIgnoreCase("medals")) {
                statName = context.getResources().getString(R.string.text_stat_medals);
            } else if (stat.getStat_name().equalsIgnoreCase("ribbons")) {
                statName = context.getResources().getString(R.string.text_stat_ribbons);
            } else if (stat.getStat_name().equalsIgnoreCase("score")) {
                statName = context.getResources().getString(R.string.text_stat_score);
            } else if (stat.getStat_name().equalsIgnoreCase("kdr")) {
                statName = context.getResources().getString(R.string.text_stat_kdr);
            } else if (stat.getStat_name().equalsIgnoreCase("scorehour")) {
                statName = context.getResources().getString(R.string.text_stat_score_hour);
            }

            holder.name.setText(statName);
            holder.total.setText(context.getResources().getString(R.string.text_stat_all) + " " + stat.getAll_time());
            holder.today.setText(context.getResources().getString(R.string.text_stat_today) + " " + stat.getDay().d01);
            holder.week.setText(context.getResources().getString(R.string.text_stat_week) + " " + stat.getWeek().w01);
            holder.month.setText(context.getResources().getString(R.string.text_stat_month) + " " + stat.getMonth().m01);
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