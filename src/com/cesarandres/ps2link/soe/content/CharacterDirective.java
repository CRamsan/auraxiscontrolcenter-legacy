
package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CharacterDirective {

    @SerializedName("characters_directive_list")
    @Expose
    private List<CharactersDirectiveList> charactersDirectiveList = new ArrayList<CharactersDirectiveList>();
    @Expose
    private Integer returned;

    /**
     * 
     * @return
     *     The charactersDirectiveList
     */
    public List<CharactersDirectiveList> getCharactersDirectiveList() {
        return charactersDirectiveList;
    }

    /**
     * 
     * @param charactersDirectiveList
     *     The characters_directive_list
     */
    public void setCharactersDirectiveList(List<CharactersDirectiveList> charactersDirectiveList) {
        this.charactersDirectiveList = charactersDirectiveList;
    }

    /**
     * 
     * @return
     *     The returned
     */
    public Integer getReturned() {
        return returned;
    }

    /**
     * 
     * @param returned
     *     The returned
     */
    public void setReturned(Integer returned) {
        this.returned = returned;
    }

}
