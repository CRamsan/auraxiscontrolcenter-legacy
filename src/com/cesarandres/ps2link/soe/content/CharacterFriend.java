package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.character.Name;



public class CharacterFriend {
	private Name name;
	private String character_id;
	private String last_login_time;
	private int online;

	public String getCharacter_id() {
		return character_id;
	}

	public void setCharacter_id(String characted_id) {
		this.character_id = characted_id;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}
}
