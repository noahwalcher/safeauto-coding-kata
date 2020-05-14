package main;

/*This program uses a command line interface to interact with a user.
 * The user is able to issue commands that add/remove drivers and trips
 * from a text file. The user can then generate a report of the
 * information in the text file.
 */
public class DriverReportCLI {

	private Menu menu;
	private DriverReport dr = new DriverReport();

	public DriverReportCLI(Menu menu) {
		this.menu = menu;
	}

	public static void main(String[] args) {
		Menu menu = new Menu();
		DriverReportCLI cli = new DriverReportCLI(menu);
		cli.run();
	}

	public void run() {
		menu.displayIntroMessage();
		boolean running = true;
		
		//Continuously takes user input as commands until the Report command is given.
		while (running) {
			String command = menu.getUserInput();
			running = dr.verifyInput(command);
		}
	}

}
