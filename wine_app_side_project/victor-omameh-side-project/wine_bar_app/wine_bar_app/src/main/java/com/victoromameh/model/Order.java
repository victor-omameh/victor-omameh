package com.victoromameh.model;

import java.time.LocalDate;

public class Order {

	private int orderNumber;
	private LocalDate orderDate;
	private int wineId;
	private Wine orderedWine;
	private int userFromId;
	private int userToId;
	private String userFrom;
	private String userTo;
	private boolean favorite;
	private boolean gift;
	private String message;
	
	
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public Wine getOrderedWine() {
		return orderedWine;
	}
	public void setOrderedWine(Wine orderedWine) {
		this.orderedWine = orderedWine;
	}
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public String getUserTo() {
		return userTo;
	}
	public void setUserTo(String userTo) {
		this.userTo = userTo;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public boolean isGift() {
		return gift;
	}
	public void setGift(boolean gift) {
		this.gift = gift;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getUserFromId() {
		return userFromId;
	}
	public void setUserFromId(int userFromId) {
		this.userFromId = userFromId;
	}
	public int getUserToId() {
		return userToId;
	}
	public void setUserToId(int userToId) {
		this.userToId = userToId;
	}
	public int getWineId() {
		return wineId;
	}
	public void setWineId(int wineId) {
		this.wineId = wineId;
	}
	
	
	
	
}
