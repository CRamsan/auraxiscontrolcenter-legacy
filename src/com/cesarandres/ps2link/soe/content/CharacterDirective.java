
package com.cesarandres.ps2link.soe.content;


public class CharacterDirective {

    private String character_id;
    private String completion_time;
    private String directive_id;
    private String directive_tree_id;
    
    private Directive directive_id_join_directive;

    private CharacterDirectiveObjective directiveObjective;
    
	public Directive getDirective_id_join_directive() {
		return directive_id_join_directive;
	}

	public void setDirective_id_join_directive(
			Directive directive_id_join_directive) {
		this.directive_id_join_directive = directive_id_join_directive;
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

	public String getDirective_id() {
		return directive_id;
	}

	public void setDirective_id(String directive_id) {
		this.directive_id = directive_id;
	}

	public String getDirective_tree_id() {
		return directive_tree_id;
	}

	public void setDirective_tree_id(String directive_tree_id) {
		this.directive_tree_id = directive_tree_id;
	}

	public CharacterDirectiveObjective getDirectiveObjective() {
		return directiveObjective;
	}

	public void setDirectiveObjective(CharacterDirectiveObjective directiveObjective) {
		this.directiveObjective = directiveObjective;
	}

}
