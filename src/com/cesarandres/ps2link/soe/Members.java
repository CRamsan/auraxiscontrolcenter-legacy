
package com.cesarandres.ps2link.soe;

public class Members{
   	private String character_id;
   	private String member_since;
   	private String online_status;
   	private String rank;
   	private String rank_ordinal;

 	public String getCharacter_id(){
		return this.character_id;
	}
	public void setCharacter_id(String character_id){
		this.character_id = character_id;
	}
 	public String getMember_since(){
		return this.member_since;
	}
	public void setMember_since(String member_since){
		this.member_since = member_since;
	}
 	public String getOnline_status(){
		return this.online_status;
	}
	public void setOnline_status(String online_status){
		this.online_status = online_status;
	}
 	public String getRank(){
		return this.rank;
	}
	public void setRank(String rank){
		this.rank = rank;
	}
 	public String getRank_ordinal(){
		return this.rank_ordinal;
	}
	public void setRank_ordinal(String rank_ordinal){
		this.rank_ordinal = rank_ordinal;
	}
}
