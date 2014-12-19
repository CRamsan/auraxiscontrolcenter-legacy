package com.cesarandres.ps2link.soe.view;
 
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.soe.SOECensus;
import com.cesarandres.ps2link.soe.content.CharacterDirectiveTree;
import com.cesarandres.ps2link.soe.util.EmbeddableExpandableListView;
 
public class DirectiveTreeListAdapter extends BaseExpandableListAdapter implements OnGroupExpandListener{
	
    private BaseFragment fragment;
    private EmbeddableExpandableListView expandableList;
    private ArrayList<CharacterDirectiveTree> directiveTrees;
    private DirectiveTierListAdapter nextAdapter;
    private EmbeddableExpandableListView nextList;
    protected LayoutInflater mInflater;
 
    public DirectiveTreeListAdapter(BaseFragment fragment) {
    	this.fragment = fragment;
    	this.nextAdapter = new DirectiveTierListAdapter(fragment);
    	this.mInflater = LayoutInflater.from(fragment.getActivity());
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
  
    	this.nextAdapter.setDirectives(this.directiveTrees.get(groupPosition).getDirective_tier().getDirectives());
    	
    	if(this.nextList == null){
    		this.nextList = new EmbeddableExpandableListView(this.fragment.getActivity());
    		this.nextList.setRows(this.directiveTrees.get(groupPosition).getDirective_tier().getDirectives().size());
    		this.nextList.setRow_height(75);
    		this.nextList.setAdapter(nextAdapter);
    	}
    	this.nextAdapter.notifyDataSetInvalidated();
    	return this.nextList;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.directiveTrees.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
    	return this.directiveTrees.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	
    	if (convertView == null) {
    	    convertView = mInflater.inflate(R.layout.layout_directive_tree_item, parent, false);

    	    holder = new ViewHolder();
    	    holder.treeIcon = (NetworkImageView) convertView.findViewById(R.id.networkImageViewDirectoryTreeTier);
    	    holder.treeName = (TextView) convertView.findViewById(R.id.textViewDirectiveTreeName);
    	    holder.treeValue = (TextView) convertView.findViewById(R.id.textViewDirectiveTreeValueq);
    	    holder.treeLevel = (ImageView) convertView.findViewById(R.id.imageViewDirectiveLevel);
    	    convertView.setTag(holder);
    	} else {
    	    holder = (ViewHolder) convertView.getTag();
    	}
    	
    	CharacterDirectiveTree tree = this.directiveTrees.get(groupPosition);
    	holder.treeIcon.setImageUrl(SOECensus.ENDPOINT_URL + "/" + tree.getDirective_tier().getImagePath(), ApplicationPS2Link.mImageLoader);
    	holder.treeName.setText(tree.getDirective_tree_id_join_directive_tree().getName().getEn());
    	holder.treeValue.setText(Integer.toString(tree.getCurrent_level_value()));
    	int resID = this.fragment.getActivity().getResources().getIdentifier("objective_progress_" + tree.getCurrent_directive_tier_id()
    	+ "_0", "drawable","com.cesarandres.ps2link");
    	holder.treeLevel.setImageResource(resID);
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

	public void setDirectiveTrees(ArrayList<CharacterDirectiveTree> directiveTrees) {
		this.directiveTrees = directiveTrees;
	}
	
	static class ViewHolder{
		NetworkImageView treeIcon;
		TextView treeName;
		TextView treeValue;
		ImageView treeLevel;
	}
}