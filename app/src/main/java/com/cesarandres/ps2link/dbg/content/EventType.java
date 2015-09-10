package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;

public class EventType {
	private Name_Multi name;
	private Description description;
	public Name_Multi getName() {
		return name;
	}
	public void setName(Name_Multi name) {
		this.name = name;
	}
	public Description getDescription() {
		return description;
	}
	public void setDescription(Description description) {
		this.description = description;
	}
}
