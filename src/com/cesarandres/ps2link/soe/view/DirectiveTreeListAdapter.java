package com.cesarandres.ps2link.soe.view;
 
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.DirectiveTree;
import com.cesarandres.ps2link.soe.content.response.Directive_tree_list;
import com.cesarandres.ps2link.soe.util.EmbeddableExpandableListView;
 
public class DirectiveTreeListAdapter extends BaseExpandableListAdapter implements OnGroupExpandListener{
	
    private BaseFragment fragment;
    private EmbeddableExpandableListView expandableList;
    private ArrayList<DirectiveTree> categories;
 
    public DirectiveTreeListAdapter(BaseFragment fragment) {
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
  
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.fragment.getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_directive_loading, null);
        }
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
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
        DirectiveTree headerTitle = (DirectiveTree) getGroup(groupPosition);
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
	
    /**
     * @param character_id
     *            Character id that will be used to request the list of directives
     */
    public void downloadDirectivesTreeList(final DirectiveTreeCategoryListAdapter parent, final View view, String categoryId) {
	this.fragment.setProgressButton(true);
	String url = 	"http://census.soe.com/get/ps2:v2/" +
			"directive_tree?c:limit=1000&" + 
			"c:lang=en&directive_tree_category_id=" + categoryId;

	Listener<Directive_tree_list> success = new Response.Listener<Directive_tree_list>() {
	    @Override
	    public void onResponse(Directive_tree_list response) {
		fragment.setProgressButton(false);
		try {
			EmbeddableExpandableListView newList = new EmbeddableExpandableListView(fragment.getActivity());
		    expandableList = newList;
		    categories = response.getDirective_tree_list();
		    expandableList.setRows(categories.size());
		    expandableList.setRow_height(view.getHeight());
		    expandableList.setAdapter(DirectiveTreeListAdapter.this);
		    expandableList.setOnGroupExpandListener(DirectiveTreeListAdapter.this);
			parent.notifyDataSetChanged();
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

	SOECensus.sendGsonRequest(url, Directive_tree_list.class, success, error, this);
    }
	
	public View getExpandableView(){
		return this.expandableList;
	}
}