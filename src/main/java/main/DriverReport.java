package main;

import java.util.regex.Pattern;

//Works as a carrier between he CLI class and the TripWriter. Also validates input.
public class DriverReport {

	private TripWriter tw = new TripWriter();

	/*Checks to make sure user input matches one of the possible commands and is of appropriate length.
	Each command returns true to keep the program running except for the Report command which ends the program.*/
	public boolean verifyInput(String command) {
		if (command.startsWith("AddDriver ") && command.length() > 10) {
			tw.addDriver(command);
			return true;
		} else if (command.startsWith("RemoveDriver ") && command.length() > 13) {
			tw.removeDriver(command);
			return true;
		} else if (command.startsWith("AddTrip ") && command.length() > 8) {
			
			//Secondary round of authentication on the user input for the AddTrip command. Makes sure that valid formatting is used using regex.
			if (Pattern.matches(
					"AddTrip [A-Z,a-z]+,\\d{4}-[01]\\d-[0-3]\\d\\s[0-2]\\d((:[0-5]\\d)?){2},\\d{4}-[01]\\d-[0-3]\\d\\s[0-2]\\d((:[0-5]\\d)?){2},[0-9][A-Za-z0-9 -]*$",
					command)) {
				tw.addTrip(command);
				return true;
			}
			System.out.println("Invalid formatting. Please try again.");
			return true;
		} else if (command.startsWith("RemoveTrip ") && command.length() > 11) {
			tw.removeTrip(command);
			return true;
		} else if (command.startsWith("Report")) {
			tw.generateReport();
			return false;
		} else {
			System.out.println("Sorry, but that command was not recognized. Please try again.");
			return true;
		}
	}
}
