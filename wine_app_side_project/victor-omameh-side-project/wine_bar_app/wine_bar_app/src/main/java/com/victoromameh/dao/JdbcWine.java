package com.victoromameh.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.victoromameh.model.Order;
import com.victoromameh.model.Wine;

public class JdbcWine implements WineDao{

	private JdbcTemplate jdbcTemplate;
	
	public JdbcWine(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	@Override
	public List<Wine> getFullWineList() {
		List<Wine> winesWithIntialDetails = getIntialsWineDetails();		
		return getFinalDetails(winesWithIntialDetails);
	}
	
	
	@Override
	public void orderWine(Order newOrder) {
		int accountIdFrom = getAccountId(newOrder.getUserFrom());
		int accountIdTo = getAccountId(newOrder.getUserTo());
		String sql = "INSERT INTO user_history (order_number, order_date, wine_id, account_id_from, account_id_to, favorite, gift, message) "
				+ "VALUES (DEFAULT, CAST (? AS DATE), ?, ?, ?, ?, ?, ?) RETURNING order_number";
		jdbcTemplate.queryForRowSet(sql, newOrder.getOrderDate(), newOrder.getOrderedWine().getWineId(), accountIdFrom, accountIdTo, newOrder.isFavorite(), newOrder.isGift(), newOrder.getMessage());
	}
	
	@Override
	public List<Wine> getOrderHistory(String username) {
		int accountId = getAccountId(username);
		return mapFavoritesToOrderHistory(getFinalDetails(getIntialWineDetailsFromUserHistory(accountId)));
	}
	
	@Override
	public void addOrRemoveFavorite(Wine selectedWine, String username) {
		
		int accountId = getAccountId(username);
		
		if (selectedWine.isFavorite()) {
			String sql = "UPDATE user_history SET favorite = true WHERE wine_id = ? AND account_id_from = ? RETURNING wine_id";
			jdbcTemplate.queryForRowSet(sql, selectedWine.getWineId(), accountId);
		} else {
			String sql = "UPDATE user_history SET favorite = false WHERE wine_id = ? AND account_id_from = ? RETURNING wine_id";
			jdbcTemplate.queryForRowSet(sql, selectedWine.getWineId(), accountId);
		}
		
	}
	
	@Override
	public void sendFriendRecommendation(String currentUsername, String friendUsername, Wine selectedWine, String message) {
		
		int accountIdFrom = getAccountId(currentUsername);
		int accountIdTo = getAccountId(friendUsername);
		
		String sql = "INSERT INTO recommendations (recommendation_number, recommendation_date, wine_id, account_id_to, account_id_from, message) " + 
				"VALUES (DEFAULT, CAST (? AS DATE), ?, ?, ?, ?) RETURNING recommendation_number";
		jdbcTemplate.queryForRowSet(sql, LocalDate.now(), selectedWine.getWineId(), accountIdTo, accountIdFrom, message);
	}
	
	@Override
	public List<Order> getListOfGifts(String username) {
		int accountId = getAccountId(username);
		List<Wine> giftOfWines = mapFavoritesToOrderHistory(getFinalDetails(getIntialWineDetailsFromGiftHistory(accountId)));
		List<Order> giftOrders = new ArrayList<Order>();
		
		String sql = "SELECT order_number, order_date, wine_id, account_id_from, account_id_to, favorite, gift, message FROM user_history WHERE account_id_to = ? AND gift = true";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);
		
		while(row.next()) {
			Order gift = new Order();
			gift.setWineId(row.getInt("wine_id"));
			gift.setOrderDate(row.getDate("order_date").toLocalDate());
			gift.setUserFromId(row.getInt("account_id_from"));
			gift.setUserToId(accountId);
			gift.setMessage(row.getString("message"));
			gift.setUserTo(username);
			gift.setUserFrom(getUsernameFromAccountId(row.getInt("account_id_from")));
			giftOrders.add(gift);
		}
		
		for (Order gift : giftOrders) {
			
			for (Wine wine : giftOfWines) {
				if (gift.getWineId() == wine.getWineId()) {
					gift.setOrderedWine(wine);
				}
			}
		}
		return giftOrders;
	}
	
	
/*
 * 
 * 
 * PRIVATE METHODS
 * 
 * 
 */
	
	private String getUsernameFromAccountId(int accountId) {
		
		String sql = "SELECT user_name FROM accounts " + 
				"JOIN users ON users.user_id = accounts.user_id WHERE account_id = ?";
		
		return jdbcTemplate.queryForObject(sql, String.class, accountId);
	}
	
	private int getAccountId(String username) {
		String sql = "SELECT account_id FROM accounts JOIN users ON users.user_id = accounts.user_id WHERE user_name = ?";
		return jdbcTemplate.queryForObject(sql, int.class, username);
	}
	
	private List<Wine> getIntialWineDetailsFromUserHistory(int accountId){
		
		String sql = "SELECT wines.wine_id, name, vintage, price, wine_type, varietal, tannin_level, body_level, acidity_level, region_name, style_type " + 
				"FROM wines " + 
				"JOIN wine_types ON wine_types.type_id = wines.type_id " + 
				"JOIN varietal ON varietal.varietal_id = wines.varietal_id " + 
				"JOIN tannin ON tannin.tannin_id = wines.tannin_id " + 
				"JOIN body ON body.body_id = wines.body_id " + 
				"JOIN acidity ON acidity.acidity_id = wines.acidity_id " + 
				"JOIN region ON region.region_id = wines.region_id " + 
				"JOIN style ON style.style_id = region.style_id " + 
				"JOIN user_history ON user_history.wine_id = wines.wine_id " + 
				"WHERE account_id_from = ?";
		
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);
		
		List<Wine> allOrderedWines = new ArrayList<Wine>();
		while(row.next()) {
			allOrderedWines.add(mapIntialDetailsToWine(row));
		}
		
		return allOrderedWines;
	}
	
private List<Wine> getIntialWineDetailsFromGiftHistory(int accountId){
		
		String sql = "SELECT wines.wine_id, name, vintage, price, wine_type, varietal, tannin_level, body_level, acidity_level, region_name, style_type " + 
				"FROM wines " + 
				"JOIN wine_types ON wine_types.type_id = wines.type_id " + 
				"JOIN varietal ON varietal.varietal_id = wines.varietal_id " + 
				"JOIN tannin ON tannin.tannin_id = wines.tannin_id " + 
				"JOIN body ON body.body_id = wines.body_id " + 
				"JOIN acidity ON acidity.acidity_id = wines.acidity_id " + 
				"JOIN region ON region.region_id = wines.region_id " + 
				"JOIN style ON style.style_id = region.style_id " + 
				"JOIN user_history ON user_history.wine_id = wines.wine_id " + 
				"WHERE account_id_to = ? AND gift = true";
		
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);
		
		List<Wine> allOrderedWines = new ArrayList<Wine>();
		while(row.next()) {
			allOrderedWines.add(mapIntialDetailsToWine(row));
		}
		
		return allOrderedWines;
	}
	
	
	private List<Wine> getIntialsWineDetails(){
		
		String sql = "SELECT wine_id, name, vintage, price, wine_type, varietal, tannin_level, body_level, acidity_level, region_name, style_type " + 
				"FROM wines " + 
				"JOIN wine_types ON wine_types.type_id = wines.type_id " + 
				"JOIN varietal ON varietal.varietal_id = wines.varietal_id " + 
				"JOIN tannin ON tannin.tannin_id = wines.tannin_id " + 
				"JOIN body ON body.body_id = wines.body_id " + 
				"JOIN acidity ON acidity.acidity_id = wines.acidity_id " + 
				"JOIN region ON region.region_id = wines.region_id " + 
				"JOIN style ON style.style_id = region.style_id";
		
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		
		List<Wine> allWines = new ArrayList<Wine>();
		while(row.next()) {
			allWines.add(mapIntialDetailsToWine(row));
		}
		return allWines;
	}
	
	
	
	
	private List<Wine> getFinalDetails(List<Wine> winesWithIntialDetails){
		return getNonFruitCharacteristics(getFruitCharacteristics(winesWithIntialDetails));
	}
	
	private List<Wine> getFruitCharacteristics(List<Wine> winesWithIntialDetails){
		
		for (Wine wine : winesWithIntialDetails) {
				List<String> fruits = new ArrayList<String>();
				String sql = "SELECT wines.wine_id, fruit_character " + 
						"FROM wines " + 
						"JOIN fruit_wine ON fruit_wine.wine_id = wines.wine_id " + 
						"JOIN fruit ON fruit.fruit_id = fruit_wine.fruit_id " + 
						"WHERE wines.wine_id = ?";
				SqlRowSet row = jdbcTemplate.queryForRowSet(sql, wine.getWineId());
				while(row.next()) {
					fruits.add(mapFruit(row));
				}
				wine.setFruit(fruits);
			}
		return winesWithIntialDetails;
	}

	private List<Wine> getNonFruitCharacteristics(List<Wine> winesWithFruitDetails){
		
		for (Wine wine : winesWithFruitDetails) {
			List<String> nonFruit = new ArrayList<String>();	
			String sql = "SELECT wines.wine_id, non_fruit_character " + 
					"FROM wines " + 
					"JOIN non_fruit_wine ON non_fruit_wine.wine_id = wines.wine_id " + 
					"JOIN non_fruit ON non_fruit.non_fruit_id = non_fruit_wine.non_fruit_id " + 
					"WHERE wines.wine_id = ?";
			SqlRowSet row = jdbcTemplate.queryForRowSet(sql, wine.getWineId());
			while(row.next()) {
				nonFruit.add(mapNonFruit(row));
			}
			wine.setNonFruit(nonFruit);
		}
		return winesWithFruitDetails;
	}
	
	private List<Wine> mapFavoritesToOrderHistory(List<Wine> finalOrderHistoryList){
		String sql = "SELECT favorite FROM user_history WHERE wine_id = ?";
		for (Wine wine : finalOrderHistoryList) {
			
			SqlRowSet row = jdbcTemplate.queryForRowSet(sql, wine.getWineId());
			row.next();
			wine.setFavorite(row.getBoolean("favorite"));
		}
		return finalOrderHistoryList;
	}
	
	
	
	
	private Wine mapIntialDetailsToWine(SqlRowSet row) {
		Wine mappedWine = new Wine();
		
		mappedWine.setWineId(row.getInt("wine_id"));
		mappedWine.setName(row.getString("name"));
		mappedWine.setPrice(row.getDouble("price"));
		mappedWine.setCategory(row.getString("wine_type"));
		mappedWine.setVarietal(row.getString("varietal"));
		mappedWine.setTannins(row.getString("tannin_level"));
		mappedWine.setBody(row.getString("body_level"));
		mappedWine.setAcidity(row.getString("acidity_level"));
		mappedWine.setRegion(row.getString("region_name"));
		mappedWine.setStyle(row.getString("style_type"));

		if (row.getInt("vintage") > 0) {
			mappedWine.setVintage(row.getInt("vintage"));
		}
		return mappedWine;
	}
	private String mapFruit(SqlRowSet row) {
		return row.getString("fruit_character");
	}
	private String mapNonFruit(SqlRowSet row) {
		return row.getString("non_fruit_character");
	}


}
