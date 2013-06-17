
package com.cesarandres.ps2link.soe;

import java.util.List;

public class Outfit_response{
   	private boolean internal;
   	private List outfit_list;
   	private Number returned;
   	private Timing timing;

 	public boolean getInternal(){
		return this.internal;
	}
	public void setInternal(boolean internal){
		this.internal = internal;
	}
 	public List getOutfit_list(){
		return this.outfit_list;
	}
	public void setOutfit_list(List outfit_list){
		this.outfit_list = outfit_list;
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
