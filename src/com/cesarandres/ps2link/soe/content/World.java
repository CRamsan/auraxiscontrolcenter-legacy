
package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.world.Name;

public class World{
   	private Name name;
   	private String world_id;
   	private String state;

 	public Name getName(){
		return this.name;
	}
	public void setName(Name name){
		this.name = name;
	}
 	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
	public String getWorld_id() {
		return world_id;
	}
	public void setWorld_id(String world_id) {
		this.world_id = world_id;
	}
}
