package com.cesarandres.ps2link.soe.content.backlog;

import java.util.List;

public class Characters_friend_list {
	private String character_id;
	private List friend_list;
	private String name;

	public String getCharacter_id() {
		return this.character_id;
	}

	public void setCharacter_id(String character_id) {
		this.character_id = character_id;
	}

	public List getFriend_list() {
		return this.friend_list;
	}

	public void setFriend_list(List friend_list) {
		this.friend_list = friend_list;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
