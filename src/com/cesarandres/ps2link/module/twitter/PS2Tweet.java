package com.cesarandres.ps2link.module.twitter;


public class PS2Tweet implements Comparable<PS2Tweet> {
	private String user;
	private String content;
	private String tag;
	private Integer  date;
	private String id;

	public PS2Tweet(String id, String user, int date, String content,
			String tag) {
		this.user = user;
		this.date = date;
		this.content = content;
		this.tag = tag;
		this.id = id;
	}

	public PS2Tweet() {
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public int compareTo(PS2Tweet other) {
		return this.date.compareTo(other.getDate()) * -1;
	}
}
