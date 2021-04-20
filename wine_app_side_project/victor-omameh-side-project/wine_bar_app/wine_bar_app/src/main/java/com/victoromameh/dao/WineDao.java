package com.victoromameh.dao;

import java.util.List;

import com.victoromameh.model.Order;
import com.victoromameh.model.Wine;

public interface WineDao {
	
	public List<Wine> getFullWineList();
	public void orderWine(Order newOrder);
	public List<Wine> getOrderHistory(String username);
	public void addOrRemoveFavorite(Wine selectedWine, String username);
	public void sendFriendRecommendation(String currentUsername, String friendUsername, Wine selectedWine, String message);
	public List<Order>getListOfGifts(String username);
}
