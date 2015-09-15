package com.cesarandres.ps2link.dbg.content;

public class WorldEvent {
    private EventType event;
    private String faction_nc;
    private String faction_tr;
    private String faction_vs;
    private String metagame_event_state;
    private String metagame_event_id;
    private String timestamp;
    private String zone_id;
    private String world_id;
    private String event_type;

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getWorld_id() {
        return world_id;
    }

    public void setWorld_id(String world_id) {
        this.world_id = world_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getMetagame_event_state_name() {
        return metagame_event_state_name;
    }

    public void setMetagame_event_state_name(String metagame_event_state_name) {
        this.metagame_event_state_name = metagame_event_state_name;
    }

    public MetagameEvent getMetagame_event_id_join_metagame_event() {
        return metagame_event_id_join_metagame_event;
    }

    public void setMetagame_event_id_join_metagame_event(MetagameEvent metagame_event_id_join_metagame_event) {
        this.metagame_event_id_join_metagame_event = metagame_event_id_join_metagame_event;
    }

    private String metagame_event_state_name;
    private MetagameEvent metagame_event_id_join_metagame_event;

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getFaction_nc() {
        return faction_nc;
    }

    public void setFaction_nc(String faction_nc) {
        this.faction_nc = faction_nc;
    }

    public String getFaction_tr() {
        return faction_tr;
    }

    public void setFaction_tr(String faction_tr) {
        this.faction_tr = faction_tr;
    }

    public String getFaction_vs() {
        return faction_vs;
    }

    public void setFaction_vs(String faction_vs) {
        this.faction_vs = faction_vs;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMetagame_event_state() {
        return metagame_event_state;
    }

    public void setMetagame_event_state(String metagame_event_state) {
        this.metagame_event_state = metagame_event_state;
    }

    public String getMetagame_event_id() {
        return metagame_event_id;
    }

    public void setMetagame_event_id(String metagame_event_id) {
        this.metagame_event_id = metagame_event_id;
    }
}
