package com.cesarandres.ps2link.soe.content.response;

import java.util.ArrayList;

import com.cesarandres.ps2link.soe.content.Item;
import com.cesarandres.ps2link.soe.content.Vehicle;

public class Item_list_response {
    private ArrayList<Item> item_list;
    private ArrayList<Vehicle> vehicle_list;

    public ArrayList<Item> getItem_list() {
	return item_list;
    }

    public void setItem_list(ArrayList<Item> item_list) {
	this.item_list = item_list;
    }

    public ArrayList<Vehicle> getVehicle_list() {
	return vehicle_list;
    }

    public void setEvent_list(ArrayList<Vehicle> vehicle_list) {
	this.vehicle_list = vehicle_list;
    }
}
