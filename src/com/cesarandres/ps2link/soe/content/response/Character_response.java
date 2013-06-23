package com.cesarandres.ps2link.soe.content.response;

import java.util.List;

import com.cesarandres.ps2link.soe.content.CharacterProfile;

public class Character_response{
   	private List<CharacterProfile> character_name_list;
   	private List<CharacterProfile> character_list;

 	public List<CharacterProfile> getCharacter_name_list(){
		return this.character_name_list;
	}
	public void setCharacter_name_list(List<CharacterProfile> character_name_list){
		this.character_name_list = character_name_list;
	}
	public List<CharacterProfile> getCharacter_list() {
		return character_list;
	}
	public void setCharacter_list(List<CharacterProfile> character_list) {
		this.character_list = character_list;
	}
}
