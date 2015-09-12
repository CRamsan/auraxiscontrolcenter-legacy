package com.cesarandres.ps2link.dbg.content.response;

import com.cesarandres.ps2link.dbg.content.Member;
import com.cesarandres.ps2link.dbg.content.Outfit;

import java.util.ArrayList;

public class Outfit_member_response {
    private ArrayList<Outfit> outfit_list;
    private ArrayList<Member> outfit_member_list;

    public ArrayList<Outfit> getOutfit_list() {
        return outfit_list;
    }

    public void setOutfit_list(ArrayList<Outfit> outfit_list) {
        this.outfit_list = outfit_list;
    }

    public ArrayList<Member> getOutfit_member_list() {
        return outfit_member_list;
    }

    public void setOutfit_member_list(ArrayList<Member> outfit_member_list) {
        this.outfit_member_list = outfit_member_list;
    }

}
