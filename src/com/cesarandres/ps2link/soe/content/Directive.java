
package com.cesarandres.ps2link.soe.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Directive {

    @Expose
    private Name name;
    @Expose
    private Description description;
    @SerializedName("directive_id")
    @Expose
    private String directiveId;
    @SerializedName("directive_tier_id")
    @Expose
    private String directiveTierId;
    @SerializedName("directive_tier_id_join_directive_tier")
    @Expose
    private DirectiveTier directiveTierIdJoinDirectiveTier;
    @SerializedName("directive_tree_id")
    @Expose
    private String directiveTreeId;
    @SerializedName("directive_tree_id_join_directive_tree")
    @Expose
    private DirectiveTree directiveTreeIdJoinDirectiveTree;
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("image_set_id")
    @Expose
    private String imageSetId;
    @SerializedName("objective_set_id")
    @Expose
    private String objectiveSetId;

    private DirectiveObjective directiveObjective;
    
    /**
     * 
     * @return
     *     The name
     */
    public Name getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(Description description) {
        this.description = description;
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
     *     The directiveTierIdJoinDirectiveTier
     */
    public DirectiveTier getDirectiveTierIdJoinDirectiveTier() {
        return directiveTierIdJoinDirectiveTier;
    }

    /**
     * 
     * @param directiveTierIdJoinDirectiveTier
     *     The directive_tier_id_join_directive_tier
     */
    public void setDirectiveTierIdJoinDirectiveTier(DirectiveTier directiveTierIdJoinDirectiveTier) {
        this.directiveTierIdJoinDirectiveTier = directiveTierIdJoinDirectiveTier;
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
     *     The directiveTreeIdJoinDirectiveTree
     */
    public DirectiveTree getDirectiveTreeIdJoinDirectiveTree() {
        return directiveTreeIdJoinDirectiveTree;
    }

    /**
     * 
     * @param directiveTreeIdJoinDirectiveTree
     *     The directive_tree_id_join_directive_tree
     */
    public void setDirectiveTreeIdJoinDirectiveTree(DirectiveTree directiveTreeIdJoinDirectiveTree) {
        this.directiveTreeIdJoinDirectiveTree = directiveTreeIdJoinDirectiveTree;
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
     *     The objectiveSetId
     */
    public String getObjectiveSetId() {
        return objectiveSetId;
    }

    /**
     * 
     * @param objectiveSetId
     *     The objective_set_id
     */
    public void setObjectiveSetId(String objectiveSetId) {
        this.objectiveSetId = objectiveSetId;
    }

	public DirectiveObjective getDirectiveObjective() {
		return directiveObjective;
	}

	public void setDirectiveObjective(DirectiveObjective directiveObjective) {
		this.directiveObjective = directiveObjective;
	}

}
