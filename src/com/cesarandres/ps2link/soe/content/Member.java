package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.character.Name;



public class Member {
	private String character_id;
	private String online_status;
	private String rank;
	private String outfit_id;
	private Name name;

	public String getCharacter_id() {
		return this.character_id;
	}

	public void setCharacter_id(String character_id) {
		this.character_id = character_id;
	}

	public String getOnline_status() {
		return this.online_status;
	}

	public void setOnline_status(String online_status) {
		this.online_status = online_status;
	}

	public String getRank() {
		return this.rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getOutfit_id() {
		return outfit_id;
	}

	public void setOutfit_id(String outfit_id) {
		this.outfit_id = outfit_id;
	}
}
