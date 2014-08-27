package com.cesarandres.ps2link.soe.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.content.World;
import com.cesarandres.ps2link.soe.content.response.server.LiveServers;
import com.cesarandres.ps2link.soe.util.Logger;

public class ServerItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<World> serverList;

    public ServerItemAdapter(Context context, List<World> serverList) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
	this.serverList = new ArrayList<World>(serverList);
    }

    /**
     * This method will use the LiveServer object to pass population information
     * to the list of servers found in the API
     * 
     * @param serverList
     *            List of servers with population information
     */
    public void setServerPopulation(LiveServers serverList) {
	String name, population = "";
	for (World world : this.serverList) {
	    name = world.getName().getEn();
	    try {
		if (name.equals("Briggs")) {
		    population = serverList.getBriggs().getStatus();
		} else if (name.equals("Emerald")) {
		    population = serverList.getEmerald().getStatus();
		} else if (name.equals("Connery")) {
		    population = serverList.getConnery().getStatus();
		} else if (name.equals("Miller")) {
		    population = serverList.getMiller().getStatus();
		} else if (name.equals("Cobalt")) {
		    population = serverList.getCobalt().getStatus();
		} else{
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
	    convertView = mInflater.inflate(R.layout.layout_server_item, parent);

	    // Creates a ViewHolder and store references to the two children
	    // views
	    // we want to bind data to.
	    holder = new ViewHolder();
	    holder.serverStatus = (TextView) convertView.findViewById(R.id.textViewServerStatus);
	    holder.serverName = (TextView) convertView.findViewById(R.id.textViewServerListName);
	    holder.serverRegion = (TextView) convertView.findViewById(R.id.textViewServerListRegion);
	    holder.serverPopulation = (TextView) convertView.findViewById(R.id.textViewServerPopulation);
	    convertView.setTag(holder);
	} else {
	    // Get the ViewHolder back to get fast access to the TextView
	    // and the ImageView.
	    holder = (ViewHolder) convertView.getTag();
	}

	// Bind the data efficiently with the holder.
	// TODO Check for a replacement for this functions
	String serverPopulation = this.serverList.get(position).getPopulation();
	if (serverPopulation != null) {
	    holder.serverPopulation.setText("Population: " + serverPopulation.toUpperCase(Locale.getDefault()));
	    if (serverPopulation.equalsIgnoreCase("low")) {
		// Orange color
		holder.serverPopulation.setTextColor(Color.rgb(250, 140, 0));
	    } else if (serverPopulation.equalsIgnoreCase("medium")) {
		holder.serverPopulation.setTextColor(Color.YELLOW);
	    } else if (serverPopulation.equalsIgnoreCase("high")) {
		holder.serverPopulation.setTextColor(Color.GREEN);
	    } else {
		holder.serverPopulation.setTextColor(Color.RED);
	    }
	}else{
	    holder.serverPopulation.setText("Population: Not Available");
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
	}else{
	    holder.serverStatus.setText("UNKNOWN");
	    holder.serverStatus.setTextColor(Color.RED);	    
	}

	String name = this.serverList.get(position).getName().getEn();

	if (name.equals("Briggs")) {
	    holder.serverRegion.setText("(AU)");
	} else if (name.equals("Emerald")) {
	    holder.serverRegion.setText("(US EAST)");
	} else if (name.equals("Connery")) {
	    holder.serverRegion.setText("(US WEST)");
	} else if (name.equals("Ceres") || name.equals("Cobalt") ) {
	    holder.serverRegion.setText("(EU)");
	} else {
	    holder.serverRegion.setText("");
	}

	holder.serverName.setText(name);

	return convertView;
    }

    static class ViewHolder {

	TextView serverStatus;
	TextView serverName;
	TextView serverRegion;
	TextView serverPopulation;
    }
}
