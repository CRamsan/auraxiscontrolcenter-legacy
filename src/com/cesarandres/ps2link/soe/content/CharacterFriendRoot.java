package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;

public class CharacterFriendRoot {
    private String character_id;
    private String name;
    private ArrayList<CharacterFriend> friend_list;

    public String getCharacter_id() {
	return character_id;
    }

    public void setCharacter_id(String character_id) {
	this.character_id = character_id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public ArrayList<CharacterFriend> getFriend_list() {
	return friend_list;
    }

    public void setFriend_list(ArrayList<CharacterFriend> friend_list) {
	this.friend_list = friend_list;
    }
}
