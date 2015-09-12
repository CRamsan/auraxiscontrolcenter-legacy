package com.cesarandres.ps2link.dbg.content.response.server;

import com.google.gson.annotations.SerializedName;

public class LiveServers {
    @SerializedName("Briggs (AU)")
    private LiveServer Briggs;

    @SerializedName("Cobalt (EU)")
    private LiveServer Cobalt;

    @SerializedName("Connery (US West)")
    private LiveServer Connery;

    @SerializedName("Emerald (US East)")
    private LiveServer Emerald;

    @SerializedName("Miller (EU)")
    private LiveServer Miller;

    public LiveServer getBriggs() {
        return Briggs;
    }

    public void setBriggs(LiveServer briggs) {
        Briggs = briggs;
    }

    public LiveServer getCobalt() {
        return Cobalt;
    }

    public void setCobalt(LiveServer cobalt) {
        Cobalt = cobalt;
    }

    public LiveServer getEmerald() {
        return Emerald;
    }

    public void setEmerald(LiveServer emerald) {
        Emerald = emerald;
    }

    public LiveServer getConnery() {
        return Connery;
    }

    public void setConnery(LiveServer connery) {
        Connery = connery;
    }

    public LiveServer getMiller() {
        return Miller;
    }

    public void setMiller(LiveServer miller) {
        Miller = miller;
    }
}
