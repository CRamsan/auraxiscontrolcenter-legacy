package com.cesarandres.ps2link.module.reddit;

import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;

public class RedditItemAdapter extends BaseAdapter{

    private static PrettyTime prettyTime = new PrettyTime();
    private List<Child> children;
    protected LayoutInflater mInflater;
    private Context context;
    
    public RedditItemAdapter(Context context, List<Child> children) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
	this.children = children;
	this.context = context;
    }

    @Override
    public int getCount() {
	return this.children.size();
    }

    @Override
    public Child getItem(int position) {
	return this.children.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.layout_reddit_item, parent, false);

	    holder = new ViewHolder();
	    holder.author = (TextView) convertView.findViewById(R.id.textViewRedditAuthor);
	    holder.comments = (TextView) convertView.findViewById(R.id.textViewRedditComments);
	    holder.date = (TextView) convertView.findViewById(R.id.textViewRedditCreated);
	    holder.flare = (TextView) convertView.findViewById(R.id.textViewRedditFlare);
	    holder.domain = (TextView) convertView.findViewById(R.id.textViewRedditDomain);
	    holder.score = (TextView) convertView.findViewById(R.id.textViewRedditScore);
	    holder.title = (TextView) convertView.findViewById(R.id.textViewRedditTitle);
	    holder.thumbnail = (NetworkImageView) convertView.findViewById(R.id.networkImageViewRedditThumbnail);
	    holder.thumbnail.setErrorImageResId(R.drawable.image_not_found);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	
	Child child = getItem(position);

	holder.author.setText(child.getData().getAuthor());
	holder.comments.setText(Integer.toString(child.getData().getNumComments()) + " " + context.getResources().getString(R.string.text_comments));	
	String updateTime = prettyTime.format(new Date(child.getData().getCreatedUtc() * 1000l));
	holder.date.setText(updateTime);
	holder.domain.setText(child.getData().getDomain());
	if(child.getData().getLinkFlairText() != null){
		holder.flare.setVisibility(View.VISIBLE);
		holder.flare.setText(child.getData().getLinkFlairText());
	}else{
		holder.flare.setVisibility(View.GONE);
		holder.flare.setText(child.getData().getLinkFlairText());
	}
	holder.score.setText(Integer.toString(child.getData().getScore()));
	holder.title.setText(child.getData().getTitle());

    holder.thumbnail.setDefaultImageResId(0);
    holder.thumbnail.setImageUrl("",null);
	if(!child.getData().getThumbnail().equals("self")){
		holder.thumbnail.setImageUrl(child.getData().getThumbnail(), ApplicationPS2Link.mImageLoader);
	}else{
	    holder.thumbnail.setDefaultImageResId(R.drawable.image_not_found);
	    holder.thumbnail.setImageUrl("",null);
	}
	
	return convertView;
    }

    static class ViewHolder {
    	NetworkImageView thumbnail;
    	TextView score;
    	TextView flare;
    	TextView title;
    	TextView author;
    	TextView domain;
    	TextView date;
    	TextView comments;
    }
}