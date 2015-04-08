package com.cesarandres.ps2link.dbg.util;

import android.content.Context;
import android.widget.ExpandableListView;

public class EmbeddableExpandableListView extends ExpandableListView {

	private int row_height = 20;
	private int rows;
	
	public EmbeddableExpandableListView( Context context ) {
        super( context );
    }

    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
    	setMeasuredDimension( getMeasuredWidth(), rows*row_height );

    }

	public int getRow_height() {
		return row_height;
	}

	public void setRow_height(int row_height) {
		this.row_height = row_height;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
}
