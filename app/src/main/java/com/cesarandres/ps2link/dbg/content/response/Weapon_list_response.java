package com.cesarandres.ps2link.dbg.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.dbg.content.item.Weapon;


public class Weapon_list_response {
    private ArrayList<Weapon> characters_weapon_stat_by_faction_list;

	public ArrayList<Weapon> getcharacters_weapon_stat_by_faction_list() {
		return characters_weapon_stat_by_faction_list;
	}

	public void setcharacters_weapon_stat_by_faction_list(
			ArrayList<Weapon> characters_weapon_stat_by_faction_list) {
		this.characters_weapon_stat_by_faction_list = characters_weapon_stat_by_faction_list;
	}
}