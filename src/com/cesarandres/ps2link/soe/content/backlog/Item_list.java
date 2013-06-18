
package com.cesarandres.ps2link.soe.content.backlog;

public class Item_list{
   	private String consumed_count;
   	private String id;
   	private String override_tint_id;
   	private String stack_count;

 	public String getConsumed_count(){
		return this.consumed_count;
	}
	public void setConsumed_count(String consumed_count){
		this.consumed_count = consumed_count;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getOverride_tint_id(){
		return this.override_tint_id;
	}
	public void setOverride_tint_id(String override_tint_id){
		this.override_tint_id = override_tint_id;
	}
 	public String getStack_count(){
		return this.stack_count;
	}
	public void setStack_count(String stack_count){
		this.stack_count = stack_count;
	}
}
