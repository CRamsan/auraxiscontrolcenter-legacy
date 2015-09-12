package com.cesarandres.ps2link.dbg.content.response;

import com.cesarandres.ps2link.dbg.content.World;

import java.util.ArrayList;

public class Server_response {
    private ArrayList<World> world_list;

    public ArrayList<World> getWorld_list() {
        return this.world_list;
    }

    public void setWorld_list(ArrayList<World> world_list) {
        this.world_list = world_list;
    }
}
