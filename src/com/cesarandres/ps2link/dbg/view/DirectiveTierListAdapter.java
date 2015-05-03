package com.cesarandres.ps2link.dbg.view;
 
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.dbg.content.Directive;
import com.cesarandres.ps2link.dbg.util.EmbeddableExpandableListView;
 
public class DirectiveTierListAdapter extends BaseExpandableListAdapter implements OnGroupExpandListener{
	
    private BaseFragment fragment;
    private EmbeddableExpandableListView expandableList;
    private ArrayList<Directive> directives;
 
    public DirectiveTierListAdapter(BaseFragment fragment) {
    	this.fragment = fragment;
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
  
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.directives.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
    	try{
    	return this.directives.size();
    	}catch(Exception e){
    		this.directives = new ArrayList<Directive>();
    		return this.directives.size();
    	}
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        Directive headerTitle = (Directive) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.fragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_directive_item, parent, false);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textViewDirectiveCategoryName);
        ProgressBar pbar = (ProgressBar) convertView.findViewById(R.id.progressBarDirectiveProgress);
        String name = "None";
        try{
        	name = headerTitle.getName().getLocalizedName();        	
        }catch(Exception e){

        }
        lblListHeader.setText(name);
        int value = 0;
        int max = 0;
        try{
        value = Integer.parseInt(headerTitle.getDirective().getDirectiveObjective().getState_data());
        //max = Integer.parseInt(headerTitle.getDirective().getDirectiveObjective().getObjective_id_join_objective().getParam1());
        }catch(Exception e){
        	
        }
        pbar.setProgress(value);
        pbar.setMax(max);
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

	public void setDirectives(ArrayList<Directive> directives) {
		this.directives = directives;
	}
}