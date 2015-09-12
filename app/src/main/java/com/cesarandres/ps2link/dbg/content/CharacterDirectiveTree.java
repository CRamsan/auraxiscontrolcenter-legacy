package com.cesarandres.ps2link.dbg.content;


public class CharacterDirectiveTree implements Comparable<CharacterDirectiveTree> {

    private DirectiveTree directive_tree_id_join_directive_tree;
    private String character_id;
    private String completion_time_date;
    private String completion_time;
    private String current_directive_tier_id;
    private String current_level;
    private int current_level_value;
    private String directive_tree_id;
    private DirectiveTier directive_tier;

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

    public String getCurrent_level() {
        return current_level;
    }

    public void setCurrrent_level(String current_level) {
        this.current_level = current_level;
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

    @Override
    public int compareTo(CharacterDirectiveTree another) {
        this.getDirective_tree_id_join_directive_tree().getName().getLocalizedName().compareTo(another.getDirective_tree_id_join_directive_tree().getName().getLocalizedName());
        return 1;
    }

    public int getCurrent_level_value() {
        return current_level_value;
    }

    public void setCurrent_level_value(int current_level_value) {
        this.current_level_value = current_level_value;
    }

    public DirectiveTier getDirective_tier() {
        return directive_tier;
    }

    public void setDirective_tier(DirectiveTier directive_tier) {
        this.directive_tier = directive_tier;
    }
}