package com.cesarandres.ps2link.dbg.content.response.server;

import com.google.gson.annotations.SerializedName;

public class LiveServersPS4 {

    @SerializedName("Ceres (EU)")
    private LiveServer Ceres;

    @SerializedName("Crux")
    private LiveServer Crux;

    @SerializedName("Dahaka (EU)")
    private LiveServer Dahaka;

    @SerializedName("Genudine")
    private LiveServer Genudine;

    @SerializedName("Lithcorp (EU)")
    private LiveServer Lithcorp;

    @SerializedName("Palos")
    private LiveServer Palos;

    @SerializedName("Rashnu (EU)")
    private LiveServer Rashnu;

    @SerializedName("Searhus")
    private LiveServer Searhus;

    public LiveServer getCeres() {
        return Ceres;
    }

    public void setCeres(LiveServer ceres) {
        Ceres = ceres;
    }

    public LiveServer getCrux() {
        return Crux;
    }

    public void setCrux(LiveServer crux) {
        Crux = crux;
    }

    public LiveServer getDahaka() {
        return Dahaka;
    }

    public void setDahaka(LiveServer dahaka) {
        Dahaka = dahaka;
    }

    public LiveServer getGenudine() {
        return Genudine;
    }

    public void setGenudine(LiveServer genudine) {
        Genudine = genudine;
    }

    public LiveServer getLithcorp() {
        return Lithcorp;
    }

    public void setLithcorp(LiveServer lithcorp) {
        Lithcorp = lithcorp;
    }

    public LiveServer getPalos() {
        return Palos;
    }

    public void setPalos(LiveServer palos) {
        Palos = palos;
    }

    public LiveServer getRashnu() {
        return Rashnu;
    }

    public void setRashnu(LiveServer rashnu) {
        Rashnu = rashnu;
    }

    public LiveServer getSearhus() {
        return Searhus;
    }

    public void setSearhus(LiveServer searhus) {
        Searhus = searhus;
    }
}
