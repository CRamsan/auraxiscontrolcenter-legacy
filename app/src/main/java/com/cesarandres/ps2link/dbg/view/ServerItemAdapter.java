package com.cesarandres.ps2link.dbg.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.content.World;
import com.cesarandres.ps2link.dbg.content.WorldEvent;
import com.cesarandres.ps2link.dbg.content.response.server.PS2;
import com.cesarandres.ps2link.dbg.util.Logger;
import com.cesarandres.ps2link.fragments.FragmentSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.*;

public class ServerItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<World> serverList;
    private Context context;
    private HashMap<CompoundButton.OnCheckedChangeListener, String> channelMap;
    private boolean notificationsEnabled;

    public ServerItemAdapter(Context context, List<World> serverList) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context);
        this.serverList = new ArrayList<World>(serverList);
        this.channelMap = new HashMap<>();
        this.context = context;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        notificationsEnabled = settings.getBoolean(FragmentSettings.PREF_KEY_NOTIFICATION_ENABLE, false);

        for (World world : this.serverList) {
            String channel = DBGCensus.currentNamespace.toString() + "-" + world.getWorld_id();
            channel = channel.replace(":v2","");
            world.setIsRegistered(settings.getBoolean("parse_" + channel, false));
            // REMOVE THIS TO ENABLE PUSH NOTIFICATIONS
            world.setIsRegistered(false);
        }
    }

    /**
     * This method will use the LiveServer object to pass population information
     * to the list of servers found in the API
     *
     * @param serverList List of servers with population information
     */
    public void setServerPopulation(PS2 serverList) {
        String name, population = "";
        for (World world : this.serverList) {
            name = world.getName().getLocalizedName();
            try {
                if (name.equals("Briggs")) {
                    population = serverList.getLive().getBriggs().getStatus();
                } else if (name.equals("Connery")) {
                    population = serverList.getLive().getConnery().getStatus();
                } else if (name.equals("Miller")) {
                    population = serverList.getLive().getMiller().getStatus();
                } else if (name.equals("Cobalt")) {
                    population = serverList.getLive().getCobalt().getStatus();
                } else if (name.equals("Ceres")) {
                    population = serverList.getLivePS4().getCeres().getStatus();
                } else if (name.equals("Crux")) {
                    population = serverList.getLivePS4().getCrux().getStatus();
                } else if (name.equals("Dahaka")) {
                    population = serverList.getLivePS4().getDahaka().getStatus();
                } else if (name.equals("Genudine")) {
                    population = serverList.getLivePS4().getGenudine().getStatus();
                } else if (name.equals("Lithcorp")) {
                    population = serverList.getLivePS4().getLithcorp().getStatus();
                } else if (name.equals("Palos")) {
                    population = serverList.getLivePS4().getPalos().getStatus();
                } else if (name.equals("Rashnu")) {
                    population = serverList.getLivePS4().getRashnu().getStatus();
                } else if (name.equals("Searhus")) {
                    population = serverList.getLivePS4().getSearhus().getStatus();
                } else if (name.equals("Emerald") || name.equals("Smaragd") || name.equals("Esmeralda") || name.equals("Smeraldo")) {
                    population = serverList.getLive().getEmerald().getStatus();
                } else {
                    population = null;
                }
            } catch (NullPointerException e) {
                Logger.log(Log.INFO, this, "Population info not available for server " + name);
                population = null;
            }
            world.setPopulation(population);
        }

        this.notifyDataSetChanged();
    }

    public void setServerAlert(WorldEvent event){
        for (World world : this.serverList) {
            if(world.getWorld_id().equals(event.getWorld_id())) {
                world.setLastAlert(event);
            }
        }

        this.notifyDataSetChanged();
    }

    public void onItemSelected(int index, Context context) {
        World world = this.serverList.get(index);
        String channel = DBGCensus.currentNamespace.toString() + "-" +
                        this.serverList.get(index).getWorld_id();
        channel = channel.replace(":v2","");
        if (world.isRegistered()) {
            //ParsePush.unsubscribeInBackground(channel);
        } else {
            //ParsePush.subscribeInBackground(channel);
        }
        world.setIsRegistered(!world.isRegistered());

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("parse_" + channel, world.isRegistered());
        editor.commit();

        // REMOVE THIS TO ENABLE PUSH NOTIFICATIONS
        //ParsePush.unsubscribeInBackground(channel);
        //world.setIsRegistered(false);
        makeText(context, R.string.toast_push_notifications_disabled, LENGTH_LONG).show();

        notifyDataSetInvalidated();
    }
    @Override
    public int getCount() {
        return this.serverList.size();
    }

    @Override
    public World getItem(int position) {
        return this.serverList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid
        // unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is
        // no need
        // to reinflate it. We only inflate a new View when the convertView
        // supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_server_item, parent, false);

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.serverStatus = (TextView) convertView.findViewById(R.id.textViewServerStatus);
            holder.serverName = (TextView) convertView.findViewById(R.id.textViewServerListName);
            holder.serverRegion = (TextView) convertView.findViewById(R.id.textViewServerListRegion);
            holder.serverPopulation = (TextView) convertView.findViewById(R.id.textViewServerPopulation);
            holder.serverAlert = (TextView) convertView.findViewById(R.id.textViewServerAlert);
            holder.serverAlertCheckBox = (CheckBox)convertView.findViewById(R.id.checkBoxServerAlert);
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        String serverPopulation = this.serverList.get(position).getPopulation();
        if (serverPopulation != null) {
            if (serverPopulation.equalsIgnoreCase("low")) {
                // Orange color
                holder.serverPopulation.setTextColor(Color.rgb(250, 140, 0));
                holder.serverPopulation.setText(context.getResources().getString(R.string.text_server_population) + " " + context.getResources().getString(R.string.text_low).toUpperCase());
            } else if (serverPopulation.equalsIgnoreCase("medium")) {
                holder.serverPopulation.setTextColor(Color.YELLOW);
                holder.serverPopulation.setText(context.getResources().getString(R.string.text_server_population) + " " + context.getResources().getString(R.string.text_medium).toUpperCase());
            } else if (serverPopulation.equalsIgnoreCase("high")) {
                holder.serverPopulation.setTextColor(Color.GREEN);
                holder.serverPopulation.setText(context.getResources().getString(R.string.text_server_population) + " " + context.getResources().getString(R.string.text_high).toUpperCase());
            } else {
                holder.serverPopulation.setTextColor(Color.RED);
                holder.serverPopulation.setText(context.getResources().getString(R.string.text_server_population) + " " + serverPopulation.toUpperCase());
            }
        } else {
            holder.serverPopulation.setText(context.getResources().getString(R.string.text_server_population) + " " + "Not Available");
            holder.serverPopulation.setTextColor(Color.WHITE);
        }

        String serverState = this.serverList.get(position).getState();
        if (serverState != null) {
            holder.serverStatus.setText(serverState.toUpperCase(Locale.getDefault()));
            if (serverState.equalsIgnoreCase("online")) {
                holder.serverStatus.setTextColor(Color.GREEN);
            } else {
                holder.serverStatus.setTextColor(Color.RED);
            }
        } else {
            holder.serverStatus.setText("UNKNOWN");
            holder.serverStatus.setTextColor(Color.RED);
        }

        holder.serverAlertCheckBox.setEnabled(notificationsEnabled);
        holder.serverAlertCheckBox.setChecked(this.serverList.get(position).isRegistered());

        String name = this.serverList.get(position).getName().getLocalizedName();

        if (name.equals("Briggs")) {
            holder.serverRegion.setText("(AU)");
        } else if (name.equals("Emerald") || name.equals("Smaragd") || name.equals("Esmeralda") || name.equals("Smeraldo")) {
            holder.serverRegion.setText("(US EAST)");
        } else if (name.equals("Connery")) {
            holder.serverRegion.setText("(US WEST)");
        } else if (name.equals("Miller") || name.equals("Cobalt")) {
            holder.serverRegion.setText("(EU)");
        } else if (name.equals("Jaeger")) {
            holder.serverRegion.setText("( - )");
        } else {
            holder.serverRegion.setText("");
        }

        holder.serverName.setText(name);

        WorldEvent lastAlert = getItem(position).getLastAlert();
        if(lastAlert != null){
            try {
                // THIS ARE GREEN
                //   metagame_event_state_id: "135",
                //            name: "started"
                //    metagame_event_state_id: "136",
                //            name: "restarted"
                //    metagame_event_state_id: "139",
                //            name: "xp bonus changed"
                // THIS ARE GRAY
                //    metagame_event_state_id: "137",
                //            name: "canceled"
                //    metagame_event_state_id: "138",
                //            name: "ended"

                if(lastAlert.getMetagame_event_state().equals("135") ||
                   lastAlert.getMetagame_event_state().equals("136") ||
                   lastAlert.getMetagame_event_state().equals("139") ) {
                    holder.serverAlert.setText(context.getResources().getString(R.string.text_server_alert_current)
                            + " " + lastAlert.getMetagame_event_id_join_metagame_event().getDescription().getLocalizedName());

                    holder.serverAlert.setTextColor(Color.argb(255, 120, 235, 235));
                    holder.serverAlert.setTypeface(null, Typeface.BOLD);
                } else {
                    holder.serverAlert.setText(context.getResources().getString(R.string.text_server_alert_recently)
                            + " " + lastAlert.getMetagame_event_id_join_metagame_event().getDescription().getLocalizedName());
                    holder.serverAlert.setTypeface(null, Typeface.NORMAL);
                    holder.serverAlert.setTextColor(Color.GRAY);
                }
            }catch (NullPointerException e){
                holder.serverAlert.setText(context.getResources().getString(R.string.text_server_alert_recently)
                        + " " + context.getResources().getString(R.string.text_unknown).toUpperCase());
                holder.serverAlert.setTypeface(null, Typeface.NORMAL);
            }
        }else{
            holder.serverAlert.setText(context.getResources().getString(R.string.text_server_alert_recently)
                    + " " + context.getResources().getString(R.string.text_none));
            holder.serverAlert.setTypeface(null, Typeface.NORMAL);
        }

        return convertView;
    }

    static class ViewHolder {

        TextView serverStatus;
        TextView serverName;
        TextView serverRegion;
        TextView serverPopulation;
        TextView serverAlert;
        CheckBox serverAlertCheckBox;
    }
}
