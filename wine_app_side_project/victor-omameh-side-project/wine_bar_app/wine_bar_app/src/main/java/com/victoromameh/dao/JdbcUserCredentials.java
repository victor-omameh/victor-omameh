package com.victoromameh.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.victoromameh.model.UserCredentials;

public class JdbcUserCredentials implements UserCredentialsDao{

	private JdbcTemplate jdbcTemplate;
	
	public JdbcUserCredentials(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public boolean verifyIfUserAlreadyExists(UserCredentials newUser) {
		String username = newUser.getUsername();
		boolean userAlreadyExists = false;

		List<String> registeredUsers = getListOfUsers();
		
		for (String registeredUser : registeredUsers) {
			if (username.equalsIgnoreCase(registeredUser)) {
				userAlreadyExists = true;
			}
		}
		return userAlreadyExists;
	}

	@Override
	public void registerNewUser(UserCredentials newUser) {
		createNewAccount(createNewUser(newUser));
	}
	
	@Override
	public boolean verifyLoginInput(UserCredentials user) {
		boolean inputMatch = false;
		if (!verifyIfUserAlreadyExists(user)) {
			return inputMatch;
		}
		if (verifyCorrectPassword(user)) {
			inputMatch = true;
		}
		return inputMatch;
	}
	
	
/*
 * 
 * PRIVATE METHODS
 * 	
 */
	
	private int createNewUser(UserCredentials newUser) {
		String sql = "INSERT INTO users (user_id, user_name, password) VALUES (default, ?, ?) RETURNING user_id";
		int userId = jdbcTemplate.queryForObject(sql, int.class, newUser.getUsername(), newUser.getPassword());
		
		return userId;
	}
	
	private void createNewAccount(int userId) {
		
		String sql = "INSERT INTO accounts (account_id, user_id, balance) VALUES (DEFAULT, ?, 0.00) RETURNING account_id";
		jdbcTemplate.queryForRowSet(sql, userId);
		
	}
	
	private List<String> getListOfUsers(){
		List<String> users = new ArrayList<String>();
		
		String sql = "SELECT user_name FROM users";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		
		while(row.next()) {
			users.add(row.getString("user_name"));
		}
		
		return users;
	}


	
	private boolean verifyCorrectPassword(UserCredentials newUser) {
		
		boolean passwordVerified = false;
		
		String sql = "SELECT password FROM users WHERE user_name = ?";
		String passwordToMatch = jdbcTemplate.queryForObject(sql, String.class, newUser.getUsername());
		
		if (newUser.getPassword().equals(passwordToMatch)) {
			passwordVerified = true;
		}
		return passwordVerified;
	}
	

}
