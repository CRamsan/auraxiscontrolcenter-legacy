
package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;



public class CharacterDirectiveTree {
            
    private DirectiveTree directive_tree_id_join_directive_tree;
    private String character_id;
    private String completion_time_date;
    private String completion_time;
    private String current_directive_tier_id;
    private String currrent_level;
    private String directive_tree_id;
    private ArrayList<CharacterDirectiveTier> directiveTiers;

	public DirectiveTree getDirective_tree_id_join_directive_tree() {
		return directive_tree_id_join_directive_tree;
	}
	public void setDirective_tree_id_join_directive_tree(
			DirectiveTree directive_tree_id_join_directive_tree) {
		this.directive_tree_id_join_directive_tree = directive_tree_id_join_directive_tree;
	}
	public String getCharacter_id() {
		return character_id;
	}
	public void setCharacter_id(String character_id) {
		this.character_id = character_id;
	}
	public String getCompletion_time() {
		return completion_time;
	}
	public void setCompletion_time(String completion_time) {
		this.completion_time = completion_time;
	}
	public String getCurrent_directive_tier_id() {
		return current_directive_tier_id;
	}
	public void setCurrent_directive_tier_id(String current_directive_tier_id) {
		this.current_directive_tier_id = current_directive_tier_id;
	}
	public String getCurrrent_level() {
		return currrent_level;
	}
	public void setCurrrent_level(String currrent_level) {
		this.currrent_level = currrent_level;
	}
	public String getDirective_tree_id() {
		return directive_tree_id;
	}
	public void setDirective_tree_id(String directive_tree_id) {
		this.directive_tree_id = directive_tree_id;
	}
	public String getCompletion_time_date() {
		return completion_time_date;
	}
	public void setCompletion_time_date(String completion_time_date) {
		this.completion_time_date = completion_time_date;
	}

	public ArrayList<CharacterDirectiveTier> getDirectiveTiers() {
		return directiveTiers;
	}

	public void registerDirectiveTiers(CharacterDirectiveTier newDirectiveTier) {
		if(this.directiveTiers == null){
			this.directiveTiers = new ArrayList<CharacterDirectiveTier>();
		}
		this.directiveTiers.add(newDirectiveTier);
	}
}