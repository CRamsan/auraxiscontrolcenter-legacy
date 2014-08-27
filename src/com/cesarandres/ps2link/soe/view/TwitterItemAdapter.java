package com.cesarandres.ps2link.soe.view;

import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cesarandres.ps2link.ApplicationPS2Link;
import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.module.twitter.PS2Tweet;

public class TwitterItemAdapter extends DBItemAdapter {

    private PrettyTime p = new PrettyTime();

    public TwitterItemAdapter(Context context, String[] users, ObjectDataSource data) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
	this.size = data.countAllTweets(users);
	this.cursor = data.getTweetCursor(users);
    }

    @Override
    public int getCount() {
	return this.size;
    }

    @Override
    public PS2Tweet getItem(int position) {
	PS2Tweet tweet = null;
	try {
	    tweet = ObjectDataSource.cursorToTweet(ObjectDataSource.cursorToPosition(cursor, position));
	} catch (IllegalStateException e) {
	    tweet = new PS2Tweet();
	    tweet.setId("");
	    tweet.setDate(0);
	    tweet.setContent("");
	    tweet.setUser("");
	    tweet.setTag("");
	    tweet.setImgUrl("");
	}
	return tweet;
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.layout_tweet_item, parent);

	    holder = new ViewHolder();
	    holder.tweetName = (TextView) convertView.findViewById(R.id.textViewTwitterName);
	    holder.tweetTag = (TextView) convertView.findViewById(R.id.textViewTwitterTag);
	    holder.tweetText = (TextView) convertView.findViewById(R.id.textViewTwitterText);
	    holder.tweetDate = (TextView) convertView.findViewById(R.id.textViewTwitterDate);
	    holder.userImage = (NetworkImageView) convertView.findViewById(R.id.networkImageViewTweet);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	PS2Tweet tweet = getItem(position);
	holder.tweetName.setText(tweet.getUser());
	holder.tweetText.setText(tweet.getContent());
	Linkify.addLinks(holder.tweetText, Linkify.WEB_URLS);
	holder.tweetText.setFocusable(false);
	holder.tweetTag.setText("@" + tweet.getTag());
	String updateTime = p.format(new Date(tweet.getDate() * 1000l));

	holder.userImage.setImageUrl(tweet.getImgUrl(), ApplicationPS2Link.mImageLoader);
	holder.tweetDate.setText(updateTime);

	return convertView;
    }

    static class ViewHolder {
	TextView tweetName;
	TextView tweetTag;
	TextView tweetText;
	TextView tweetDate;
	NetworkImageView userImage;
    }
}