
package com.cesarandres.ps2link.soe;

public class World_list{
   	private Description description;
   	private Name name;
   	private String server_id;
   	private String state;

 	public Description getDescription(){
		return this.description;
	}
	public void setDescription(Description description){
		this.description = description;
	}
 	public Name getName(){
		return this.name;
	}
	public void setName(Name name){
		this.name = name;
	}
 	public String getServer_id(){
		return this.server_id;
	}
	public void setServer_id(String server_id){
		this.server_id = server_id;
	}
 	public String getState(){
		return this.state;
	}
	public void setState(String state){
		this.state = state;
	}
}
