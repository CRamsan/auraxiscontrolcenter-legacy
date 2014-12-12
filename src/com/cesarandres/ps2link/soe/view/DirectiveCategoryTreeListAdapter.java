package com.cesarandres.ps2link.soe.view;
 
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.DirectiveTreeCategory;
import com.cesarandres.ps2link.soe.content.response.Directive_tree_category_list;
 
public class DirectiveCategoryTreeListAdapter extends BaseExpandableListAdapter implements OnGroupExpandListener, OnGroupClickListener{

    private ExpandableListView expandableList;
    private BaseFragment fragment;
    private ArrayList<DirectiveTreeCategory> categories;
    private DirectiveTreeListAdapter nextAdapter;
    
    public DirectiveCategoryTreeListAdapter(BaseFragment fragment, ExpandableListView expandableList, String profileId) {
    	this.fragment = fragment;
        this.expandableList = expandableList;
        this.nextAdapter = new DirectiveTreeListAdapter(fragment, profileId);
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
  
    	if(!this.nextAdapter.isReady()){
    		LayoutInflater infalInflater = (LayoutInflater) this.fragment.getActivity()
    				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		convertView = infalInflater.inflate(R.layout.layout_directive_loading, parent, false);
            
            this.nextAdapter.downloadDirectivesTreeList(this, convertView, ((DirectiveTreeCategory)this.getGroup(groupPosition)).getDirectiveTreeCategoryId());
            
            return convertView;
    	}else{
    		return this.nextAdapter.getExpandableView();
    	}
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
	
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		this.nextAdapter.setReady(false);
		return false;
	}
	
    /**
     * @param character_id
     *            Character id that will be used to request the list of directives
     */
    public void downloadDirectivesList() {
	this.fragment.setProgressButton(true);
	String url = 	"http://census.soe.com/get/ps2:v2/" +
					"directive_tree_category?c:limit=1000&c:lang=en";

	Listener<Directive_tree_category_list> success = new Response.Listener<Directive_tree_category_list>() {
	    @Override
	    public void onResponse(Directive_tree_category_list response) {
		fragment.setProgressButton(false);
		try {
			categories = response.getDirective_tree_category_list();
		    expandableList.setOnGroupExpandListener(DirectiveCategoryTreeListAdapter.this);
		    expandableList.setOnGroupClickListener(DirectiveCategoryTreeListAdapter.this);			
		    expandableList.setAdapter(DirectiveCategoryTreeListAdapter.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    }
	};

	ErrorListener error = new Response.ErrorListener() {
	    @Override
	    public void onErrorResponse(VolleyError error) {
		fragment.setProgressButton(false);
	    }
	};

	SOECensus.sendGsonRequest(url, Directive_tree_category_list.class, success, error, this);
    }
}