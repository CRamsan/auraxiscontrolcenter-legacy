
package com.cesarandres.ps2link.soe.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CharactersDirectiveList {

    @SerializedName("character_id")
    @Expose
    private String characterId;
    @SerializedName("completion_time")
    @Expose
    private String completionTime;
    @SerializedName("completion_time_date")
    @Expose
    private String completionTimeDate;
    @SerializedName("directive_id")
    @Expose
    private String directiveId;
    @SerializedName("directive_id_join_directive")
    @Expose
    private DirectiveIdJoinDirective directiveIdJoinDirective;
    @SerializedName("directive_tree_id")
    @Expose
    private String directiveTreeId;

    /**
     * 
     * @return
     *     The characterId
     */
    public String getCharacterId() {
        return characterId;
    }

    /**
     * 
     * @param characterId
     *     The character_id
     */
    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    /**
     * 
     * @return
     *     The completionTime
     */
    public String getCompletionTime() {
        return completionTime;
    }

    /**
     * 
     * @param completionTime
     *     The completion_time
     */
    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * 
     * @return
     *     The completionTimeDate
     */
    public String getCompletionTimeDate() {
        return completionTimeDate;
    }

    /**
     * 
     * @param completionTimeDate
     *     The completion_time_date
     */
    public void setCompletionTimeDate(String completionTimeDate) {
        this.completionTimeDate = completionTimeDate;
    }

    /**
     * 
     * @return
     *     The directiveId
     */
    public String getDirectiveId() {
        return directiveId;
    }

    /**
     * 
     * @param directiveId
     *     The directive_id
     */
    public void setDirectiveId(String directiveId) {
        this.directiveId = directiveId;
    }

    /**
     * 
     * @return
     *     The directiveIdJoinDirective
     */
    public DirectiveIdJoinDirective getDirectiveIdJoinDirective() {
        return directiveIdJoinDirective;
    }

    /**
     * 
     * @param directiveIdJoinDirective
     *     The directive_id_join_directive
     */
    public void setDirectiveIdJoinDirective(DirectiveIdJoinDirective directiveIdJoinDirective) {
        this.directiveIdJoinDirective = directiveIdJoinDirective;
    }

    /**
     * 
     * @return
     *     The directiveTreeId
     */
    public String getDirectiveTreeId() {
        return directiveTreeId;
    }

    /**
     * 
     * @param directiveTreeId
     *     The directive_tree_id
     */
    public void setDirectiveTreeId(String directiveTreeId) {
        this.directiveTreeId = directiveTreeId;
    }

}
