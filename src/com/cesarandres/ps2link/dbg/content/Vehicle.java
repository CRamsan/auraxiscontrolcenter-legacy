package com.cesarandres.ps2link.dbg.content;

import com.cesarandres.ps2link.dbg.content.item.IContainDrawable;

public class Vehicle implements IContainDrawable {

    private Name name;
    private Description description;
    private String cost;
    private String cost_resource_id;
    private String image_id;
    private String image_path;
    private String image_set_id;
    private String type_id;
    private String type_name;
    private String vehicle_id;

    public Name getName() {
	return name;
    }

    public void setName(Name name) {
	this.name = name;
    }

    public Description getDescription() {
	return description;
    }

    public void setDescription(Description description) {
	this.description = description;
    }

    public String getCost() {
	return cost;
    }

    public void setCost(String cost) {
	this.cost = cost;
    }

    public String getCost_resource_id() {
	return cost_resource_id;
    }

    public void setCost_resource_id(String cost_resource_id) {
	this.cost_resource_id = cost_resource_id;
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

    public String getType_id() {
	return type_id;
    }

    public void setType_id(String type_id) {
	this.type_id = type_id;
    }

    public String getType_name() {
	return type_name;
    }

    public void setType_name(String type_name) {
	this.type_name = type_name;
    }

    public String getVehicle_id() {
	return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
	this.vehicle_id = vehicle_id;
    }

    @Override
    public String getNameText() {
	return getName().getEn();
    }

    @Override
    public String getImagePath() {
	return getImage_path();
    }

}
