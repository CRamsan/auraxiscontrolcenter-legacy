package com.cesarandres.ps2link.dbg.content.item;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;

public class WeaponInfo {
    private Name_Multi name;
    private String image_path;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Name_Multi getName() {
        return name;
    }

    public void setName(Name_Multi name) {
        this.name = name;
    }
}