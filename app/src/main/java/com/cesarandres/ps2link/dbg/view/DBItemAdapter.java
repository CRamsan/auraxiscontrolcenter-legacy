package com.cesarandres.ps2link.dbg.view;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * This abstract class provides some extra functionality for adapters that need
 * to read from the database
 */
public abstract class DBItemAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Cursor cursor;
    protected int size;
}
