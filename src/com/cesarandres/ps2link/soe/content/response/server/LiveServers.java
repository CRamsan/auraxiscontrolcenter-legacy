package com.cesarandres.ps2link.soe.content.response.server;

import com.google.gson.annotations.SerializedName;

public class LiveServers {
	@SerializedName("Briggs (AU)")
	private LiveServer Briggs;
	
	@SerializedName("Ceres (EU)")
	private LiveServer Ceres;
	
	@SerializedName("Cobalt (EU)")
	private LiveServer Cobalt;
	
	@SerializedName("Connery (US West)")
	private LiveServer Connery;
	
	@SerializedName("Mattherson (US East)")
	private LiveServer Mattherson;
	
	@SerializedName("Miller (EU)")
	private LiveServer Miller;
	
	@SerializedName("Waterson (US East)")
	private LiveServer Waterson;
	
	@SerializedName("Woodman (EU)")
	private LiveServer Woodman;
		
	public LiveServer getBriggs() {
		return Briggs;
	}

	public void setBriggs(LiveServer briggs) {
		Briggs = briggs;
	}

	public LiveServer getCeres() {
		return Ceres;
	}

	public void setCeres(LiveServer ceres) {
		Ceres = ceres;
	}

	public LiveServer getCobalt() {
		return Cobalt;
	}

	public void setCobalt(LiveServer cobalt) {
		Cobalt = cobalt;
	}

	public LiveServer getConnery() {
		return Connery;
	}

	public void setConnery(LiveServer connery) {
		Connery = connery;
	}

	public LiveServer getMattherson() {
		return Mattherson;
	}

	public void setMattherson(LiveServer mattherson) {
		Mattherson = mattherson;
	}

	public LiveServer getMiller() {
		return Miller;
	}

	public void setMiller(LiveServer miller) {
		Miller = miller;
	}

	public LiveServer getWaterson() {
		return Waterson;
	}

	public void setWaterson(LiveServer waterson) {
		Waterson = waterson;
	}

	public LiveServer getWoodman() {
		return Woodman;
	}

	public void setWoodman(LiveServer woodman) {
		Woodman = woodman;
	}
}
