package com.cesarandres.ps2link.dbg.content.character;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;

public class Server {
    private Name_Multi name;
    private String state;
    private int world_id;

    public Name_Multi getName() {
	return name;
    }

    public void setName(Name_Multi name) {
	this.name = name;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public int getWorld_id() {
	return world_id;
    }

    public void setWorld_id(int world_id) {
	this.world_id = world_id;
    }
}
