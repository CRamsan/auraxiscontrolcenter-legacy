package com.cesarandres.ps2link.soe.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.soe.content.CharacterProfile;

public class Character_response {
	private ArrayList<CharacterProfile> character_name_list;
	private ArrayList<CharacterProfile> character_list;

	public ArrayList<CharacterProfile> getCharacter_name_list() {
		return this.character_name_list;
	}

	public void setCharacter_name_list(
			ArrayList<CharacterProfile> character_name_list) {
		this.character_name_list = character_name_list;
	}

	public ArrayList<CharacterProfile> getCharacter_list() {
		return character_list;
	}

	public void setCharacter_list(ArrayList<CharacterProfile> character_list) {
		this.character_list = character_list;
	}

}
