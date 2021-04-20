package com.victoromameh.model;

import java.time.LocalDate;

public class Recommendation {
	
	private int recommendationNumber;
	private LocalDate date;
	private int wineId;
	private String userTo;
	private String userFrom;
	private String message;
	
	
	public int getRecommendationNumber() {
		return recommendationNumber;
	}
	public void setRecommendationNumber(int recommendationNumber) {
		this.recommendationNumber = recommendationNumber;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public int getWineId() {
		return wineId;
	}
	public void setWineId(int wineId) {
		this.wineId = wineId;
	}
	public String getUserTo() {
		return userTo;
	}
	public void setUserTo(String userTo) {
		this.userTo = userTo;
	}
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
