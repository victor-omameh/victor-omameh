package com.victoromameh;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.victoromameh.dao.JdbcUserAccount;
import com.victoromameh.dao.UserAccountDao;
import com.victoromameh.model.UserAccount;
import com.victoromameh.model.UserAddress;
import com.victoromameh.model.UserCredentials;

public class JdbcUserAccountIntegrationTest extends DaoIntegrationTest{

	private UserAccountDao userDao;
	private JdbcTemplate jdbcTemplate;
	
	@BeforeClass 
	public static void setupData() {
		setupDataSource();
	}
	
	@AfterClass
	public static void closeData() throws SQLException {
		closeDataSource();
	}
	
	@After
	public void rollbackTransaction() throws SQLException {
		rollback();
	}
	
	@Before
	public void setup() {
		userDao = new JdbcUserAccount(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
	}
	
	
	
	@Test
	public void get_correct_user() {
		UserCredentials testUser = createTestUser("testUser", "password");
		UserAccount result = userDao.getNewUserAccount(testUser);
		Assert.assertEquals(testUser.getUsername(), result.getUsername());
	}
	
	@Test
	public void get_current_balance() {
		userDao.getNewUserAccount(createTestUser("testUser", "password"));
		double result = userDao.getCurrentBalance("testUser");
		Assert.assertEquals(0.00, result, 0.00);
	}
	
	@Test
	public void update_balance() {
		UserAccount testUser = userDao.getNewUserAccount(createTestUser("testUser", "password"));
		testUser.setBalance(100);
		userDao.updateAccountBalance(testUser);
		double result = userDao.getCurrentBalance("testUser");
		
		Assert.assertEquals(100.00, result, 0.00);
	}
	
	@Test
	public void get_user_address() {
		UserAccount testUser = userDao.getNewUserAccount(createTestUser("testUser", "password"));
		insertTestAddress(testUser.getAccountId());
		
		UserAddress result = userDao.getUserAddress(testUser);
		
		Assert.assertEquals("TT", result.getState());
	}
	
	@Test
	public void add_new_user_address() {
		UserAccount testUser = userDao.getNewUserAccount(createTestUser("testUser", "password"));
		UserAddress testAddress = new UserAddress(testUser);
		testAddress.setStreet("123 Test St.");
		testAddress.setCity("TestCity");
		testAddress.setState("TT");
		testAddress.setZipcode(54321);
		
		userDao.addUserAddress(testAddress);
		UserAddress result = userDao.getUserAddress(testUser);
		
		Assert.assertEquals(testAddress.getState(), result.getState());
	}
	
	@Test
	public void update_user_address() {
		
		UserAccount testUser = userDao.getNewUserAccount(createTestUser("testUser", "password"));
		insertTestAddress(testUser.getAccountId());
		UserAddress testNewAddress = new UserAddress(testUser);
		testNewAddress.setStreet("Test Ct.");
		testNewAddress.setCity("TestingNew");
		testNewAddress.setState("ZZ");
		testNewAddress.setZipcode(12345);
		
		userDao.updateUserAddress(testNewAddress);
		UserAddress result = userDao.getUserAddress(testUser);
		
		Assert.assertEquals(testNewAddress.getState(), result.getState());
		
	}
	
	
	
	private UserCredentials createTestUser(String username, String password) {
		
		UserCredentials testUser = new UserCredentials(username, password);
		String sql = "INSERT INTO users (user_id, user_name, password) VALUES (DEFAULT, ?, ?) RETURNING user_id";
		int userId = jdbcTemplate.queryForObject(sql, int.class, username, password);
		
		String sql2 = "INSERT INTO accounts (account_id, user_id, balance) VALUES (DEFAULT, ?, 0.00) RETURNING account_id";
		jdbcTemplate.queryForRowSet(sql2, userId);
		
		return testUser;
	}
	
	private void insertTestAddress(int accountId) {
		String sql = "INSERT INTO user_address (address_id, account_id, street, city, state, zipcode) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING state";
		jdbcTemplate.queryForRowSet(sql, accountId, "123 Test St.", "Testing", "TT", 54321);
	}
	
}
