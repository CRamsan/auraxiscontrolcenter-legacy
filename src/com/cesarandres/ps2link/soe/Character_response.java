package com.cesarandres.ps2link.soe;

import java.util.List;

public class Character_response{
   	private List character_list;
   	private boolean internal;
   	private Number limit;
   	private Number max_ts;
   	private Number min_ts;
   	private Number returned;
   	private Timing timing;

 	public List getCharacter_list(){
		return this.character_list;
	}
	public void setCharacter_list(List character_list){
		this.character_list = character_list;
	}
 	public boolean getInternal(){
		return this.internal;
	}
	public void setInternal(boolean internal){
		this.internal = internal;
	}
 	public Number getLimit(){
		return this.limit;
	}
	public void setLimit(Number limit){
		this.limit = limit;
	}
 	public Number getMax_ts(){
		return this.max_ts;
	}
	public void setMax_ts(Number max_ts){
		this.max_ts = max_ts;
	}
 	public Number getMin_ts(){
		return this.min_ts;
	}
	public void setMin_ts(Number min_ts){
		this.min_ts = min_ts;
	}
 	public Number getReturned(){
		return this.returned;
	}
	public void setReturned(Number returned){
		this.returned = returned;
	}
 	public Timing getTiming(){
		return this.timing;
	}
	public void setTiming(Timing timing){
		this.timing = timing;
	}
}
