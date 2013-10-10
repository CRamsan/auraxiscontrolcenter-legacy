package com.cesarandres.ps2link.soe.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.Member;

public class OnlineMemberItemAdapter extends DBItemAdapter {

	private ArrayList<Member> members;

	public OnlineMemberItemAdapter(ArrayList<Member> members) {
		this.members = members;
	}

	@Override
	public int getCount() {
		return this.size;
	}

	@Override
	public Member getItem(int position) {
		return this.members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_member_item, null);

			holder = new ViewHolder();
			holder.memberName = (TextView) convertView.findViewById(R.id.textViewMemberListName);
			holder.memberRank = (TextView) convertView.findViewById(R.id.textViewMemberListRank);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.memberName.setText(getItem(position).getName().getFirst());
		holder.memberRank.setText(getItem(position).getCharacter().getProfile().getName().getEn());

		return convertView;
	}

	static class ViewHolder {
		TextView memberName;
		TextView memberRank;
	}
}
