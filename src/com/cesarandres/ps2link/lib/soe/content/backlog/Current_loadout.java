package com.cesarandres.ps2link.lib.soe.content.backlog;

import java.util.List;

public class Current_loadout {
	private String id;
	private String profile_id;
	private List slots_list;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfile_id() {
		return this.profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	public List getSlots_list() {
		return this.slots_list;
	}

	public void setSlots_list(List slots_list) {
		this.slots_list = slots_list;
	}
}
