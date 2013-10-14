package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;

public class Outfit {
	private String outfit_id;
	private String name;
	private String alias;
	private String leader_character_id;
	private int member_count;
	private String time_created;
	private String world_id;
	private String faction_id;
	private boolean isCached;
	private ArrayList<Member> members;

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getOutfit_Id() {
		return this.outfit_id;
	}

	public void setOutfit_Id(String id) {
		this.outfit_id = id;
	}

	public String getLeader_character_id() {
		return this.leader_character_id;
	}

	public void setLeader_character_id(String leader_character_id) {
		this.leader_character_id = leader_character_id;
	}

	public int getMember_count() {
		return this.member_count;
	}

	public void setMember_count(int member_count) {
		this.member_count = member_count;
	}

	public ArrayList<Member> getMembers() {
		return this.members;
	}

	public void setMembers(ArrayList<Member> members) {
		this.members = members;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime_created() {
		return this.time_created;
	}

	public void setTime_created(String time_created) {
		this.time_created = time_created;
	}

	public String getWorld_id() {
		return world_id;
	}

	public void setWorld_id(String world_id) {
		this.world_id = world_id;
	}

	public String getFaction_id() {
		return faction_id;
	}

	public void setFaction_id(String faction_id) {
		this.faction_id = faction_id;
	}

	public boolean isCached() {
		return isCached;
	}

	public void setCached(boolean isCached) {
		this.isCached = isCached;
	}

}
