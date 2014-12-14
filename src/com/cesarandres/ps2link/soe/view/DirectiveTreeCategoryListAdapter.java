package com.cesarandres.ps2link.soe.view;
 
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.content.DirectiveTreeCategory;
import com.cesarandres.ps2link.soe.util.EmbeddableExpandableListView;
 
public class DirectiveTreeCategoryListAdapter extends BaseExpandableListAdapter implements OnGroupExpandListener{

    private ExpandableListView expandableList;
    private BaseFragment fragment;
    private ArrayList<DirectiveTreeCategory> categories;

    private DirectiveTreeListAdapter nextAdapter;
    private EmbeddableExpandableListView  nextList;
    
    public DirectiveTreeCategoryListAdapter(BaseFragment fragment, ExpandableListView expandableList) {
    	this.fragment = fragment;
        this.expandableList = expandableList;
        this.nextAdapter = new DirectiveTreeListAdapter(fragment);
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return null;
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

    	if(this.nextList == null){
    		this.nextList = new EmbeddableExpandableListView(this.fragment.getActivity());
    		this.nextList.setRows(this.categories.size());
    		this.nextList.setRow_height(50);			
    	}
    	return this.nextList;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.categories.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.categories.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        DirectiveTreeCategory headerTitle = (DirectiveTreeCategory) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.fragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_directive_category_item, parent, false);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textViewDirectiveCategoryName);
        lblListHeader.setText(headerTitle.getName().getEn());
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

	@Override
	public void onGroupExpand(int groupPosition) {
	    int len = getGroupCount();

	    for (int i = 0; i < len; i++) {
	        if (i != groupPosition) {
	        	this.expandableList.collapseGroup(i);
	        }
	    }
	}
	
	public void setCategories(ArrayList<DirectiveTreeCategory> categories) {
		this.categories = categories;
	}
}