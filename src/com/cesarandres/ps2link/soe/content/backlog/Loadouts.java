package com.cesarandres.ps2link.soe.content.backlog;

import java.util.List;

public class Loadouts {
    private List<String> current_loadout;
    private List<String> current_vehicle_loadout;
    private List<String> custom_loadouts_list;
    private List<String> custom_vehicle_loadouts_list;

    public List<String> getCurrent_loadout() {
	return this.current_loadout;
    }

    public void setCurrent_loadout(List<String> current_loadout) {
	this.current_loadout = current_loadout;
    }

    public List<String> getCurrent_vehicle_loadout() {
	return this.current_vehicle_loadout;
    }

    public void setCurrent_vehicle_loadout(List<String> current_vehicle_loadout) {
	this.current_vehicle_loadout = current_vehicle_loadout;
    }

    public List<String> getCustom_loadouts_list() {
	return this.custom_loadouts_list;
    }

    public void setCustom_loadouts_list(List<String> custom_loadouts_list) {
	this.custom_loadouts_list = custom_loadouts_list;
    }

    public List<String> getCustom_vehicle_loadouts_list() {
	return this.custom_vehicle_loadouts_list;
    }

    public void setCustom_vehicle_loadouts_list(List<String> custom_vehicle_loadouts_list) {
	this.custom_vehicle_loadouts_list = custom_vehicle_loadouts_list;
    }
}
