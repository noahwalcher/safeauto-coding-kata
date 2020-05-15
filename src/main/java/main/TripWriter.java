package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/*This class handles the work associated with reading and writing the text file */
public class TripWriter {

	private static final String FILE_NAME = "TripsAndDriver.txt";
	private File file = new File(FILE_NAME);
	private FileWriter fw;
	private BufferedWriter bw;
	private boolean isOpen = false;

	// Given a String append, the method will add that line to the text file.
	public void useFileWriter(String append) {
		try {
			// Checks to see if the file/bufferedwriter are already open. If not, opens
			// them. They remain open until the Report command is issued
			if (!isOpen) {
				fw = new FileWriter(file, true);
				bw = new BufferedWriter(fw);
				isOpen = true;
				bw.newLine();
			}
			bw.write(append);
			bw.flush();
			bw.newLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method is used to check if a Driver already exists in the text file to
	// avoid duplicates. Returns true if already exists.
	public boolean checkIfExists(String check) {
		try {
			Scanner fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
				if (check.equals(fileScanner.nextLine())) {
					return true;
				}
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * This method is used to remove lines from the text file. It does so by
	 * creating a list of Strings and only copying over lines that you don't
	 * want deleted.
	 */
	public void editFile(String remove) {
		try {
			List<String> lines = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
		
			// Make sure the buffered writer from useFileWriter method is closed from earlier
			if (isOpen) {
				bw.close();
				fw.close();
				isOpen = false;
			}
			String lineToRemove = remove;
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				String trimmedLine = currentLine.trim();

				// checks if the line is identical to the one we're trying to remove. Only works
				// for Driver,[name] lines.
				if (trimmedLine.equals(lineToRemove)
						// Or checks to see if line starts with our desired text. This is for lines that
						// start with Trip,[id].
						|| (trimmedLine.startsWith(remove) && trimmedLine.startsWith("Trip"))
						//Lastly checks if there is an empty line to avoid copying over.
						|| trimmedLine.equals(""))
					continue;
				
				lines.add(currentLine + System.getProperty("line.separator"));
			}
			//Create a new writer that is not in append mode.
			fw = new FileWriter(file, false);
			bw = new BufferedWriter(fw);
			for (String line : lines) {
				bw.write(line);
				bw.flush();
			}
			System.out.println("Your change is complete.");
			reader.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// This method runs through the text file and finds the highest current trip id
	// and returns and int 1 greater.
	public int getHighestId() {
		int highest = 1;
		String line;
		try {
			Scanner fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
				line = fileScanner.nextLine();
				if (line.startsWith("Trip,")) {
					String[] array = line.split(",");
					int id = Integer.parseInt(array[1]);
					if (id >= highest) {
						highest = id + 1;
					}
				}
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return highest;
	}

	// Checks to see if a Driver exists already. If not, adds the new driver to the
	// text file.
	public void addDriver(String driver) {
		String name = driver.substring(10);
		String append = "Driver," + name;
		if (checkIfExists(append)) {
			System.out.println(name + " already exists.");
		} else {
			useFileWriter(append);
			System.out.println("Driver " + name + " was successfully added.");
		}
	}

	// Removes any instances of Driver,[name] lines in the text file with the
	// desired name.
	public void removeDriver(String driver) {
		String name = driver.substring(13);
		String remove = "Driver," + name;
		editFile(remove);
	}

	// Formats and adds a Trip line to the text file.
	public void addTrip(String trip) {
		// Removes the 'AddTrip ' from the command issued
		String allData = trip.substring(8);

		// Splits all of the data from the user input and designates it as appropriate
		// types
		String[] splitData = allData.split(",");
		String name = splitData[0];
		String start = splitData[1];
		String end = splitData[2];
		int miles = Integer.parseInt(splitData[3]);

		/*
		 * Converts the String dates from user input into LocalDateTime objects, then
		 * converts them back into Strings in the desired format for the text file.
		 */
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime startDate = LocalDateTime.parse(start, formatter);
		LocalDateTime endDate = LocalDateTime.parse(end, formatter);
		formatter = DateTimeFormatter.ofPattern("MM-dd-YYYY-HH:mm");
		String finalStart = startDate.format(formatter);
		String finalEnd = endDate.format(formatter);

		// If driver from the trip doesn't exists as a Driver yet, adds them to the text
		// file.
		if (!checkIfExists("Driver," + name)) {
			useFileWriter("Driver," + name);
		}

		// Assembles the String that will be added to the text file and finally returns
		// the id of the newly created trip.
		int id = getHighestId();
		String input = "Trip," + id + "," + name + "," + finalStart + "," + finalEnd + "," + miles;
		useFileWriter(input);
		System.out.println("The id for your trip is: " + id);
	}

	// Removes a trip with the desired trip id from the text file.
	public void removeTrip(String command) {
		String remove = "Trip," + command.substring(11);
		editFile(remove);
		System.out.println("This trip was successfully removed.");
	}

	// Generates a final report from the text file. Will also end the program.
	public void generateReport() {
		// Create a list of Drivers and Trips for ease of use later.
		List<Driver> drivers = new ArrayList<Driver>();
		List<Trip> trips = new ArrayList<Trip>();
		try {
			// if the writers are open from the useFileWriter method closes them.
			if (isOpen) {
				bw.close();
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		try {
			Scanner fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();

				// While looping through the text file. Save every Driver line as a Driver
				// object and add it to the list from earlier.
				if (line.startsWith("Driver,")) {
					Driver driver = new Driver();
					driver.setName(line.split(",")[1]);
					drivers.add(driver);
				}

				// Grabs all of the trips from text file, saves them as Trip objects, and adds
				// them to the list from earlier.
				else if (line.startsWith("Trip,")) {
					String[] divided = line.split(",");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy-HH:mm");
					LocalDateTime startDate = LocalDateTime.parse(divided[3], formatter);
					LocalDateTime endDate = LocalDateTime.parse(divided[4], formatter);
					long duration = Duration.between(startDate, endDate).toHours();
					Trip trip = new Trip();
					trip.setName(divided[2]);
					trip.setHoursTaken(duration);
					trip.setMilesDriven(Double.parseDouble(divided[5]));
					trips.add(trip);
				}
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Driver Report");

		// Loops through the list of drivers
		for (Driver driver : drivers) {

			// For each driver, find all trips with matching driver name. Extract the
			// appropriate info and store in Driver object.
			for (Trip trip : trips) {
				if (trip.getName().equals(driver.getName())) {

					// Discard any trips not between an average of 5mph and 100mph.
					double mph = trip.getMilesDriven() / trip.getHoursTaken();
					if (mph > 5.0 && mph < 100.00) {
						driver.setTotalMiles(driver.getTotalMiles() + (int) trip.getMilesDriven());
						driver.setHoursTaken(driver.getHoursTaken() + trip.getHoursTaken());
					}
				}
			}
		}

		// Sorts the list of drivers by total miles driven.
		drivers.sort((Driver d1, Driver d2) -> ((Integer) d2.getTotalMiles()).compareTo(d1.getTotalMiles()));

		// Loops through the newly sorted Drivers list and outputs the report
		// information to console
		for (Driver driver : drivers) {
			if (driver.getTotalMiles() < 1) {
				System.out.println(driver.getName() + " drove " + driver.getTotalMiles() + " miles");
			} else {
				System.out.println(driver.getName() + " drove " + driver.getTotalMiles() + " miles at an average of "
						+ (int) (driver.getTotalMiles() / driver.getHoursTaken()) + " mph");
			}
		}
	}
}
