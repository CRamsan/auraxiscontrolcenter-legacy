package com.cesarandres.ps2link.dbg.content.response;

import com.cesarandres.ps2link.dbg.content.CharacterEvent;
import com.cesarandres.ps2link.dbg.content.WorldEvent;

import java.util.ArrayList;

public class World_event_list_response {

    private ArrayList<WorldEvent> world_event_list;

    public ArrayList<WorldEvent> getWorld_event_list() {
        return world_event_list;
    }

    public void setWorld_event_list(ArrayList<WorldEvent> world_event_list) {
        this.world_event_list = world_event_list;
    }
}
