
package com.cesarandres.ps2link.soe.content;

import java.util.List;

public class Outfit{
   	private String id;
   	private String name;
	private String alias;
   	private String leader_character_id;
   	private String member_count;
   	private String time_created;
   	private String world_id;
	private String faction_id;
   	private List<Member> members;
   	   	
 	public String getAlias(){
		return this.alias;
	}
	public void setAlias(String alias){
		this.alias = alias;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getLeader_character_id(){
		return this.leader_character_id;
	}
	public void setLeader_character_id(String leader_character_id){
		this.leader_character_id = leader_character_id;
	}
 	public String getMember_count(){
		return this.member_count;
	}
	public void setMember_count(String member_count){
		this.member_count = member_count;
	}
 	public List<Member> getMembers(){
		return this.members;
	}
	public void setMembers(List<Member> members){
		this.members = members;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getTime_created(){
		return this.time_created;
	}
	public void setTime_created(String time_created){
		this.time_created = time_created;
	}
	
   	public String getWorld_id() {
		return world_id;
	}
	public void setWorld_id(String world_id) {
		this.world_id = world_id;
	}
	public String getFaction_id() {
		return faction_id;
	}
	public void setFaction_id(String faction_id) {
		this.faction_id = faction_id;
	}

}
