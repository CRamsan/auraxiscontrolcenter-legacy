package com.cesarandres.ps2link.soe.content;

public class CharacterEvent {
	private CharacterProfileLimited attacker;
	private CharacterProfileLimited character;
	private String attacker_character_id = "5428010618041058369";
	private String attacker_vehicle_id = "0";
	private String attacker_weapon_id = "7211";
	private String character_id = "5428037660460403521";
	private String is_critical = "0";
	private String is_headshot = "0";
	private String table_type = "kills";
	private String timestamp = "1374629753";
	private String world_id = "18";
	private String zone_id = "8";

	public CharacterProfileLimited getAttacker() {
		return attacker;
	}

	public void setAttacker(CharacterProfileLimited attacker) {
		this.attacker = attacker;
	}

	public CharacterProfileLimited getCharacter() {
		return character;
	}

	public void setCharacter(CharacterProfileLimited defcharacter) {
		this.character = defcharacter;
	}

	public String getAttacker_character_id() {
		return attacker_character_id;
	}

	public void setAttacker_character_id(String attacker_character_id) {
		this.attacker_character_id = attacker_character_id;
	}

	public String getAttacker_vehicle_id() {
		return attacker_vehicle_id;
	}

	public void setAttacker_vehicle_id(String attacker_vehicle_id) {
		this.attacker_vehicle_id = attacker_vehicle_id;
	}

	public String getAttacker_weapon_id() {
		return attacker_weapon_id;
	}

	public void setAttacker_weapon_id(String attacker_weapon_id) {
		this.attacker_weapon_id = attacker_weapon_id;
	}

	public String getCharacter_id() {
		return character_id;
	}

	public void setCharacter_id(String character_id) {
		this.character_id = character_id;
	}

	public String getIs_critical() {
		return is_critical;
	}

	public void setIs_critical(String is_critical) {
		this.is_critical = is_critical;
	}

	public String getIs_headshot() {
		return is_headshot;
	}

	public void setIs_headshot(String is_headshot) {
		this.is_headshot = is_headshot;
	}

	public String getTable_type() {
		return table_type;
	}

	public void setTable_type(String table_type) {
		this.table_type = table_type;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getWorld_id() {
		return world_id;
	}

	public void setWorld_id(String world_id) {
		this.world_id = world_id;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}
}
