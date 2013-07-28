package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.character.BattleRank;
import com.cesarandres.ps2link.soe.content.character.Name;

public class CharacterProfileLimited {
	private String id;
	private Name name;
	private String faction_id;
	private BattleRank battle_rank;

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

	public String getFaction_id() {
		return faction_id;
	}

	public void setFaction_id(String faction_id) {
		this.faction_id = faction_id;
	}

	public BattleRank getBattle_rank() {
		return battle_rank;
	}

	public void setBattle_rank(BattleRank battle_rank) {
		this.battle_rank = battle_rank;
	}
}
