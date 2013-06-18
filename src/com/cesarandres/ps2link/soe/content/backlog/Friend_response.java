
package com.cesarandres.ps2link.soe.content.backlog;

import java.util.List;

public class Friend_response{
   	private List characters_friend_list;
   	private boolean internal;
   	private Number returned;
   	private Timing timing;

 	public List getCharacters_friend_list(){
		return this.characters_friend_list;
	}
	public void setCharacters_friend_list(List characters_friend_list){
		this.characters_friend_list = characters_friend_list;
	}
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
}
