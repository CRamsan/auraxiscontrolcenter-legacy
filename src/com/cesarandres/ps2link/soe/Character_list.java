package com.cesarandres.ps2link.soe;

import java.util.List;

public class Character_list{
   	private Certs certs;
   	private Currency currency;
   	private List experience;
   	private String id;
   	private List item_list;
   	private Loadouts loadouts;
   	private Name name;
   	private Profile profile;
   	private List skill_list;
   	private Times times;
   	private String ts;
   	private Type type;

 	public Certs getCerts(){
		return this.certs;
	}
	public void setCerts(Certs certs){
		this.certs = certs;
	}
 	public Currency getCurrency(){
		return this.currency;
	}
	public void setCurrency(Currency currency){
		this.currency = currency;
	}
 	public List getExperience(){
		return this.experience;
	}
	public void setExperience(List experience){
		this.experience = experience;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public List getItem_list(){
		return this.item_list;
	}
	public void setItem_list(List item_list){
		this.item_list = item_list;
	}
 	public Loadouts getLoadouts(){
		return this.loadouts;
	}
	public void setLoadouts(Loadouts loadouts){
		this.loadouts = loadouts;
	}
 	public Name getName(){
		return this.name;
	}
	public void setName(Name name){
		this.name = name;
	}
 	public Profile getProfile(){
		return this.profile;
	}
	public void setProfile(Profile profile){
		this.profile = profile;
	}
 	public List getSkill_list(){
		return this.skill_list;
	}
	public void setSkill_list(List skill_list){
		this.skill_list = skill_list;
	}
 	public Times getTimes(){
		return this.times;
	}
	public void setTimes(Times times){
		this.times = times;
	}
 	public String getTs(){
		return this.ts;
	}
	public void setTs(String ts){
		this.ts = ts;
	}
 	public Type getType(){
		return this.type;
	}
	public void setType(Type type){
		this.type = type;
	}
}
