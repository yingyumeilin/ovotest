package com.example.oto01.model;

public class MapCity {

	public String snippet;
	public String title;

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "MapCity [snippet=" + snippet + ", title=" + title
				+ ", toString()=" + super.toString() + "]";
	}

}
