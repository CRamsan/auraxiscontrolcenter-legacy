
package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DirectiveTreeCategory {

    @Expose
    private Name___ name;
    @SerializedName("directive_tree_category_id")
    @Expose
    private String directiveTreeCategoryId;

    private ArrayList<CharacterDirectiveTree> characterDirectiveTreeList;
    
    /**
     * 
     * @return
     *     The name
     */
    public Name___ getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(Name___ name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The directiveTreeCategoryId
     */
    public String getDirectiveTreeCategoryId() {
        return directiveTreeCategoryId;
    }

    /**
     * 
     * @param directiveTreeCategoryId
     *     The directive_tree_category_id
     */
    public void setDirectiveTreeCategoryId(String directiveTreeCategoryId) {
        this.directiveTreeCategoryId = directiveTreeCategoryId;
    }

	public ArrayList<CharacterDirectiveTree> getCharacterDirectiveTreeList() {
		return characterDirectiveTreeList;
	}

	public void registerCharacterDirectiveTreeList(
			CharacterDirectiveTree characterDirectiveTree) {
		if(this.characterDirectiveTreeList == null){
			this.characterDirectiveTreeList = new ArrayList<CharacterDirectiveTree>();
		}
		this.characterDirectiveTreeList.add(characterDirectiveTree);
	}

}
