package com.victoromameh.dao;

import com.victoromameh.model.UserCredentials;

public interface UserCredentialsDao {

	public boolean verifyIfUserAlreadyExists(UserCredentials newUser);
	public void registerNewUser(UserCredentials newUser);
	public boolean verifyLoginInput(UserCredentials user);
	
}
