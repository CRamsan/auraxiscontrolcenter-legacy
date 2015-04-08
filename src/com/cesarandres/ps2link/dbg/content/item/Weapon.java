package com.cesarandres.ps2link.dbg.content.item;
 public class Weapon{
    	private String character_id;
    	private String item_id;
    	private String last_save;
		private String last_save_date;
    	private String stat_name;
    	private WeaponInfo item_id_join_item;
    	private WeaponInfo vehicle_id_join_vehicle;
    	private int value_nc;
    	private int value_tr;
    	private int value_vs;
    	
    	public String getCharacter_id() {
			return character_id;
		}
		public void setCharacter_id(String character_id) {
			this.character_id = character_id;
		}
		public String getItem_id() {
			return item_id;
		}
		public void setItem_id(String item_id) {
			this.item_id = item_id;
		}
		public String getLast_save() {
			return last_save;
		}
		public void setLast_save(String last_save) {
			this.last_save = last_save;
		}
		public String getLast_save_date() {
			return last_save_date;
		}
		public void setLast_save_date(String last_save_date) {
			this.last_save_date = last_save_date;
		}
		public String getStat_name() {
			return stat_name;
		}
		public void setStat_name(String stat_name) {
			this.stat_name = stat_name;
		}
		public int getValue_nc() {
			return value_nc;
		}
		public void setValue_nc(int value_nc) {
			this.value_nc = value_nc;
		}
		public int getValue_tr() {
			return value_tr;
		}
		public void setValue_tr(int value_tr) {
			this.value_tr = value_tr;
		}
		public int getValue_vs() {
			return value_vs;
		}
		public void setValue_vs(int value_vs) {
			this.value_vs = value_vs;
		}
		public WeaponInfo getItem_id_join_item() {
			return item_id_join_item;
		}
		public void setItem_id_join_item(WeaponInfo item_id_join_item) {
			this.item_id_join_item = item_id_join_item;
		}
		public WeaponInfo getVehicle_id_join_vehicle() {
			return vehicle_id_join_vehicle;
		}
		public void setVehicle_id_join_vehicle(WeaponInfo vehicle_id_join_vehicle) {
			this.vehicle_id_join_vehicle = vehicle_id_join_vehicle;
		}
    }