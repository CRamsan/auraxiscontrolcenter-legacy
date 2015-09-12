package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.item.IContainDrawable;
import com.cesarandres.ps2link.dbg.content.world.Name_Multi;

public class Item implements IContainDrawable {

    private Name_Multi name;
    private Description description;

    private String faction_id;
    private String image_id;
    private String image_path;
    private String image_set_id;
    private String is_default_attachment;
    private String is_vehicle_weapon;
    private String item_category_id;
    private String item_id;
    private String item_type_id;
    private String max_stack_size;

    public Name_Multi getName() {
        return name;
    }

    public void setName(Name_Multi name) {
        this.name = name;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getFaction_id() {
        return faction_id;
    }

    public void setFaction_id(String faction_id) {
        this.faction_id = faction_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getImage_set_id() {
        return image_set_id;
    }

    public void setImage_set_id(String image_set_id) {
        this.image_set_id = image_set_id;
    }

    public String getIs_default_attachment() {
        return is_default_attachment;
    }

    public void setIs_default_attachment(String is_default_attachment) {
        this.is_default_attachment = is_default_attachment;
    }

    public String getIs_vehicle_weapon() {
        return is_vehicle_weapon;
    }

    public void setIs_vehicle_weapon(String is_vehicle_weapon) {
        this.is_vehicle_weapon = is_vehicle_weapon;
    }

    public String getItem_category_id() {
        return item_category_id;
    }

    public void setItem_category_id(String item_category_id) {
        this.item_category_id = item_category_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type_id() {
        return item_type_id;
    }

    public void setItem_type_id(String item_type_id) {
        this.item_type_id = item_type_id;
    }

    public String getMax_stack_size() {
        return max_stack_size;
    }

    public void setMax_stack_size(String max_stack_size) {
        this.max_stack_size = max_stack_size;
    }

    @Override
    public String getNameText() {
        if (name == null) {
            return null;
        } else {
            return getName().getLocalizedName();
        }
    }

    @Override
    public String getImagePath() {
        return getImage_path();
    }
}
