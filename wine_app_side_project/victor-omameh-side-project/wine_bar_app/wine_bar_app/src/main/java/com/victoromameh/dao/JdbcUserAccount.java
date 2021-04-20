package com.victoromameh.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.victoromameh.model.Recommendation;
import com.victoromameh.model.UserAccount;
import com.victoromameh.model.UserAddress;
import com.victoromameh.model.UserCredentials;

public class JdbcUserAccount implements UserAccountDao{

	private JdbcTemplate jdbcTemplate;
	
	public JdbcUserAccount (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public UserAccount getNewUserAccount(UserCredentials userCredentials) {
		
		String sql = "SELECT users.user_id, user_name, password, account_id " + 
				"FROM users " + 
				"JOIN accounts ON accounts.user_id = users.user_id " + 
				"WHERE user_name = ?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, userCredentials.getUsername());
		row.next();
		
		return mapRowToUser(row);
	}
	
	@Override
	public double getCurrentBalance(String username) {

		String sql = "SELECT balance FROM accounts\n" + 
				"JOIN users ON users.user_id = accounts.user_id WHERE user_name = ?";
		double currentBalance = jdbcTemplate.queryForObject(sql, double.class, username);
		
		return currentBalance;
	}
	
	@Override
	public void updateAccountBalance(UserAccount currentUser) {
		String sql = "UPDATE accounts SET balance = ? WHERE account_id = ? RETURNING balance";
		jdbcTemplate.queryForRowSet(sql, currentUser.getBalance(), currentUser.getAccountId());
	}
	
	@Override
	public UserAddress getUserAddress(UserAccount currentUser) {
		String sql = "SELECT address_id, street, city, state, zipcode FROM user_address " + 
				"WHERE account_id = ?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, currentUser.getAccountId());
		
		UserAddress userAddress = new UserAddress(currentUser);
		if (row.next()) {
			userAddress = mapRowToAddress(row, currentUser);
		}
		
		return userAddress;
	}
	
	@Override
	public void addUserAddress(UserAddress userAddress) {
		
		String sql = "INSERT INTO user_address (address_id, account_id, street, city, state, zipcode) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING state";
		jdbcTemplate.queryForRowSet(sql, userAddress.getCurrentUser().getAccountId(), userAddress.getStreet(), userAddress.getCity(), userAddress.getState(), userAddress.getZipcode());
		
	}
	
	@Override
	public void updateUserAddress(UserAddress userAddress) {
		
		String sql = "UPDATE user_address SET street = ?, city = ?, state = ?, zipcode = ? WHERE account_id = ? RETURNING account_id";
		jdbcTemplate.queryForRowSet(sql, userAddress.getStreet(), userAddress.getCity(), userAddress.getState(), userAddress.getZipcode(), userAddress.getCurrentUser().getAccountId());
		
	}
	
	@Override
	public void addFriend(String currentUsername, String friendUsername) {
		int accountId = getAccountId(currentUsername);
		int friendAccountId = getAccountId(friendUsername);
		
		String sql = "INSERT INTO friends_list (account_id, friend_account_id) VALUES (?, ?) RETURNING account_id";
		jdbcTemplate.queryForRowSet(sql, accountId, friendAccountId);
		
	}
	
	@Override
	public List<UserAccount> getListOfFriends(String username) {
		int accountId = getAccountId(username);
		
		List<UserAccount> friendsList = new ArrayList<UserAccount>();
		
		String sql = "SELECT friend_account_id FROM friends_list WHERE account_id = ?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);
		
		while(row.next()) {
			UserAccount friend = new UserAccount();
			friend.setAccountId(row.getInt("friend_account_id"));
			friendsList.add(friend);
		}
		
		sql = "SELECT user_name FROM users " + 
				"JOIN accounts ON users.user_id = accounts.user_id " + 
				"WHERE account_id = ?";
		
		for (UserAccount friend : friendsList) {
			String friendName = jdbcTemplate.queryForObject(sql, String.class, friend.getAccountId());
			friend.setUsername(friendName);
		}
		
		return friendsList;
	}
	
	@Override
	public List<String> getListOfUserNames() {
		String sql = "SELECT user_name FROM users";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		
		List<String> users = new ArrayList<String>();
		while(row.next()) {
			users.add(row.getString("user_name"));
		}
		return users;
	}

	@Override
	public List<Recommendation> getListOfRecommendations(String username) {
		List<Recommendation> recommendations = new ArrayList<Recommendation>();
		int accountId = getAccountId(username);
		
		String sql = "SELECT recommendation_number, recommendation_date, wine_id, account_id_to, account_id_from, message FROM recommendations WHERE account_id_to = ?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);
		
		while (row.next()) {
			int accountIdFrom = row.getInt("account_id_from");
			Recommendation recommendation = mapRowToRecommendation(row);
			recommendation.setUserTo(getUsernameFromAccountId(accountId));
			recommendation.setUserFrom(getUsernameFromAccountId(accountIdFrom));
			recommendations.add(recommendation);
		}
		
		return recommendations;
	}

	private Recommendation mapRowToRecommendation(SqlRowSet row) {
		Recommendation recommendation = new Recommendation();
		
		recommendation.setRecommendationNumber(row.getInt("recommendation_number"));
		recommendation.setDate(row.getDate("recommendation_date").toLocalDate());
		recommendation.setWineId(row.getInt("wine_id"));
		recommendation.setMessage(row.getString("message"));
		
		return recommendation;
	}
	
	
	
	private UserAccount mapRowToUser(SqlRowSet row) {
		UserAccount user = new UserAccount();
		user.setUserID(row.getInt("user_id"));
		user.setUsername(row.getString("user_name"));
		user.setPassword(row.getString("password"));
		user.setAccountId(row.getInt("account_id"));
		
		return user;
	}
	
	private UserAddress mapRowToAddress(SqlRowSet row, UserAccount currentUser) {
		UserAddress address = new UserAddress(currentUser);
		address.setStreet(row.getString("street"));
		address.setCity(row.getString("city"));
		address.setState(row.getString("state"));
		address.setZipcode(row.getInt("zipcode"));
		
		return address;
		
	}
	
	private int getAccountId(String username) {
		String sql = "SELECT account_id FROM accounts JOIN users ON users.user_id = accounts.user_id WHERE user_name = ?";
		return jdbcTemplate.queryForObject(sql, int.class, username);
	}
	
	private String getUsernameFromAccountId(int accountId) {
		
		String sql = "SELECT user_name FROM accounts " + 
				"JOIN users ON users.user_id = accounts.user_id WHERE account_id = ?";
		
		return jdbcTemplate.queryForObject(sql, String.class, accountId);
	}


}
