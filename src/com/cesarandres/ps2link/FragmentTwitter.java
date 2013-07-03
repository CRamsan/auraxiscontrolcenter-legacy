package com.cesarandres.ps2link;

import java.util.ArrayList;

import twitter4j.Status;
import twitter4j.User;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cesarandres.ps2link.base.BaseFragment;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.module.twitter.PS2Tweet;
import com.cesarandres.ps2link.soe.content.Member;

/**
 * Created by cesar on 6/16/13.
 */
public class FragmentTwitter extends BaseFragment {


	public static Bitmap ps2deals;
	public static Bitmap plantside2;
	public static Bitmap purrfectstorm;
	public static Bitmap mhigby;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View root = inflater.inflate(R.layout.fragment_twitter, container,
				false);

		((Button) root.findViewById(R.id.buttonFragmentTitle))
				.setText(getString(R.string.text_menu_reddit));

		ImageButton updateButton = (ImageButton) root
				.findViewById(R.id.buttonFragmentUpdate);
		updateButton.setVisibility(View.VISIBLE);

		updateButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Reload tweets
			}
		});

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private static class TwitterItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private int size;
		private ArrayList<PS2Tweet> tweets;
		
		public TwitterItemAdapter(Context context, ArrayList<PS2Tweet> tweets) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			this.mInflater = LayoutInflater.from(context);
			this.tweets = tweets;
		}

		@Override
		public int getCount() {
			return this.tweets.size();
		}

		@Override
		public PS2Tweet getItem(int position) {
			return this.tweets.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.member_item_list, null);

				holder = new ViewHolder();
				holder.memberName = (TextView) convertView
						.findViewById(R.id.textViewMemberListName);
				holder.memberStatus = (TextView) convertView
						.findViewById(R.id.textViewMemberListStatus);
				holder.memberRank = (TextView) convertView
						.findViewById(R.id.textViewMemberListRank);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.memberName.setText(getItem(position).getName().getFirst());
			holder.memberRank.setText(getItem(position).getRank());

			if (getItem(position).getOnline_status().equals("0")) {
				holder.memberStatus.setText("Offline");
				holder.memberStatus.setTextColor(Color.RED);
			} else {
				holder.memberStatus.setText("Online");
				holder.memberStatus.setTextColor(Color.GREEN);
			}

			return convertView;
		}

		static class ViewHolder {
			TextView memberName;
			TextView memberStatus;
			TextView memberRank;
		}
	}

	private class UpdateTweets extends AsyncTask<String, Integer, Integer> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Integer doInBackground(String... users) {
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
		}
	}
}
