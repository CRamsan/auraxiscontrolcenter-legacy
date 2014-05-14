package com.cesarandres.ps2link.soe.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.soe.content.CharacterProfile;

public class Character_list_response {
    private ArrayList<CharacterProfile> character_name_list;
    private ArrayList<CharacterProfile> character_list;
    private ArrayList<CharacterProfile> characters_online_status_list;

    public ArrayList<CharacterProfile> getCharacter_name_list() {
	return this.character_name_list;
    }

    public void setCharacter_name_list(ArrayList<CharacterProfile> character_name_list) {
	this.character_name_list = character_name_list;
    }

    public ArrayList<CharacterProfile> getCharacter_list() {
	return character_list;
    }

    public void setCharacter_list(ArrayList<CharacterProfile> character_list) {
	this.character_list = character_list;
    }

    public ArrayList<CharacterProfile> getCharacters_online_status_list() {
	return characters_online_status_list;
    }

    public void setCharacters_online_status_list(ArrayList<CharacterProfile> characters_online_status_list) {
	this.characters_online_status_list = characters_online_status_list;
    }

}