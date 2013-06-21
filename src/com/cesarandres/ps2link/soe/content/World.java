
package com.cesarandres.ps2link.soe.content;

import com.cesarandres.ps2link.soe.content.world.Name_Multi;

public class World{
   	private Name_Multi name;
   	private String world_id;
   	private String state;

 	public Name_Multi getName(){
		return this.name;
	}
	public void setName(Name_Multi name){
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
