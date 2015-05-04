package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;
import com.cesarandres.ps2link.dbg.content.character.BattleRank;
import com.cesarandres.ps2link.dbg.content.character.Certs;
import com.cesarandres.ps2link.dbg.content.character.Server;
import com.cesarandres.ps2link.dbg.content.character.Stats;
import com.cesarandres.ps2link.dbg.content.character.Times;
import com.cesarandres.ps2link.dbg.content.character.Name;

public class CharacterProfile {
    private String character_id;
    private Name name;
    private String active_profile_id;
    private Certs certs;
    private BattleRank battle_rank;
    private Times times;
    private String faction_id;
    private String world_id;
    private Outfit outfit;
    private String outfitName;
    private Profile profile;
    private Stats stats;
    private Server server;
    private int online_status;
    private boolean cached;
    private CharacterProfile character_id_join_character;
    private Namespace namespace;

    public Certs getCerts() {
	return this.certs;
    }

    public void setCerts(Certs certs) {
	this.certs = certs;
    }

    public String getCharacterId() {
	return this.character_id;
    }

    public void setCharacterId(String id) {
	this.character_id = id;
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

    public int getOnline_status() {
	return online_status;
    }

    public void setOnline_status(int online_status) {
	this.online_status = online_status;
    }

    public Outfit getOutfit() {
	return outfit;
    }

    public void setOutfit(Outfit outfit) {
	this.outfit = outfit;
    }

    public String getOutfitName() {
	return outfitName;
    }

    public void setOutfitName(String outfitName) {
	this.outfitName = outfitName;
    }

    public Stats getStats() {
	return stats;
    }

    public void setStats(Stats stats) {
	this.stats = stats;
    }

    public Profile getProfile() {
	return profile;
    }

    public void setProfile(Profile profile) {
	this.profile = profile;
    }

    public Server getServer() {
	return server;
    }

    public void setServer(Server server) {
	this.server = server;
    }

	public Namespace getNamespace() {
		return namespace;
	}

	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}

	public CharacterProfile getCharacter_id_join_character() {
		return character_id_join_character;
	}

	public void setCharacter_id_join_character(
			CharacterProfile character_id_join_character) {
		this.character_id_join_character = character_id_join_character;
	}
}
