package com.cesarandres.ps2link.dbg.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.dbg.content.Outfit;

public class OutfitItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Outfit> outfitList;
    private Context context;

    public OutfitItemAdapter(Context context, List<Outfit> outfitList) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
	this.outfitList = new ArrayList<Outfit>(outfitList);
	this.context = context;
    }

    @Override
    public int getCount() {
	return this.outfitList.size();
    }

    @Override
    public Outfit getItem(int position) {
	return this.outfitList.get(position);
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
	    convertView = mInflater.inflate(R.layout.layout_outfit_item, parent, false);

	    // Creates a ViewHolder and store references to the two children
	    // views
	    // we want to bind data to.
	    holder = new ViewHolder();
	    holder.outfitName = (TextView) convertView.findViewById(R.id.textViewOutfitName);
	    holder.outfitAlias = (TextView) convertView.findViewById(R.id.textViewOutfitAlias);
	    holder.memberCount = (TextView) convertView.findViewById(R.id.textViewOutfitCount);
	    convertView.setTag(holder);
	} else {
	    // Get the ViewHolder back to get fast access to the TextView
	    // and the ImageView.
	    holder = (ViewHolder) convertView.getTag();
	}

	holder.outfitName.setText(this.outfitList.get(position).getName());
	holder.memberCount.setText(context.getResources().getString(R.string.text_outfit_members) + " " + this.outfitList.get(position).getMember_count());
	String tag = this.outfitList.get(position).getAlias();
	if (tag.length() > 0) {
	    holder.outfitAlias.setText("(" + tag + ")");
	}

	return convertView;
    }

    static class ViewHolder {
	TextView outfitName;
	TextView outfitAlias;
	TextView memberCount;
    }
}
