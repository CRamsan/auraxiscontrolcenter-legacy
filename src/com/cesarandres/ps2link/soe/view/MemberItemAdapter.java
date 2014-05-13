package com.cesarandres.ps2link.soe.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.module.ObjectDataSource;
import com.cesarandres.ps2link.soe.content.Member;
import com.cesarandres.ps2link.soe.content.character.Name;

public class MemberItemAdapter extends DBItemAdapter {

    public MemberItemAdapter(Context context, String outfitId, ObjectDataSource data, boolean showOffline) {
	// Cache the LayoutInflate to avoid asking for a new one each time.
	this.mInflater = LayoutInflater.from(context);
	this.size = data.countAllMembers(outfitId, showOffline);
	this.cursor = data.getMembersCursor(outfitId, showOffline);
    }

    @Override
    public int getCount() {
	return this.size;
    }

    @Override
    public Member getItem(int position) {
	Member member;
	try {
	    member = ObjectDataSource.cursorToMember(ObjectDataSource.cursorToPosition(cursor, position));
	} catch (IllegalStateException e) {
	    member = new Member();
	    member.setCharacter_id("");
	    member.setOnline_status("");
	    member.setOutfit_id("");
	    member.setRank("");
	    member.setName(new Name() {
		{
		    this.setFirst("");
		}
	    });
	}
	return member;
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
	    holder.memberStatus = (TextView) convertView.findViewById(R.id.textViewMemberListStatus);
	    holder.memberRank = (TextView) convertView.findViewById(R.id.textViewMemberListRank);
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
