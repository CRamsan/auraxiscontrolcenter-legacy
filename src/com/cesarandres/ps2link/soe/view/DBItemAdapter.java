package com.cesarandres.ps2link.soe.view;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class DBItemAdapter extends BaseAdapter {
	protected LayoutInflater mInflater;
	protected Cursor cursor;
	protected int size;
}
