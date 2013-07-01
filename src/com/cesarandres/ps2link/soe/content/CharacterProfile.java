package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.backlog.Times;
import com.cesarandres.ps2link.soe.content.character.BattleRank;
import com.cesarandres.ps2link.soe.content.character.Certs;
import com.cesarandres.ps2link.soe.content.character.Name;

public class CharacterProfile {
	private String id;
	private Name name;
	private String active_profile_id;
	private Certs certs;
	private BattleRank battle_rank;
	private Times times;
	private String faction_id;
	private String world_id;
	private boolean cached;
	
	public Certs getCerts() {
		return this.certs;
	}

	public void setCerts(Certs certs) {
		this.certs = certs;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Name getName() {
		return this.name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Times getTimes() {
		return this.times;
	}

	public void setTimes(Times times) {
		this.times = times;
	}

	public String getActive_profile_id() {
		return active_profile_id;
	}

	public void setActive_profile_id(String active_profile_id) {
		this.active_profile_id = active_profile_id;
	}

	public BattleRank getBattle_rank() {
		return battle_rank;
	}

	public void setBattle_rank(BattleRank battle_rank) {
		this.battle_rank = battle_rank;
	}

	public String getFaction_id() {
		return faction_id;
	}

	public void setFaction_id(String faction_id) {
		this.faction_id = faction_id;
	}

	public String getWorld_id() {
		return world_id;
	}

	public void setWorld_id(String world_id) {
		this.world_id = world_id;
	}

	public boolean isCached() {
		return cached;
	}

	public void setCached(boolean cached) {
		this.cached = cached;
	}
}
