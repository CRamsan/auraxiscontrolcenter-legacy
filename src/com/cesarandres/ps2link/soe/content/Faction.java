package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.world.Name_Multi;

public class Faction {

    public static final String VS = "1";
    public static final String NC = "2";
    public static final String TR = "3";

    private String id;
    private Name_Multi name;
    private String code;
    private String icon;

    public Name_Multi getName() {
	return name;
    }

    public void setName(Name_Multi name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }
}
