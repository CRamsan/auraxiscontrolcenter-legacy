package com.cesarandres.ps2link.soe.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.soe.content.WDS_Stat;

public class World_Stat_History_Server_response {
    private ArrayList<WDS_Stat> world_stat_history_list;

    public ArrayList<WDS_Stat> getWorld_stat_history_list() {
	return world_stat_history_list;
    }

    public void setWorld_stat_history_list(ArrayList<WDS_Stat> world_stat_history_list) {
	this.world_stat_history_list = world_stat_history_list;
    }

}
