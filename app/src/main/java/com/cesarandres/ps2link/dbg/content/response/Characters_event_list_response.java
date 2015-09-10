package com.cesarandres.ps2link.dbg.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.dbg.content.CharacterEvent;

public class Characters_event_list_response {

    private ArrayList<CharacterEvent> characters_event_list;

    public ArrayList<CharacterEvent> getCharacters_event_list() {
	return characters_event_list;
    }

    public void setCharacter_event_list(ArrayList<CharacterEvent> characters_event_list) {
	this.characters_event_list = characters_event_list;
    }
}
