package com.cesarandres.ps2link.lib.soe.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.lib.soe.content.World;

public class Server_response {
	private ArrayList<World> world_list;

	public ArrayList<World> getWorld_list() {
		return this.world_list;
	}

	public void setWorld_list(ArrayList<World> world_list) {
		this.world_list = world_list;
	}
}