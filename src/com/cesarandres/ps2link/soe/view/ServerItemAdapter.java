package com.cesarandres.ps2link.soe.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.soe.content.World;

public class ServerItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<World> serverList;

    public ServerItemAdapter(Context context, List<World> serverList) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
	this.serverList = new ArrayList<World>(serverList);
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
	    convertView = mInflater.inflate(R.layout.layout_server_item, null);

	    // Creates a ViewHolder and store references to the two children
	    // views
	    // we want to bind data to.
	    holder = new ViewHolder();
	    holder.serverstatus = (TextView) convertView.findViewById(R.id.textViewServerStatus);
	    holder.serverName = (TextView) convertView.findViewById(R.id.textViewServerListName);
	    holder.serverRegion = (TextView) convertView.findViewById(R.id.textViewServerListRegion);
	    convertView.setTag(holder);
	} else {
	    // Get the ViewHolder back to get fast access to the TextView
	    // and the ImageView.
	    holder = (ViewHolder) convertView.getTag();
	}

	// Bind the data efficiently with the holder.
	String serverState = this.serverList.get(position).getState();
	if (serverState.equals("online")) {
	    holder.serverstatus.setText(serverState.toUpperCase());
	    holder.serverstatus.setTextColor(Color.GREEN);
	} else {
	    holder.serverstatus.setText(serverState.toUpperCase());
	    holder.serverstatus.setTextColor(Color.RED);
	}

	String name = this.serverList.get(position).getName().getEn();

	if (name.equals("Briggs")) {
	    holder.serverRegion.setText("(AU)");
	} else if (name.equals("Waterson") || name.equals("Mattherson")) {
	    holder.serverRegion.setText("(US EAST)");
	} else if (name.equals("Connery")) {
	    holder.serverRegion.setText("(US WEST)");
	} else if (name.equals("Mallory") || name.equals("Ceres") || name.equals("Miller") || name.equals("Cobalt") || name.equals("Woodman")) {
	    holder.serverRegion.setText("(EU)");
	} else {
	    holder.serverRegion.setText("");
	}

	holder.serverName.setText(name);

	return convertView;
    }

    static class ViewHolder {

	TextView serverstatus;
	TextView serverName;
	TextView serverRegion;
    }
}
