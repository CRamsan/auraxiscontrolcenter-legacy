package com.cesarandres.ps2link.soe.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cesarandres.ps2link.R;

public class LoadingItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public LoadingItemAdapter(Context context) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	return 1;
    }

    @Override
    public Object getItem(int position) {
	return null;
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.layout_loading_item, parent, false);
	}

	return convertView;
    }
}
