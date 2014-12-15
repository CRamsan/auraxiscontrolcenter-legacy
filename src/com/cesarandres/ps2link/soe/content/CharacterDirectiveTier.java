
package com.cesarandres.ps2link.soe.content;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CharacterDirectiveTier {

    @Expose
    private Name_ name;
    @SerializedName("completion_count")
    @Expose
    private String completionCount;
    @SerializedName("directive_points")
    @Expose
    private String directivePoints;
    @SerializedName("directive_tier_id")
    @Expose
    private String directiveTierId;
    @SerializedName("directive_tree_id")
    @Expose
    private String directiveTreeId;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("image_set_id")
    @Expose
    private String imageSetId;
    @SerializedName("reward_set_id")
    @Expose
    private String rewardSetId;

    private ArrayList<CharacterDirective> charactersDirective;
    
    /**
     * 
     * @return
     *     The name
     */
    public Name_ getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(Name_ name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The completionCount
     */
    public String getCompletionCount() {
        return completionCount;
    }

    /**
     * 
     * @param completionCount
     *     The completion_count
     */
    public void setCompletionCount(String completionCount) {
        this.completionCount = completionCount;
    }

    /**
     * 
     * @return
     *     The directivePoints
     */
    public String getDirectivePoints() {
        return directivePoints;
    }

    /**
     * 
     * @param directivePoints
     *     The directive_points
     */
    public void setDirectivePoints(String directivePoints) {
        this.directivePoints = directivePoints;
    }

    /**
     * 
     * @return
     *     The directiveTierId
     */
    public String getDirectiveTierId() {
        return directiveTierId;
    }

    /**
     * 
     * @param directiveTierId
     *     The directive_tier_id
     */
    public void setDirectiveTierId(String directiveTierId) {
        this.directiveTierId = directiveTierId;
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

    /**
     * 
     * @return
     *     The imageId
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * 
     * @param imageId
     *     The image_id
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * 
     * @return
     *     The imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 
     * @param imagePath
     *     The image_path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 
     * @return
     *     The imageSetId
     */
    public String getImageSetId() {
        return imageSetId;
    }

    /**
     * 
     * @param imageSetId
     *     The image_set_id
     */
    public void setImageSetId(String imageSetId) {
        this.imageSetId = imageSetId;
    }

    /**
     * 
     * @return
     *     The rewardSetId
     */
    public String getRewardSetId() {
        return rewardSetId;
    }

    /**
     * 
     * @param rewardSetId
     *     The reward_set_id
     */
    public void setRewardSetId(String rewardSetId) {
        this.rewardSetId = rewardSetId;
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
