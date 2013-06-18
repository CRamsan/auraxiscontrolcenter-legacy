
package com.cesarandres.ps2link.soe.content.response;

import java.util.List;

import com.cesarandres.ps2link.soe.content.World;

public class Server_response{
   	private List<World> world_list;

 	public List<World> getWorld_list(){
		return this.world_list;
	}
	public void setWorld_list(List<World> world_list){
		this.world_list = world_list;
	}
}
