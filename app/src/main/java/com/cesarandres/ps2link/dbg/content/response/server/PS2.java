package com.cesarandres.ps2link.dbg.content.response.server;

import com.google.gson.annotations.SerializedName;

public class PS2 {
    private LiveServers Live;

    @SerializedName("Live PS4")
    private LiveServersPS4 LivePS4;

    public LiveServersPS4 getLivePS4() {
        return LivePS4;
    }

    public void setLivePS4(LiveServersPS4 livePS4) {
        LivePS4 = livePS4;
    }

    public LiveServers getLive() {
        return Live;
    }

    public void setLive(LiveServers Live) {
        this.Live = Live;
    }
}
