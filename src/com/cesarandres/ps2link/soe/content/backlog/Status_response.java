package com.cesarandres.ps2link.soe.content.backlog;

import java.util.List;

public class Status_response {
    private List<String> characters_online_status_list;
    private boolean internal;
    private Number returned;
    private Timing timing;

    public List<String> getCharacters_online_status_list() {
	return this.characters_online_status_list;
    }

    public void setCharacters_online_status_list(List<String> characters_online_status_list) {
	this.characters_online_status_list = characters_online_status_list;
    }

    public boolean getInternal() {
	return this.internal;
    }

    public void setInternal(boolean internal) {
	this.internal = internal;
    }

    public Number getReturned() {
	return this.returned;
    }

    public void setReturned(Number returned) {
	this.returned = returned;
    }

    public Timing getTiming() {
	return this.timing;
    }

    public void setTiming(Timing timing) {
	this.timing = timing;
    }
}
