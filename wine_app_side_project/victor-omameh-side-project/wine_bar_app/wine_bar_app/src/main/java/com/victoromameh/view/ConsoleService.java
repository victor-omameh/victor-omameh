package com.victoromameh.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.victoromameh.model.Order;
import com.victoromameh.model.Recommendation;
import com.victoromameh.model.UserAccount;
import com.victoromameh.model.Wine;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	private static DecimalFormat df2 = new DecimalFormat("$##0.00");
	
	
	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}
	
	
	
	public void prompt(String prompt) {
		out.println(prompt);
	}
	
	public void errorMessage(String error) {
		out.println(error);
	}
	
	public int getMenuChoice(String[] options) {
		int userChoice = 0;
		while (userChoice == 0) {
			displayOptions(options);
			userChoice = getMenuChoiceFromUserInput(options);
			if (userChoice <= 0 || userChoice > options.length) {
				userChoice = 0;
			}
		}
		out.println();
		return userChoice;
	}
	
	private void displayOptions(String[] options) {
		
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}
	
	private int getMenuChoiceFromUserInput(String[] options) {
		int userChoice = 0;
		String userInput = in.nextLine();
		try {
			userChoice = Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (userChoice <= 0 || userChoice > options.length) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return userChoice;
	}
	
	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		boolean gettingInput = true;
		while(gettingInput) {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
				if(result < 0 ) {
					out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
				} else {
					gettingInput = false;
				}
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		}
		return result;
	}
	
	public void displayWineList(List<Wine> wineList, String listName) {
		
		String lineFormat = "%-5s %-25s %-15s %n";
		out.println("-----------------------------------------");
		out.println(listName + "\n");
		out.printf(lineFormat,"ID","Name & Varietal", "");
		out.println("-----------------------------------------");
		
		for (Wine wine : wineList) {
			if (wine.isFavorite()) {
				out.printf(lineFormat, wine.getWineId(), wine.getName() + ", " + wine.getVarietal(), "[Favorite]");
			} else {
				out.printf(lineFormat, wine.getWineId(), wine.getName() + ", " + wine.getVarietal(), "");

			}
		}
		out.println("-----------------------------------------");
	}
	
	public void displayWineDetails(Wine selectedWine) {
		
		out.println("-----------------------------------------");
		out.println("Wine Details");
		out.println("-----------------------------------------");
		out.println("ID: " + selectedWine.getWineId());
		out.println("Name: " + selectedWine.getName());
		if (selectedWine.getVintage() > 0) {
			out.println("Vintage: " + selectedWine.getVintage());
		} else {
			out.println("Vintage: Non-Vintage");
		}
		out.println("Category: " + selectedWine.getCategory());
		out.println("Varietal: " + selectedWine.getVarietal());
		out.println("Region: " + selectedWine.getRegion());
		out.println("Style: " + selectedWine.getStyle());
		out.println("Body: " + selectedWine.getBody());
		out.println("Tannins: " + selectedWine.getTannins());
		out.println("Acidity: " + selectedWine.getAcidity());
		System.out.print("Notes: ");
		
		for (int i = 0; i < selectedWine.getFruit().size(); i++) {
			if (i > 0) {
				System.out.print(", ");
			}
			System.out.print(selectedWine.getFruit().get(i));	
		}
		
		for (int i = 0; i < selectedWine.getNonFruit().size(); i++) {
			if (i >= 0) {
				out.print(", ");
			}
			out.print(selectedWine.getNonFruit().get(i));	
		}
		out.println();
		out.println("Price: " + df2.format(selectedWine.getPrice()));
		out.println();
		
		
	}
	
	public void displayWinningWines (Map<Wine, Integer> wineScores) {
		
		String lineFormat = "%-5s %-35s %n";
		out.println("-----------------------------------------");
		out.println("WINNERS\n");
		out.printf(lineFormat,"ID","Name & Varietal");
		out.println("-----------------------------------------");
		
		for (Map.Entry<Wine, Integer> entry : wineScores.entrySet()) {
			out.printf(lineFormat, entry.getKey().getWineId(), entry.getKey().getName() + ", " + entry.getKey().getVarietal());
		}
		
		out.println("-----------------------------------------");
		
	}
	
	public void displayFriendsList(List<UserAccount> friendsList) {
		String lineFormat = "%-5s %-35s %n";
		out.println("-----------------------------------------");
		out.println("Friends List\n");
		out.printf(lineFormat,"ID","Username");
		out.println("-----------------------------------------");
		
		for (UserAccount friend : friendsList) {
			out.printf(lineFormat, friend.getAccountId(), friend.getUsername());
		}
		out.println("-----------------------------------------");
	}
	
	public void displayRecomendations(List<Recommendation> recommendationsList) {
		
		String lineFormat = "%-5s %-15s %-15s %-15s %n";
		out.println("-----------------------------------------");
		out.println("Recommendations from Friends \n");
		out.printf(lineFormat,"ID","From", "Date", "Message");
		out.println("-----------------------------------------");
		
		for (Recommendation recommendation : recommendationsList) {
			out.printf(lineFormat, recommendation.getWineId(), recommendation.getUserFrom(), recommendation.getDate(), recommendation.getMessage());
		}
	}
	
	public void displayGifts(List<Order> gifts) {
		
		String lineFormat = "%-5s %-25s %-15s %-15s %n";
		out.println("-----------------------------------------");
		out.println("Gifts from Friends \n");
		out.printf(lineFormat,"ID","Wine", "From", "Message");
		out.println("-----------------------------------------");
		
		for (Order gift : gifts) {
			out.printf(lineFormat, gift.getWineId(), gift.getOrderedWine().getName(), gift.getUserFrom(), gift.getMessage() );
		}
		
		out.println();
	}
	
}

