package com.cesarandres.ps2link.dbg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.dbg.content.CharacterProfile;
import com.cesarandres.ps2link.dbg.content.Faction;

import java.util.ArrayList;
import java.util.List;

public class ProfileItemAdapter extends BaseAdapter {
    private static Bitmap vs_icon;
    private static Bitmap nc_icon;
    private static Bitmap tr_icon;
    private LayoutInflater mInflater;
    private ArrayList<CharacterProfile> profileList;
    private boolean full;

    public ProfileItemAdapter(Context context, List<CharacterProfile> profileList, boolean full) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.mInflater = LayoutInflater.from(context);
        this.profileList = new ArrayList<CharacterProfile>(profileList);
        this.full = full;
        vs_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_faction_vs);
        tr_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_faction_tr);
        nc_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_faction_nc);
    }

    @Override
    public int getCount() {
        return this.profileList.size();
    }

    @Override
    public CharacterProfile getItem(int position) {
        return this.profileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid
        // unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is
        // no need
        // to reinflate it. We only inflate a new View when the convertView
        // supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_profile_item, parent, false);

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.faction = (ImageView) convertView.findViewById(R.id.imageViewFaction);
            holder.battleRank = (TextView) convertView.findViewById(R.id.textViewBattleRank);
            holder.battleRank.setVisibility(View.VISIBLE);
            holder.name = (TextView) convertView.findViewById(R.id.textViewProfileCharacterName);
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        CharacterProfile profile = this.profileList.get(position);
        if (this.full) {
            if (Faction.VS.equals(profile.getFaction_id())) {
                holder.faction.setImageBitmap(vs_icon);
            } else if (Faction.NC.equals(profile.getFaction_id())) {
                holder.faction.setImageBitmap(nc_icon);
            } else if (Faction.TR.equals(profile.getFaction_id())) {
                holder.faction.setImageBitmap(tr_icon);
            }
            holder.battleRank.setText(Integer.toString(profile.getBattle_rank().getValue()));
        } else {
            if (profile.getCharacter_id_join_character() == null) {
                holder.faction.setImageDrawable(null);
            } else {
                if (Faction.VS.equals(profile.getCharacter_id_join_character().getFaction_id())) {
                    holder.faction.setImageBitmap(vs_icon);
                } else if (Faction.NC.equals(profile.getCharacter_id_join_character().getFaction_id())) {
                    holder.faction.setImageBitmap(nc_icon);
                } else if (Faction.TR.equals(profile.getCharacter_id_join_character().getFaction_id())) {
                    holder.faction.setImageBitmap(tr_icon);
                }
            }
            if (profile.getCharacter_id_join_character() == null) {
                holder.battleRank.setText("-");
            } else {
                holder.battleRank.setText(Integer.toString(profile.getCharacter_id_join_character().getBattle_rank().getValue()));
            }
        }
        holder.name.setText(this.profileList.get(position).getName().getFirst());

        return convertView;
    }

    static class ViewHolder {
        ImageView faction;
        TextView battleRank;
        TextView name;
    }
}