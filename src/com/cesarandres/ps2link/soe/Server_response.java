
package com.cesarandres.ps2link.soe;

import java.util.List;

public class Server_response{
   	private boolean internal;
   	private Number returned;
   	private Timing timing;
   	private List world_list;

 	public boolean getInternal(){
		return this.internal;
	}
	public void setInternal(boolean internal){
		this.internal = internal;
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
 	public List getWorld_list(){
		return this.world_list;
	}
	public void setWorld_list(List world_list){
		this.world_list = world_list;
	}
}
