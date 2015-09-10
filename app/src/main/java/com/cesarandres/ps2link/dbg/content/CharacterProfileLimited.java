package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.character.BattleRank;
import com.cesarandres.ps2link.dbg.content.character.Name;

public class CharacterProfileLimited {
    private String character_id;
    private Name name;
    private String faction_id;
    private BattleRank battle_rank;

    public String getCharacter_Id() {
	return this.character_id;
    }

    public void setCharacter_Id(String id) {
	this.character_id = id;
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
