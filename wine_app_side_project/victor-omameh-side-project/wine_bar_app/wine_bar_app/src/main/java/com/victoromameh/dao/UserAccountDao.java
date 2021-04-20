package com.victoromameh.dao;

import java.util.List;

import com.victoromameh.model.Recommendation;
import com.victoromameh.model.UserAccount;
import com.victoromameh.model.UserAddress;
import com.victoromameh.model.UserCredentials;

public interface UserAccountDao {

	public UserAccount getNewUserAccount(UserCredentials userCredentials);
	public double getCurrentBalance(String username);
	public void updateAccountBalance(UserAccount currentUser);
	public UserAddress getUserAddress(UserAccount currentUser);
	public void addUserAddress(UserAddress userAddress);
	public void updateUserAddress(UserAddress userAddress);
	public void addFriend(String currentUsername, String friendUsername);
	public List<String> getListOfUserNames();
	public List<UserAccount> getListOfFriends(String username);
	public List<Recommendation> getListOfRecommendations(String username);

}
