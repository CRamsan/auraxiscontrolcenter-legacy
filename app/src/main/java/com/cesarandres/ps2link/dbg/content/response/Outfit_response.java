package com.cesarandres.ps2link.dbg.content.response;

import com.cesarandres.ps2link.dbg.content.Outfit;

import java.util.ArrayList;

public class Outfit_response {
    private ArrayList<Outfit> outfit_list;

    public ArrayList<Outfit> getOutfit_list() {
        return this.outfit_list;
    }

    public void setOutfit_list(ArrayList<Outfit> outfit_list) {
        this.outfit_list = outfit_list;
    }
}
