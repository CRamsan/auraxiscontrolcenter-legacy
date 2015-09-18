package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;

public class World {
    private Name_Multi name;
    private String world_id;
    private String character_id;
    private String state;
    private String population;
    private boolean isRegistered;
    private WorldEvent lastAlert;

    public Name_Multi getName() {
        return this.name;
    }

    public void setName(Name_Multi name) {
        this.name = name;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWorld_id() {
        return world_id;
    }

    public void setWorld_id(String world_id) {
        this.world_id = world_id;
    }

    public String getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(String character_id) {
        this.character_id = character_id;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public WorldEvent getLastAlert() {
        return lastAlert;
    }

    public void setLastAlert(WorldEvent lastAlert) {
        this.lastAlert = lastAlert;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

}
