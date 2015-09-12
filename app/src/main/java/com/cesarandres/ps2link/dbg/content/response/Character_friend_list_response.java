package com.cesarandres.ps2link.dbg.content.response;

import com.cesarandres.ps2link.dbg.content.CharacterFriendRoot;

import java.util.ArrayList;

public class Character_friend_list_response {
    private ArrayList<CharacterFriendRoot> characters_friend_list;

    public ArrayList<CharacterFriendRoot> getCharacters_friend_list() {
        return characters_friend_list;
    }

    public void setCharacters_friend_list(ArrayList<CharacterFriendRoot> characters_friend_list) {
        this.characters_friend_list = characters_friend_list;
    }
}
