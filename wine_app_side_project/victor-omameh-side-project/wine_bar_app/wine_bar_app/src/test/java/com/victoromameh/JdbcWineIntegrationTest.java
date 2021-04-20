package com.victoromameh;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.victoromameh.dao.JdbcWine;
import com.victoromameh.dao.WineDao;
import com.victoromameh.model.Order;
import com.victoromameh.model.Wine;

import org.junit.Assert;

public class JdbcWineIntegrationTest extends DaoIntegrationTest{

	private WineDao wineDao;
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
		wineDao = new JdbcWine(getDataSource());
		jdbcTemplate = new JdbcTemplate(getDataSource());
	}
	
	@Test
	public void get_list_of_wines() {
		List<Wine> test = wineDao.getFullWineList();
		int testSize = test.size();
		insertWine();
		
		List<Wine> result = wineDao.getFullWineList();
		int resultSize = result.size();
		
		Assert.assertEquals(testSize + 1, resultSize);
	}
	
//	@Test
//	public void get_order_list() {
//		
//		int testAccountId = createTestUser("testName", "test");
//		int testWineId = insertWine();
//		String sql = "INSERT INTO user_history (order_number, account_id, wine_id, favorite) VALUES (DEFAULT, ? , ? , false) RETURNING order_number";
//		jdbcTemplate.queryForRowSet(sql, testAccountId, testWineId);
//		
//		List<Wine> testWines = wineDao.getOrderHistory("testName");
//		
//		int resultSize = testWines.size();
//		
//		Assert.assertEquals(1, resultSize);
//		
//	}
	
	@Test
	public void order_wine() {
	
		createTestUser("testUser", "test");
		int wineId = insertWine();
		Order testOrder = createTestOrder("testUser", wineId);
		
		wineDao.orderWine(testOrder);
		
		int resultSize = wineDao.getOrderHistory("testUser").size();
		
		Assert.assertEquals(1, resultSize);
	}
	
	@Test
	public void add_favorite() {
		Wine testSelectedWine = new Wine();
		createTestUser("testUser", "test");
		int wineId = insertWine();
		testSelectedWine.setWineId(wineId);
		testSelectedWine.setFavorite(true);
		
		Order testOrder = createTestOrder("testUser", wineId);
		wineDao.orderWine(testOrder);
		
		wineDao.addOrRemoveFavorite(testSelectedWine, "testUser");
		
		List<Wine> resultList = wineDao.getOrderHistory("testUser");
		
		boolean resultFavorite = false;
		for (Wine wine : resultList) {
			if (wine.getWineId() == testSelectedWine.getWineId()) {
				resultFavorite = wine.isFavorite();
			}
		}
		
		Assert.assertTrue("Should be True", resultFavorite);
		
	}
	
	@Test
	public void remove_favorite() {
		Wine testSelectedWine = new Wine();
		createTestUser("testUser", "test");
		int wineId = insertWine();
		testSelectedWine.setWineId(wineId);
		testSelectedWine.setFavorite(true);
		Order testOrder = createTestOrder("testUser", wineId);
		wineDao.orderWine(testOrder);
		wineDao.addOrRemoveFavorite(testSelectedWine, "testUser");
		
		testSelectedWine.setFavorite(false);
		wineDao.addOrRemoveFavorite(testSelectedWine, "testUser");
		
		List<Wine> resultList = wineDao.getOrderHistory("testUser");
		
		boolean resultFavorite = true;
		for (Wine wine : resultList) {
			if (wine.getWineId() == testSelectedWine.getWineId()) {
				resultFavorite = wine.isFavorite();
			}
		}
		
		Assert.assertFalse("Should be False", resultFavorite);
	}
	
	
	
	private int insertWine() {
		
		String sql = "INSERT INTO wines (wine_id, name, vintage, price, type_id, varietal_id, tannin_id, body_id, acidity_id, region_id) " + 
				"VALUES (DEFAULT, 'test', 1991, 50.00, 1, 1, 1, 1, 1, 1) RETURNING wine_id";
		int wineId = jdbcTemplate.queryForObject(sql, int.class);
		
		return wineId;
	}
	
	private int createTestUser(String username, String password) {
		
		String sql = "INSERT INTO users (user_id, user_name, password) VALUES (DEFAULT, ?, ?) RETURNING user_id";
		int userId = jdbcTemplate.queryForObject(sql, int.class, username, password);
		
		String sql2 = "INSERT INTO accounts (account_id, user_id, balance) VALUES (DEFAULT, ?, 0.00) RETURNING account_id";
		int testAccountId = jdbcTemplate.queryForObject(sql2, int.class, userId);
		
		return testAccountId;
	}
	
	private Order createTestOrder(String username, int wineId) {
		
		Wine testWine = new Wine();
		testWine.setWineId(wineId);
		
		Order testOrder = new Order();
		testOrder.setOrderDate(LocalDate.now());
		testOrder.setOrderedWine(testWine);
		testOrder.setUserFrom(username);
		testOrder.setUserTo(username);
		testOrder.setFavorite(false);
		testOrder.setGift(false);
		testOrder.setMessage(null);
		
		return testOrder;
	}
}
