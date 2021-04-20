package com.victoromameh;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.victoromameh.dao.JdbcUserCredentials;
import com.victoromameh.dao.UserCredentialsDao;
import com.victoromameh.model.UserCredentials;

public class JdbcUserCredentialsIntegrationTest extends DaoIntegrationTest{

	private UserCredentialsDao userCredentialsDao;
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
		userCredentialsDao = new JdbcUserCredentials(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
	}
	
	
	@Test
	public void verify_user_exists() {
		UserCredentials testUser = createTestUser("TestUser", "password");
		Assert.assertTrue(userCredentialsDao.verifyIfUserAlreadyExists(testUser));
	}
	
	@Test
	public void verify_user_does_not_exist() {
		UserCredentials testUser = new UserCredentials("TestUser", "password");
		Assert.assertFalse(userCredentialsDao.verifyIfUserAlreadyExists(testUser));
	}
	
	@Test
	public void register_new_user() {
		UserCredentials testUser = new UserCredentials("TestUser", "password");
		userCredentialsDao.registerNewUser(testUser);
		Assert.assertTrue(userCredentialsDao.verifyIfUserAlreadyExists(testUser));
	}
	
	
	@Test
	public void login_correct_credentials() {
		UserCredentials testUser = createTestUser("TestUser", "password");
		Assert.assertTrue(userCredentialsDao.verifyLoginInput(testUser));
	}
	
	@Test
	public void login_wrong_username() {
		createTestUser("TestUser", "password");
		UserCredentials testUser = new UserCredentials("WrongUserName", "password");
		Assert.assertFalse(userCredentialsDao.verifyLoginInput(testUser));
	}
	
	@Test
	public void login_wrong_password() {
		createTestUser("TestUser", "password");
		UserCredentials testUser = new UserCredentials("TestUser", "wrongPassword");
		Assert.assertFalse(userCredentialsDao.verifyLoginInput(testUser));
	}
	
	
	
	
	private UserCredentials createTestUser(String username, String password) {
		
		UserCredentials testUser = new UserCredentials(username, password);
		String sql = "INSERT INTO users (user_id, user_name, password) VALUES (default, ?, ?) RETURNING user_name";
		jdbcTemplate.queryForRowSet(sql, username, password);
		
		return testUser;
	}
	
}
