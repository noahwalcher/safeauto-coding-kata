package main;

import java.util.Scanner;

//This class shows the introductory message to the user and get's user input.
public class Menu {
	
	private Scanner in = new Scanner(System.in);

	//Displays intro message.
	public void displayIntroMessage() {
		System.out.println("Welcome to the Driver Report Program!");
		System.out.println("You can add new drivers to the report using \"AddDriver [name]\"");
		System.out.println("You can remove drivers from the report using \"RemoveDriver [name]\"");
		System.out.println("You can add trips to the report using \"AddTrip [name,start time,stop time,miles driven]\"");
		System.out.println("You can remove trips from the report using \"RemoveTrip [id]\"");
		System.out.println("When you are finished you can type \"Report\" to see a final report of everything");
		System.out.println();
	}
	
	//Gets user input.
	public String getUserInput() {
		return in.nextLine();
	}
}
