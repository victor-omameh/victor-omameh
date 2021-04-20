package com.victoromameh;



import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.victoromameh.dao.JdbcUserAccount;
import com.victoromameh.dao.JdbcUserCredentials;
import com.victoromameh.dao.JdbcWine;
import com.victoromameh.dao.UserCredentialsDao;
import com.victoromameh.dao.UserAccountDao;
import com.victoromameh.dao.WineDao;
import com.victoromameh.model.Order;
import com.victoromameh.model.UserAccount;
import com.victoromameh.model.UserAddress;
import com.victoromameh.model.UserCredentials;
import com.victoromameh.model.Wine;
import com.victoromameh.view.ConsoleService;



public class WineBarAppCLI {
	
	private ConsoleService console;
	//WELCOME OPTIONS
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	//MAIN MENU OPTION
	private static final String SELECT_PREFERENCES_OPTION = "Select Preferences";
	private static final String GET_RECOMMENDATION_OPTION = "Get a Recommendation";
	private static final String SHOW_FULL_MENU_OPTION = "Show Full Wine List";
	private static final String SEND_RECOMMENDATION_OPTION = "Send a Recommendation to a Friend";
	private static final String SEND_GIFT_OPTION = "Send a Bottle of Wine as a Gift";
	private static final String VIEW_INBOX_OPTION = "View Inbox";
	private static final String VIEW_HISTORY_OPTION = "View Order History";
	private static final String ACCOUNT_SETTINGS_OPTION = "Account Settings";
	private static final String LOGIN_AS_DIFFERENT_USER_OPTION = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = {SELECT_PREFERENCES_OPTION, GET_RECOMMENDATION_OPTION, SHOW_FULL_MENU_OPTION, SEND_RECOMMENDATION_OPTION, SEND_GIFT_OPTION, VIEW_INBOX_OPTION, VIEW_HISTORY_OPTION, ACCOUNT_SETTINGS_OPTION,LOGIN_AS_DIFFERENT_USER_OPTION, MENU_OPTION_EXIT};
	//ACCOUNT SETTINGS OPTIONS
	private static final String ADD_MONEY_OPTION = "Add Money";
	private static final String ADD_FRIENDS = "Find Friends";
	private static final String EDIT_ADDRESS_OPTION = "Edit Shipping Address";
	private static final String[] ACCOUNT_SETTING_OPTIONS = { ADD_MONEY_OPTION, ADD_FRIENDS, EDIT_ADDRESS_OPTION, MENU_OPTION_EXIT };
	//ADDING MORE MONEY
	private static final String[] ADD_MORE_MONEY_OPTIONS = { ADD_MONEY_OPTION, MENU_OPTION_EXIT };
	//EDIT SHIPPING ADDRESS
	private static final String[] EDIT_ADDRESS_OPTIONS = { EDIT_ADDRESS_OPTION, MENU_OPTION_EXIT };
	//CONFIRM SHIPPING ADDRESS
	private static final String CONFIRM_SHIPPING_ADDRESS = "Confirm Shipping Address";
	private static final String[] CONFIRM_ADDRESS_OPTIONS = {CONFIRM_SHIPPING_ADDRESS, EDIT_ADDRESS_OPTION, MENU_OPTION_EXIT };
	//VIEW INBOX
	private static final String OPEN_RECOMMENDATIONS = "View Recomendations From Friends";
	private static final String OPEN_GIFTS = "View Gifts From Friends";
	private static final String[] VIEW_INBOX_OPTIONS = {OPEN_RECOMMENDATIONS, OPEN_GIFTS, MENU_OPTION_EXIT};
	
	private static DecimalFormat df2 = new DecimalFormat("$##0.00");
	
	
	private UserCredentialsDao userCredentialsDao;
	private UserAccountDao userDao;
	private WineDao wineDao;
	

	private UserAccount currentUser;
	private UserAddress userAddress;
	private Order newOrder;
	

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/wines");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		WineBarAppCLI application = new WineBarAppCLI(dataSource, new ConsoleService(System.in, System.out));
		application.run();
	}

	public WineBarAppCLI(DataSource datasource, ConsoleService console) {
		this.console = console;
		
		userCredentialsDao = new JdbcUserCredentials(datasource);
		userDao = new JdbcUserAccount(datasource);
		wineDao = new JdbcWine(datasource);
	}

	public void run() {
		
		console.prompt("\n*************************** \n"
				+ "Welcome to the WINE BAR APP \n"
				+ "***************************");
		console.prompt("\nFinding the perefect wine can be tough! \n"
				+ "Let us help you curate a wine that is BEST FOR YOU! \n");
		
		registerAndLogin();
		mainMenu();
		
	}
	
	private void mainMenu() {
		
		boolean selectingFromMainMenu = true;
		while (selectingFromMainMenu) {
			
			int userChoice = console.getMenuChoice(MAIN_MENU_OPTIONS);
			if (userChoice == 1) {
			//SELECT_PREFERENCES_OPTION
				selectUserPreferences();
				
			} else if(userChoice == 2) {
			//GET_RECOMMENDATION_OPTION
				makeRecommendationFromUserFavorites();
				
			} else if(userChoice == 3) {
			//SHOW FULL WINE LIST
				showWineList(wineDao.getFullWineList(), "Full Wine List");
				
			} else if(userChoice == 4) {
			//SEND_RECOMMENDATION_OPTION
				sendRecommendation();
				
			} else if(userChoice == 5) {
			//SEND_GIFT_OPTION	
				sendGift();
				
			} else if(userChoice == 6) {
			//VIEW_INBOX_OPTION	
				viewInbox();
				
			} else if(userChoice == 7) {
			//VIEW_HISTORY_OPTION
				viewOrderHistory();
				
			} else if(userChoice == 8) {
			//ACCOUNT_SETTINGS_OPTION
				openAccountSettings();
			} else if(userChoice == 9) {
				registerAndLogin();	 
				
			} else if(userChoice == 10) {
			//MENU_OPTION_EXIT
				exitApp();
				
			}
		}
	}
	
/*
 * 
 * 
 * SELECT PREFERENCES (1)
 * 
 * 
 */
	
	private void selectUserPreferences() {
		
		setupScoringStart();
		
		scoreCategory();
		scoreVarietal();
		scoreTannins();
		scoreBody();
		scoreAcidity();
		scoreRegion();
		
		showWineList(listOfWinningWines(), "Your Curated Wine(s) ");
		
		
	}
	
	private List<Wine> listOfWinningWines() {
		int topScore = 0;
		List<Wine> topPicks = new ArrayList<Wine>();
		
		for (Map.Entry<Wine, Integer> entry : currentUser.getWineScores().entrySet()) {
			if  (entry.getValue() > topScore) {
				topScore = entry.getValue();
			}
		}
		
		for (Map.Entry<Wine, Integer> entry : currentUser.getWineScores().entrySet()) {
			if  (entry.getValue() == topScore) {
				topPicks.add(entry.getKey());
			}
		}
		return topPicks;
	}
	
	private void scoreCategory() {
		
		String userPreference = null;
		int userInput = 0;
		String[] categoryOptions = {"Sparkling", "White", "Rose", "Red", "[Skip/Next]"};
		
		boolean scoringCategory = true;
		while(scoringCategory) {
			console.prompt("\nWhich type of wines do you prefer? (enter one at a time)");
			userInput = console.getMenuChoice(categoryOptions);
		
			if (userInput == 5) {
				return;
			} else if (userInput == 1) {
				userPreference = "Sparkling";
			} else if (userInput == 2) {
				userPreference = "White";
			} else if (userInput == 3) {
				userPreference = "Rose";
			} else if (userInput == 4) {
				userPreference = "Red";
			} 
		
			Map<Wine, Integer> scoreCard = currentUser.getWineScores();
			for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
				
				if (entry.getKey().getCategory().equalsIgnoreCase(userPreference)) {
					entry.setValue(entry.getValue() + 1);
				}
			}
		}
	}
	
	private void scoreVarietal() {
		
		String userPreference = null;
		int userInput = 0;
		String[] varietalOptions = {"Riesling", "Pinot Grigio", "Sauvignon Blanc", "Chardonnay", "Pinot Noir", "Merlot", "Malbec", "Cabernet Sauvignon", "Syrah", "Sangiovese", "Grenanche", "Tempranillo", "White Blend", "Red Blend", "[Skip/Next]"};
		
		boolean scoringVarietal = true;
		while(scoringVarietal) {
			console.prompt("\nSelect your preferred varietals (enter one at a time)");
			userInput = console.getMenuChoice(varietalOptions); 
		
			if (userInput == 15) {
				return;
			} else if (userInput == 1) {
				userPreference = "Riesling";
			} else if (userInput == 2) {
				userPreference = "Pinot Grigio";
			} else if (userInput == 3) {
				userPreference = "Sauvignon Blanc";
			} else if (userInput == 4) {
				userPreference = "Chardonnay";
			} else if (userInput == 5) {
				userPreference = "Pinot Noir";
			} else if (userInput == 6) {
				userPreference = "Merlot";
			} else if (userInput == 7) {
				userPreference = "Malbec";
			} else if (userInput == 8) {
				userPreference = "Cabernet Sauvignon";
			} else if (userInput == 9) {
				userPreference = "Syrah";
			} else if (userInput == 10) {
				userPreference = "Sangiovese";
			} else if (userInput == 11) {
				userPreference = "Grenanche";
			} else if (userInput == 12) {
				userPreference = "Tempranillo";
			} else if (userInput == 13) {
				userPreference = "White Blend";
			} else if (userInput == 14) {
				userPreference = "Red Blend";
			}   
			
			Map<Wine, Integer> scoreCard = currentUser.getWineScores();
			for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
				
				if (entry.getKey().getVarietal().equalsIgnoreCase(userPreference)) {
					entry.setValue(entry.getValue() + 1);
				}
			}
		}
	}
	
	private void scoreAcidity() {
		
		String userPreference = null;
		int userInput = 0;
		console.prompt("\nWhat is your preferred level of acidity? \n"
				+ "|--1  2  3  4  5  6  7  8  9  10--| \n"
				+ "Less Lively             Very Lively");
		
		boolean scoringAcidity = true;
		while(scoringAcidity) {
			userInput = console.getUserInputInteger("\nEnter value (1-10) or (0) to skip");
			if (userInput >= 0 && userInput <= 10) {
				scoringAcidity = false;
			} else {
				console.errorMessage("*** Invalid ***");
			}
		}
		if (userInput == 0) {
			return;
		} else if (userInput <= 3) {
			userPreference = "Less Lively";
		} else if (userInput <= 7) {
			userPreference = "Lively";
		} else if (userInput <= 10) {
			userPreference = "Very Lively";
		} 
		Map<Wine, Integer> scoreCard = currentUser.getWineScores();
		for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
			
			if (entry.getKey().getAcidity().equalsIgnoreCase(userPreference)) {
				entry.setValue(entry.getValue() + 1);
			}
		}
	}
	
	private void scoreBody() {
		
		String userPreference = null;
		int userInput = 0;
		console.prompt("\nWhat is your preferred body level? \n"
				+ "|--1  2  3  4  5  6  7  8  9  10--| \n"
				+ "Light Body                Full Body");
		
		boolean scoringBody = true;
		while(scoringBody) {
			userInput = console.getUserInputInteger("\nEnter value (1-10) or (0) to skip");
			if (userInput >= 0 && userInput <= 10) {
				scoringBody = false;
			} else {
				console.errorMessage("*** Invalid ***");
			}
		}
		
		if (userInput == 0) {
			return;
		} else if (userInput <= 3) {
			userPreference = "Light Body";
		} else if (userInput <= 7) {
			userPreference = "Medium Body";
		} else if (userInput <= 10) {
			userPreference = "Full Body";
		} 
		
		Map<Wine, Integer> scoreCard = currentUser.getWineScores();
		for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
			
			if (entry.getKey().getBody().equalsIgnoreCase(userPreference)) {
				entry.setValue(entry.getValue() + 1);
			}
		}
	}
	
	private void scoreTannins() {
		String userPreference = null;
		int userInput = 0;
		console.prompt("How dry/sweet do you like your wine? \n"
				+ "|--1  2  3  4  5  6  7  8  9  10--| \n"
				+ "Very Sweet                 Very Dry");
		
		boolean scoringTannins = true;
		while(scoringTannins) {
			userInput = console.getUserInputInteger("\nEnter value (1-10) or (0) to skip");
			if (userInput >= 0 && userInput <= 10) {
				scoringTannins = false;
			} else {
				console.errorMessage("*** Invalid ***");
			}
		}
		
		if (userInput == 0) {
			return;
		} else if (userInput <= 2) {
			userPreference = "Very Sweet";
		} else if (userInput <= 4) {
			userPreference = "Sweet";
		} else if (userInput <= 6) {
			userPreference = "Off Dry";
		} else if (userInput <= 8) {
			userPreference = "Dry";
		} else if (userInput <= 10) {
			userPreference = "Very Dry";
		}
		
		
		Map<Wine, Integer> scoreCard = currentUser.getWineScores();
		for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
			
			if (entry.getKey().getTannins().equalsIgnoreCase(userPreference)) {
				entry.setValue(entry.getValue() + 1);
			}
		}
	}
	
	private void scoreRegion() {
		
		String userPreference = null;
		int userInput = 0;
		String[] regionOptions = {"France", "Italy", "Germany", "Austria", "Spain", "California", "Oregon", "New Zealand", "Australia", "Argentina", "[Skip/Next]"};
		
		boolean scoringRegion = true;
		while(scoringRegion) {
			console.prompt("\nSelect any regions you may prefer (enter one at a time)");
			userInput = console.getMenuChoice(regionOptions);
		
			if (userInput == 11) {
				return;
			} else if (userInput == 1) {
				userPreference = "France";
			} else if (userInput == 2) {
				userPreference = "Italy";
			} else if (userInput == 3) {
				userPreference = "Germany";
			} else if (userInput == 4) {
				userPreference = "Austria";
			} else if (userInput == 5) {
				userPreference = "Spain";
			} else if (userInput == 6) {
				userPreference = "California";
			} else if (userInput == 7) {
				userPreference = "Oregon";
			} else if (userInput == 8) {
				userPreference = "New Zealand";
			} else if (userInput == 9) {
				userPreference = "Australia";
			} else if (userInput == 10) {
				userPreference = "Argentina";
			}    
			
			Map<Wine, Integer> scoreCard = currentUser.getWineScores();
			for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
				
				if (entry.getKey().getRegion().equalsIgnoreCase(userPreference)) {
					entry.setValue(entry.getValue() + 1);
				}
			}
		}	
	}
	
	private void setupScoringStart() {
		Map<Wine, Integer> scoreCard = new HashMap<Wine, Integer>();
		for (Wine wine : wineDao.getFullWineList()) {
			scoreCard.put(wine, 0);
		}
		currentUser.setWineScores(scoreCard);
		
	}
	
	
/*
 * 
 * 
 * GET RECOMMENDATION (2)
 * 
 * 
 */
	
	private void makeRecommendationFromUserFavorites() {
		
		List<Wine> userFavs = new ArrayList<Wine>();
		
		for(Wine wine : wineDao.getOrderHistory(currentUser.getUsername())) {
			if (wine.isFavorite()) {
				userFavs.add(wine);
			}
		}
		
		setupScoringStart();
		
		Map<Wine, Integer> scoreCard = currentUser.getWineScores();
		for (Wine favWine : userFavs) {
			
			for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
				
				if (entry.getKey().getCategory().equals(favWine.getCategory())) {
					entry.setValue(entry.getValue() + 1);
				}
				if (entry.getKey().getVarietal().equals(favWine.getVarietal())) {
					entry.setValue(entry.getValue() + 1);
				}
				if (entry.getKey().getAcidity().equals(favWine.getAcidity())) {
					entry.setValue(entry.getValue() + 1);
				}
				if (entry.getKey().getBody().equals(favWine.getBody())) {
					entry.setValue(entry.getValue() + 1);
				}
				if (entry.getKey().getTannins().equals(favWine.getTannins())) {
					entry.setValue(entry.getValue() + 1);
				}
				if (entry.getKey().getRegion().equals(favWine.getRegion())) {
					entry.setValue(entry.getValue() + 1);
				}
			}
		}
		for (Wine orderedWine : wineDao.getOrderHistory(currentUser.getUsername())) {
			for (Map.Entry<Wine, Integer> entry : scoreCard.entrySet()) {
				if (entry.getKey().getName().equals(orderedWine.getName())) {
					entry.setValue(0);
				}
			}
		}
		showWineList(listOfWinningWines(), "Our recommendation based on your selected favorites!\n");
	}
	
	
/*
 * 
 * 
 * SHOW FULL WINE LIST (3)
 * 
 * 
 */
	
	private void showWineList(List<Wine> wineList, String listType) {
		
		boolean lookingAtFullMenu = true;
		while(lookingAtFullMenu) {
			console.displayWineList(wineList, listType);
			int userInput = getInputFromWineList("Enter the Wine ID to see details/order or (0) to Cancel");
			if (userInput == 0) {
				lookingAtFullMenu = false;
			} else {
				Wine selectedWine = searchWineById(userInput);
				console.displayWineDetails(selectedWine);
				userInput = console.getUserInputInteger("Enter (1) to order " + selectedWine.getName() + ", " + selectedWine.getVarietal() + " or Enter (0) to cancel");
				if (userInput == 1) {
					orderSelectedWine(selectedWine, currentUser.getUsername(), currentUser.getUsername(), false, null);
					lookingAtFullMenu = false;
				} 
			}	
		}
	}
	
	
	private Wine searchWineById(int wineId) {
		Wine selectedWine = new Wine();
		for (Wine wine : wineDao.getFullWineList()) {
			if (wine.getWineId() == wineId) {
				selectedWine = wine;
				break;
			}
		}
		return selectedWine;
	}
		
	
	private int getInputFromWineList(String prompt) {
		int userInput = 0;
		boolean gettingInput = true;
		while(gettingInput) {
			userInput = console.getUserInputInteger(prompt);
			if (verifyInputMatchesWineList(userInput) || userInput == 0) {
				gettingInput = false;
			} else {
				console.prompt(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		}
		return userInput;
	}
/*
 * 
 * 
 * SEND RECOMMENDATION (4)
 * 
 * 
 */	
	
	private void sendRecommendation() {
		String friendsUsername = null;
		console.displayFriendsList(userDao.getListOfFriends(currentUser.getUsername()));
		
		boolean selectingFriend = true;
		while(selectingFriend) {
			int userInput = console.getUserInputInteger("Enter Friend's ID ( or '0' to Cancel)");
			if (userInput == 0) {
				return;
			}
			if (verifyFriendsAccountID(userInput)) {
				friendsUsername = friendsUsername(userInput);
				
				boolean lookingAtFullMenu = true;
				while(lookingAtFullMenu) {
					console.displayWineList(wineDao.getFullWineList(), "WINE LIST");
					userInput = getInputFromWineList("Enter the Wine ID to see details/Send Friend Recommendation or (0) to Cancel");
					if (userInput == 0) {
						lookingAtFullMenu = false;
					} else {
						Wine selectedWine = searchWineById(userInput);
						console.displayWineDetails(selectedWine);
						userInput = console.getUserInputInteger("Enter (1) to send recommendation " + selectedWine.getName() + ", " + selectedWine.getVarietal() + " or Enter (0) to cancel");
						if (userInput == 1) {
							
							String message = console.getUserInput("Enter a message");
							if (message.equals(null) || message.equals("") || message.equals(" ")) {
								message = "I think you will love this wine!";
							}
							wineDao.sendFriendRecommendation(currentUser.getUsername(), friendsUsername, selectedWine, message);
							console.prompt("\n[Recommendation Sent]");
							
							lookingAtFullMenu = false;
						} 
					}	
				}
				
				selectingFriend = false;
			} else {
				console.errorMessage("*** Invalid Account ID ***");
			}
		}
		
	}
	
	private boolean verifyFriendsAccountID(int friendsID) {
		
		boolean idFound = false;
		
		for (UserAccount friend : userDao.getListOfFriends(currentUser.getUsername())) {
			if (friend.getAccountId() == friendsID) {
				idFound = true;
				break;
			}
		}
		
		return idFound;
		
	}
	
	private String friendsUsername(int friendsID) {
		String username = null;
		
		for (UserAccount friend : userDao.getListOfFriends(currentUser.getUsername())) {
			if (friend.getAccountId() == friendsID) {
				username = friend.getUsername();
				break;
			}
		}
		
		return username;
	}
/*
 * 
 * 
 * SEND GIFT (5)
 * 
 * 
 */
	
	private void sendGift() {
		
		String friendsUsername = null;
		console.displayFriendsList(userDao.getListOfFriends(currentUser.getUsername()));
		
		boolean selectingFriend = true;
		while(selectingFriend) {
			int userInput = console.getUserInputInteger("Enter Friend's ID ( or '0' to Cancel)");
			if (userInput == 0) {
				return;
			}
			if (verifyFriendsAccountID(userInput)) {
				friendsUsername = friendsUsername(userInput);
				
				boolean lookingAtFullMenu = true;
				while(lookingAtFullMenu) {
					console.displayWineList(wineDao.getFullWineList(), "WINE LIST");
					userInput = getInputFromWineList("Enter the Wine ID to see details/Send Gift or (0) to Cancel");
					if (userInput == 0) {
						lookingAtFullMenu = false;
					} else {
						Wine selectedWine = searchWineById(userInput);
						console.displayWineDetails(selectedWine);
						userInput = console.getUserInputInteger("Enter (1) to send a bottle " + selectedWine.getName() + ", " + selectedWine.getVarietal() + " or Enter (0) to cancel");
						if (userInput == 1) {
							
							String message = console.getUserInput("Enter a message");
							if (message.equals(null) || message.equals("") || message.equals(" ")) {
								message = "I think you will love this wine!";
							}
			
							orderSelectedWine(selectedWine, currentUser.getUsername(), friendsUsername, true, message);
							
							lookingAtFullMenu = false;
						} 
					}	
				}
				
				selectingFriend = false;
			} else {
				console.errorMessage("*** Invalid Account ID ***");
			}
		}
		
	}
	
/*
 * 
 * 
 * VIEW INBOX (6)
 * 
 * 
 */
	
	private void viewInbox() {
		
		boolean viewingInbox = true;
		while(viewingInbox) {
			
			console.prompt("--------\nInbox\n--------");
			int userInput = console.getMenuChoice(VIEW_INBOX_OPTIONS);
			
			if (userInput == 1) {
				boolean lookingAtRecommendations = true;
				while(lookingAtRecommendations) {
					console.displayRecomendations(userDao.getListOfRecommendations(currentUser.getUsername()));
					
					userInput = getInputFromWineList("\nEnter the Wine ID to see details/order or (0) to Cancel");
					if (userInput == 0) {
						lookingAtRecommendations = false;
					} else {
						Wine selectedWine = searchWineById(userInput);
						console.displayWineDetails(selectedWine);
						userInput = console.getUserInputInteger("Enter (1) to order " + selectedWine.getName() + ", " + selectedWine.getVarietal() + " or Enter (0) to cancel");
						if (userInput == 1) {
							orderSelectedWine(selectedWine, currentUser.getUsername(), currentUser.getUsername(), false, null);
							lookingAtRecommendations = false;
						} 
					}	
				}

			} else if (userInput == 2) {
				
				boolean lookingAtGifts = true;
				while (lookingAtGifts) {
					
					console.displayGifts(wineDao.getListOfGifts(currentUser.getUsername()));
					lookingAtGifts = false;
					
				}
				
			} else if (userInput == 3) {
				return;
			}
		}
	}
	
	
/*
 * 
 * 
 * VIEW ORDER HISTORY (7)
 * 
 * 
 */
	
	private void viewOrderHistory() {
		
		boolean lookingAtOrderHistory = true;
		while(lookingAtOrderHistory) {
			console.displayWineList(wineDao.getOrderHistory(currentUser.getUsername()), "ORDER HISTORY");
			int userInput = getInputFromOrderHistory();
			if (userInput == 0) {
				lookingAtOrderHistory = false;
			} else {
				Wine selectedWine = searchWineByIdFromOrderHistory(userInput);
				console.displayWineDetails(selectedWine);
				userInput = console.getUserInputInteger("Enter (1) to order " + selectedWine.getName() + ", " + selectedWine.getVarietal() 
						+ "\nEnter (2) to Add or Remove as Favorite" 
						+ "\nEnter (0) to cancel");
				
				if (userInput == 1) {
					orderSelectedWine(selectedWine, currentUser.getUsername(), currentUser.getUsername(), false, null);
					lookingAtOrderHistory = false;
					
				} else if (userInput == 2) {
					if (selectedWine.isFavorite()) {
						selectedWine.setFavorite(false);
						wineDao.addOrRemoveFavorite(selectedWine, currentUser.getUsername());
					} else {
						selectedWine.setFavorite(true);
						wineDao.addOrRemoveFavorite(selectedWine, currentUser.getUsername());
					}	
				}
			}	
		}
	}
	
	private int getInputFromOrderHistory() {
		
		int userInput = 0;
		boolean gettingInput = true;
		while(gettingInput) {
			userInput = console.getUserInputInteger("Enter the Wine ID to add/remove wine as a favorite or order again [(0) to Cancel]");
			if (verifyInputMatchesWineList(userInput) || userInput == 0) {
				gettingInput = false;
			} else {
				console.prompt(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		}
		return userInput;
	}
	
	private Wine searchWineByIdFromOrderHistory(int wineId) {
		Wine selectedWine = new Wine();
		for (Wine wine : wineDao.getOrderHistory(currentUser.getUsername())) {
			if (wine.getWineId() == wineId) {
				selectedWine = wine;
				break;
			}
		}
		return selectedWine;
	}
	
	
/*
 * 
 * 
 * ACCOUNT SETTINGS (8))
 * 
 * 
 */
	
	
	private void openAccountSettings(){
		boolean editingAccountSettings = true;
		while(editingAccountSettings) {
			
			console.prompt("\n---------------- \n"
				     + "Account Settings \n"
				     + "----------------");
			int userInput = console.getMenuChoice(ACCOUNT_SETTING_OPTIONS);
			if (userInput == 1) {
				//add money
				getAmountToAdd();
			} else if (userInput == 2) {
				//find friends
				addFriend();
			
			} else if (userInput == 3) {
				//edit address
				updateAddress();
			
			} else {
				return;
			}
		}
	}
	
	private void getAmountToAdd() {
		boolean addingMoney = true;
		while (addingMoney) {
			
			boolean gettingAmountToAdd = true;
			int amountToAdd = 0;
			while(gettingAmountToAdd) {
				
				console.prompt("Current Balance: " + df2.format(userDao.getCurrentBalance(currentUser.getUsername())));
				amountToAdd = console.getUserInputInteger("Enter amount you would like to add or (0) to Cancel");
				if (amountToAdd < 0) {
					console.errorMessage("[Amount Denied]");
				} else if (amountToAdd == 0){
					addingMoney = false;
					gettingAmountToAdd = false;
				} else {
					gettingAmountToAdd = false;
				}
			}
			if (amountToAdd > 0) {
				addingMoney = addMoneyToAccount(amountToAdd);
			}
		}
		
	}
	
	private boolean addMoneyToAccount(int amountToAdd) {
		double newBalance = userDao.getCurrentBalance(currentUser.getUsername()) + amountToAdd;
		currentUser.setBalance(newBalance);
		userDao.updateAccountBalance(currentUser);
		
		console.prompt("\n[Account Updated] \n"
				+ "You have added " + df2.format(amountToAdd) + " to your account!" );
		console.prompt("\nCurrent Balance: " + df2.format(userDao.getCurrentBalance(currentUser.getUsername())));
		int userInput = console.getMenuChoice(ADD_MORE_MONEY_OPTIONS);
		if (userInput == 1) {
			return true;
		}
		return false;
	}
	
	private void addFriend() {
		console.prompt("-----------\nADD FRIENDS\n-----------\n");
		
		boolean findingFriend = true;
		while(findingFriend) {
			String friend = console.getUserInput("Enter friend's username (0 to Cancel)");
			
			if (friend.equals("0")) {
				return;
			}
			for (String user : userDao.getListOfUserNames()) {
				if (user.equalsIgnoreCase(friend)) {
					userDao.addFriend(currentUser.getUsername(), friend);
					userDao.addFriend(friend, currentUser.getUsername());
					console.prompt("\n[" + friend + " has been added to your Friends List!]");
					findingFriend = false;
				}
			}
			if (findingFriend) {
				console.errorMessage("Friend not Found");
			}
		}
	}
	
	private void updateAddress() {
		
		console.prompt("----------------------- \n"
			     + "Update Shipping Address \n"
			     + "-----------------------");
		
		boolean updatingAddress = true;
		while (updatingAddress) {
			if (userAddress.getState() == null) {
				addNewAddress();
			} else {
				int userInput = promptUserToChangeAddress();
				if (userInput == 1) {
					updateUserAddress();
				} else {
					updatingAddress = false;
				}
			}	
		}
	}
	
	private void addNewAddress() {
		userAddress.setStreet(console.getUserInput("Enter Street Address (including Apt or Suite #)"));
		userAddress.setCity(console.getUserInput("Enter City"));
		userAddress.setState(console.getUserInput("Enter State (i.e. 'OH')"));
		userAddress.setZipcode(console.getUserInputInteger("Enter Zipcode"));
		userDao.addUserAddress(userAddress);
		console.prompt("\n[Address Updated]\n");
		console.prompt("Limited Time Free Shipping On All Orders!\n");
	}
	
	private int promptUserToChangeAddress() {
		console.prompt("Your current shipping address is " + userAddress.getStreet() + " " + userAddress.getCity() + ", " + userAddress.getState() + " " + userAddress.getZipcode());
		int userInput = console.getMenuChoice(EDIT_ADDRESS_OPTIONS);

		return userInput;
		
	}
	
	private void updateUserAddress() {
		
		userAddress.setStreet(console.getUserInput("Enter Street Address (including Apt or Suite #)"));
		userAddress.setCity(console.getUserInput("Enter City"));
		userAddress.setState(console.getUserInput("Enter State (i.e. 'OH')"));
		userAddress.setZipcode(console.getUserInputInteger("Enter Zipcode"));
		userDao.updateUserAddress(userAddress);
		console.prompt("\n[Address Updated]\n");
		console.prompt("Limited Time Free Shipping On All Orders!\n");
	
	}
	
	
	
/*
 * 
 * REGISTER AND LOGIN
 * 
 */
	
	private void registerAndLogin() {
		boolean registeringOrLoggingIn = true;
		while(registeringOrLoggingIn) {
			int userChoice = console.getMenuChoice(LOGIN_MENU_OPTIONS);
			if (userChoice == 1) {
				register();
			} else if (userChoice == 2) {
				login();
				registeringOrLoggingIn = false;
			} else if (userChoice == 3){
				exitApp();
			}
		}
		
	}
	
	private void register() {
		boolean userAlreadyExists = true;
		UserCredentials newUser = null;
		while(userAlreadyExists) {
			newUser = getUserCredentials();
			userAlreadyExists = userCredentialsDao.verifyIfUserAlreadyExists(newUser);
			if(userAlreadyExists) {
				console.errorMessage("\n*** Username Already Exists ***\n");
			}
		}
		
		userCredentialsDao.registerNewUser(newUser);
		console.prompt("\n[Registeration Successful]");
		
	}
	
	private void login() {
		boolean loggingIn = true;
		UserCredentials user = null;
		while (loggingIn) {
			user = getUserCredentials();
			if(userCredentialsDao.verifyLoginInput(user)) {
				console.prompt("\n [Login Successful] Welcome " + user.getUsername() + "!");
				currentUser = userDao.getNewUserAccount(user);
				userAddress = userDao.getUserAddress(currentUser);
				loggingIn = false;
			} else {
				console.errorMessage("\n*** Incorrect Username or Password ***\n");
			}
		}
	}
	
	private void exitApp() {
		console.prompt("Goodbye!");
		System.exit(0);
	}
	
	private UserCredentials getUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		
		return new UserCredentials(username, password);
	}
	
/*
 * 
 * ADDITIONAL METHODS
 * 
 */
	
	private boolean verifyInputMatchesWineList(int userInput) {
		for(Wine wine : wineDao.getFullWineList()) {
			if (wine.getWineId() == userInput) {
				return true;
			}
		}
		return false;
	}
	
	
	private void orderSelectedWine(Wine selectedWine, String userFrom, String userTo, boolean isGift, String message) {
		newOrder = new Order();
		boolean orderingWine = true;
		while (orderingWine) {
			if (verifySufficientFunds(selectedWine.getPrice())) {
				if (!(userDao.getUserAddress(currentUser).getState() == null)) {
					
					if(!isGift) {
						int userInput = confirmShippingAddress();
						
						if (userInput == 1) {
							//confirmed
							currentUser.setBalance(userDao.getCurrentBalance(currentUser.getUsername()) - selectedWine.getPrice());
						
							newOrder.setOrderDate(LocalDate.now());
							newOrder.setOrderedWine(selectedWine);
							newOrder.setUserFrom(userFrom);
							newOrder.setUserTo(userTo);
							newOrder.setFavorite(false);
							newOrder.setGift(isGift);
							newOrder.setMessage(message);
							
							wineDao.orderWine(newOrder);
							userDao.updateAccountBalance(currentUser);
							
							console.prompt("[Wine Ordered]");
							console.prompt("\nIf you enjoy the wine, make sure to mark it as a favortie so we can send you personal recommendations!" );
							//wine ordered
							orderingWine = false;
						} else if (userInput == 2) {
							//change address
							updateUserAddress();
						} else {
							//exit
							orderingWine = false;
						}
					} else {
						currentUser.setBalance(userDao.getCurrentBalance(currentUser.getUsername()) - selectedWine.getPrice());
						
						newOrder.setOrderDate(LocalDate.now());
						newOrder.setOrderedWine(selectedWine);
						newOrder.setUserFrom(userFrom);
						newOrder.setUserTo(userTo);
						newOrder.setFavorite(false);
						newOrder.setGift(isGift);
						newOrder.setMessage(message);
						
						wineDao.orderWine(newOrder);
						userDao.updateAccountBalance(currentUser);
						
						console.prompt("\n[Gift Sent!]");
						//wine ordered
						orderingWine = false;
					}
					
					
					
				} else {
					console.prompt("[Add Shipping Address]");
					addNewAddress();
				}
			} else {
				int userInput = console.getMenuChoice(ADD_MORE_MONEY_OPTIONS);
				if (userInput == 1) {
					//add money
					getAmountToAdd();
				} else {
					//exit
					orderingWine = false;
				}
			}
		}
	}
	
	private boolean verifySufficientFunds(double priceOfWine) {
		boolean sufficientFunds = false;
		if (userDao.getCurrentBalance(currentUser.getUsername()) >= priceOfWine) {
			sufficientFunds = true;
		}
		return sufficientFunds;
	}
	
	private int confirmShippingAddress() {
	
		console.prompt("\nYour current shipping address is " + userAddress.getStreet() + " " + userAddress.getCity() + ", " + userAddress.getState() + " " + userAddress.getZipcode());
		int userInput = console.getMenuChoice(CONFIRM_ADDRESS_OPTIONS);
	
		return userInput;
	}
	
	
	
}
