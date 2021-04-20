package com.victoromameh.model;

import java.util.Map;

public class UserAccount {

    private int userID;
    private int accountId;
	private String username;
    private String password;
    private double balance;
    private String address;
    private Map<Wine, Integer> wineScores;
    

    
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Map<Wine, Integer> getWineScores() {
		return wineScores;
	}

	public void setWineScores(Map<Wine, Integer> wineScores) {
		this.wineScores = wineScores;
	}
    
    
	
}
