package com.victoromameh.model;

import java.util.List;

public class Wine {

	private int wineId;
	private String name;
	private String category;
	private String varietal;
	private int vintage;
	private double price;
	private String body;
	private String tannins;
	private String acidity;
	private String region;
	private String style;
	private List<String> fruit;
	private List<String> nonFruit;
	private boolean favorite;
	
	
	
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public int getWineId() {
		return wineId;
	}
	public void setWineId(int wineId) {
		this.wineId = wineId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getVarietal() {
		return varietal;
	}
	public void setVarietal(String varietal) {
		this.varietal = varietal;
	}
	public int getVintage() {
		return vintage;
	}
	public void setVintage(int vintage) {
		this.vintage = vintage;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTannins() {
		return tannins;
	}
	public void setTannins(String tannins) {
		this.tannins = tannins;
	}
	public String getAcidity() {
		return acidity;
	}
	public void setAcidity(String acidity) {
		this.acidity = acidity;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public List<String> getFruit() {
		return fruit;
	}
	public void setFruit(List<String> fruit) {
		this.fruit = fruit;
	}
	public List<String> getNonFruit() {
		return nonFruit;
	}
	public void setNonFruit(List<String> nonFruit) {
		this.nonFruit = nonFruit;
	}
	
	
	
	
}
