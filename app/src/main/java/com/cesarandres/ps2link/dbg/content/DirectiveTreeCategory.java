package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class DirectiveTreeCategory implements Comparable<DirectiveTreeCategory> {

    @Expose
    private Name_Multi name;
    @SerializedName("directive_tree_category_id")
    @Expose
    private String directiveTreeCategoryId;

    private ArrayList<CharacterDirectiveTree> characterDirectiveTreeList;

    private int maxValue;
    private int currentValue;

    /**
     * @return The name
     */
    public Name_Multi getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(Name_Multi name) {
        this.name = name;
    }

    /**
     * @return The directiveTreeCategoryId
     */
    public String getDirectiveTreeCategoryId() {
        return directiveTreeCategoryId;
    }

    /**
     * @param directiveTreeCategoryId The directive_tree_category_id
     */
    public void setDirectiveTreeCategoryId(String directiveTreeCategoryId) {
        this.directiveTreeCategoryId = directiveTreeCategoryId;
    }

    public ArrayList<CharacterDirectiveTree> getCharacterDirectiveTreeList() {
        return characterDirectiveTreeList;
    }

    public void registerCharacterDirectiveTreeList(
            CharacterDirectiveTree characterDirectiveTree) {
        if (this.characterDirectiveTreeList == null) {
            this.characterDirectiveTreeList = new ArrayList<CharacterDirectiveTree>();
        }
        this.characterDirectiveTreeList.add(characterDirectiveTree);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void generateValues() {
        for (CharacterDirectiveTree tree : characterDirectiveTreeList) {
            this.maxValue += 145;
            switch (Integer.parseInt(tree.getCurrent_directive_tier_id())) {
                case 0:
                    tree.setCurrent_level_value(0);
                    break;
                case 1:
                    tree.setCurrent_level_value(5);
                    break;
                case 2:
                    tree.setCurrent_level_value(15);
                    break;
                case 3:
                    tree.setCurrent_level_value(45);
                    break;
                case 4:
                    tree.setCurrent_level_value(145);
                    break;
                default:
                    break;
            }
            this.currentValue += tree.getCurrent_level_value();
        }
    }

    @Override
    public int compareTo(DirectiveTreeCategory another) {
        return this.getName().getLocalizedName().compareToIgnoreCase(another.getName().getLocalizedName());
    }
}
