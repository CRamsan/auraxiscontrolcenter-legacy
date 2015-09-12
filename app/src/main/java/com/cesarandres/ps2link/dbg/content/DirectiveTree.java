package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.world.Name_Multi;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DirectiveTree {

    @Expose
    private Name_Multi name;

    @SerializedName("directive_tree_category_id")
    @Expose
    private String directiveTreeCategoryId;

    @SerializedName("directive_tree_category_id_join_directive_tree_category")
    @Expose
    private DirectiveTreeCategory directiveTreeCategoryIdJoinDirectiveTreeCategory;

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

    /**
     * @return The directiveTreeCategoryIdJoinDirectiveTreeCategory
     */
    public DirectiveTreeCategory getDirectiveTreeCategoryIdJoinDirectiveTreeCategory() {
        return directiveTreeCategoryIdJoinDirectiveTreeCategory;
    }

    /**
     * @param directiveTreeCategoryIdJoinDirectiveTreeCategory The directive_tree_category_id_join_directive_tree_category
     */
    public void setDirectiveTreeCategoryIdJoinDirectiveTreeCategory(DirectiveTreeCategory directiveTreeCategoryIdJoinDirectiveTreeCategory) {
        this.directiveTreeCategoryIdJoinDirectiveTreeCategory = directiveTreeCategoryIdJoinDirectiveTreeCategory;
    }

    /**
     * @return The directiveTreeId
     */
    public String getDirectiveTreeId() {
        return directiveTreeId;
    }

    /**
     * @param directiveTreeId The directive_tree_id
     */
    public void setDirectiveTreeId(String directiveTreeId) {
        this.directiveTreeId = directiveTreeId;
    }

    /**
     * @return The imageId
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * @param imageId The image_id
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * @return The imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath The image_path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return The imageSetId
     */
    public String getImageSetId() {
        return imageSetId;
    }

    /**
     * @param imageSetId The image_set_id
     */
    public void setImageSetId(String imageSetId) {
        this.imageSetId = imageSetId;
    }
}
