package com.cesarandres.ps2link.dbg.content;

import com.google.gson.annotations.Expose;


public class Description {

    @Expose
    private String en;

    /**
     * @return The en
     */
    public String getEn() {
        return en;
    }

    /**
     * @param en The en
     */
    public void setEn(String en) {
        this.en = en;
    }

}
