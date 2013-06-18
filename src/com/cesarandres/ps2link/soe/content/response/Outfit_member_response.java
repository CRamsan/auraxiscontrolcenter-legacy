package com.cesarandres.ps2link.soe.content.response;

import java.util.List;

import com.cesarandres.ps2link.soe.content.Member;

public class Outfit_member_response {
	private List<Member> outfit_member_list;

	public List<Member> getOutfit_list() {
		return this.outfit_member_list;
	}

	public void setOutfit_list(List<Member> outfit_member_list) {
		this.outfit_member_list = outfit_member_list;
	}
}
