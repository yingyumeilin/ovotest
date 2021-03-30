package com.example.oto01.model;

public class MoreListItem {
	private int logo;
	private int submenu;
	private String title;
	private String info;

	public MoreListItem(int logo, String title, String info, int submenu) {
		this.logo = logo;
		this.title = title;
		this.submenu = submenu;
		this.info = info;
	}

	public MoreListItem(int logo, String title, int submenu) {
		this.logo = logo;
		this.title = title;
		this.submenu = submenu;
		this.info = "";
	}
	
	public MoreListItem(int logo, String title) {
		this.logo = logo;
		this.title = title;
	}

	public int getLogo() {
		return logo;
	}

	public void setLogo(int logo) {
		this.logo = logo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSubmenu() {
		return submenu;
	}

	public void setSubmenu(int submenu) {
		this.submenu = submenu;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}