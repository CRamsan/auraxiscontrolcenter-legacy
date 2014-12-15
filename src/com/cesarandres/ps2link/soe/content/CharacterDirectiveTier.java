
package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;


public class CharacterDirectiveTier {

 
    private ArrayList<CharacterDirective> charactersDirective;
    private String character_id;
    private String completion_time;
    private String directive_tier_id;
    private String directive_tree_id;
    private DirectiveTier directive_tier_id_join_directive_tier;
    
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

	public String getDirective_tier_id() {
		return directive_tier_id;
	}

	public void setDirective_tier_id(String directive_tier_id) {
		this.directive_tier_id = directive_tier_id;
	}

	public String getDirective_tree_id() {
		return directive_tree_id;
	}

	public void setDirective_tree_id(String directive_tree_id) {
		this.directive_tree_id = directive_tree_id;
	}

	public DirectiveTier getDirective_tier_id_join_directive_tier() {
		return directive_tier_id_join_directive_tier;
	}

	public void setDirective_tier_id_join_directive_tier(
			DirectiveTier directive_tier_id_join_directive_tier) {
		this.directive_tier_id_join_directive_tier = directive_tier_id_join_directive_tier;
	}

	public void setCharactersDirective(
			ArrayList<CharacterDirective> charactersDirective) {
		this.charactersDirective = charactersDirective;
	}

	public ArrayList<CharacterDirective> getCharactersDirective() {
		return charactersDirective;
	}
	
	public void registerDirective (CharacterDirective newDirective){
		if(charactersDirective == null){
			charactersDirective = new ArrayList<CharacterDirective>();
		}
		charactersDirective.add(newDirective);
	}

}
