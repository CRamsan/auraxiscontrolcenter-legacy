package com.cesarandres.ps2link.soe.content.character;

public class Stat {
	private String all_time;
	private String last_save;
	private String last_save_date;
	private String one_life_max;
	private String stat_name;
	private Day day;
	private Week week;
	private Month month;

	public String getAll_time() {
		return all_time;
	}

	public void setAll_time(String all_time) {
		this.all_time = all_time;
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

	public String getOne_life_max() {
		return one_life_max;
	}

	public void setOne_life_max(String one_life_max) {
		this.one_life_max = one_life_max;
	}

	public String getStat_name() {
		return stat_name;
	}

	public void setStat_name(String stat_name) {
		this.stat_name = stat_name;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public Week getWeek() {
		return week;
	}

	public void setWeek(Week week) {
		this.week = week;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public float getToday() {
		return Float.parseFloat(this.day.d01);
	}

	public void setToday(float value) {
		this.day.d01 = Float.toString(value);
	}

	public float getThisWeek() {
		return Float.parseFloat(this.week.w01);
	}

	public void setThisWeek(float value) {
		this.week.w01 = Float.toString(value);
	}

	public float getThisMonth() {
		return Float.parseFloat(this.month.m01);
	}

	public void setThisMonth(float value) {
		this.month.m01 = Float.toString(value);
	}

	public class Day {
		public String d01;
		public String d02;
		public String d03;
		public String d04;
		public String d05;
		public String d06;
		public String d07;
		public String d08;
		public String d09;
		public String d10;
		public String d12;
		public String d13;
		public String d14;
		public String d15;
		public String d16;
		public String d17;
		public String d18;
		public String d19;
		public String d20;
		public String d21;
		public String d22;
		public String d23;
		public String d24;
		public String d25;
		public String d26;
		public String d27;
		public String d28;
		public String d29;
		public String d30;
		public String d31;
	}

	public class Week {
		public String w01;
		public String w02;
		public String w03;
		public String w04;
		public String w05;
		public String w06;
		public String w07;
		public String w08;
		public String w09;
		public String w10;
		public String w12;
		public String w13;
	}

	public class Month {
		public String m01;
		public String m02;
		public String m03;
		public String m04;
		public String m05;
		public String m06;
		public String m07;
		public String m08;
		public String m09;
		public String m10;
		public String m12;
	}
}
