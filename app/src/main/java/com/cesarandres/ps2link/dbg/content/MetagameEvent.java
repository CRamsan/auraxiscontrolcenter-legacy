package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;

/**
 * Created by cramsan on 9/13/15.
 */
public class MetagameEvent {
    private String metagame_event_id;
    private String type;
    private String experience_bonus;
    private Name_Multi name;

    public Name_Multi getDescription() {
        return description;
    }

    public void setDescription(Name_Multi description) {
        this.description = description;
    }

    public String getMetagame_event_id() {
        return metagame_event_id;
    }

    public void setMetagame_event_id(String metagame_event_id) {
        this.metagame_event_id = metagame_event_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExperience_bonus() {
        return experience_bonus;
    }

    public void setExperience_bonus(String experience_bonus) {
        this.experience_bonus = experience_bonus;
    }

    public Name_Multi getName() {
        return name;
    }

    public void setName(Name_Multi name) {
        this.name = name;
    }

    private Name_Multi description;
}
