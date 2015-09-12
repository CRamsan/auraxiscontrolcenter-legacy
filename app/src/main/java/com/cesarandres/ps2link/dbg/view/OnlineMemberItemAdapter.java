package com.cesarandres.ps2link.dbg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.dbg.content.Member;

import java.util.ArrayList;

public class OnlineMemberItemAdapter extends BaseAdapter {

    private static Bitmap inf_icon;
    private static Bitmap lia_icon;
    private static Bitmap med_icon;
    private static Bitmap eng_icon;
    private static Bitmap hea_icon;
    private static Bitmap max_icon;
    protected LayoutInflater mInflater;
    private ArrayList<Member> membersOnline;

    public OnlineMemberItemAdapter(ArrayList<Member> members, Context context) {
        this.mInflater = LayoutInflater.from(context);

        this.membersOnline = new ArrayList<Member>();
        inf_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_inf);
        lia_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_lia);
        med_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_med);
        eng_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_eng);
        hea_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_hea);
        max_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_max);

        for (Member member : members) {
            if (!member.getOnline_status().equals("0")) {
                this.membersOnline.add(member);
            }
        }
    }

    @Override
    public int getCount() {
        return this.membersOnline.size();
    }

    @Override
    public Member getItem(int position) {
        return this.membersOnline.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_member_item, parent, false);

            holder = new ViewHolder();
            holder.memberName = (TextView) convertView.findViewById(R.id.textViewMemberListName);
            holder.memberRank = (TextView) convertView.findViewById(R.id.textViewMemberListRank);
            holder.classIcon = (ImageView) convertView.findViewById(R.id.imageViewMemberListClass);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            if (getItem(position).getOnline_status().equals("0")) {
                holder.memberName.setTextColor(Color.WHITE);
                holder.memberRank.setTextColor(Color.RED);
            } else {
                holder.memberName.setTextColor(Color.WHITE);
                holder.memberRank.setTextColor(Color.GREEN);
            }
            holder.memberName.setText(getItem(position).getCharacter().getName().getFirst());
            String currentClass = getItem(position).getCharacter().getProfile().getName().getLocalizedName();
            String currentClassId = getItem(position).getCharacter().getProfile_id();
            holder.memberRank.setText(currentClass);

            if (currentClassId.equals("4") || currentClassId.equals("19") || currentClassId.equals("12")) {
                holder.classIcon.setImageBitmap(lia_icon);
            } else if (currentClassId.equals("7") || currentClassId.equals("22") || currentClassId.equals("15")) {
                holder.classIcon.setImageBitmap(hea_icon);
            } else if (currentClassId.equals("5") || currentClassId.equals("20") || currentClassId.equals("13")) {
                holder.classIcon.setImageBitmap(med_icon);
            } else if (currentClassId.equals("6") || currentClassId.equals("21") || currentClassId.equals("14")) {
                holder.classIcon.setImageBitmap(eng_icon);
            } else if (currentClassId.equals("8") || currentClassId.equals("23") || currentClassId.equals("16")) {
                holder.classIcon.setImageBitmap(max_icon);
            } else if (currentClassId.equals("2") || currentClassId.equals("17") || currentClassId.equals("10")) {
                holder.classIcon.setImageBitmap(inf_icon);
            } else {
                holder.classIcon.setImageBitmap(null);
            }

        } catch (NullPointerException e) {
            holder.memberName.setText("UNKOWN");
            holder.memberRank.setText("UNKOWN");
            holder.memberName.setTextColor(Color.GRAY);
            holder.memberRank.setTextColor(Color.GRAY);
            holder.classIcon.setImageBitmap(null);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView memberName;
        TextView memberRank;
        ImageView classIcon;
    }
}
