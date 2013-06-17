
package com.cesarandres.ps2link.soe;

import java.util.List;

public class Outfit_list{
   	private String alias;
   	private String id;
   	private String leader_character_id;
   	private String member_count;
   	private List members;
   	private String name;
   	private String time_created;

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
 	public List getMembers(){
		return this.members;
	}
	public void setMembers(List members){
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
}
