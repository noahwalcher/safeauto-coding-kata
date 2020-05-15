package main;

import java.io.File;

import org.junit.*;

public class DriverReportTest {

	private DriverReport dr = new DriverReport();
	private static File file = new File("TripsAndDriver.txt");
	
	@Test
	public void add_driver_without_name_fails() {
		dr.verifyInput("AddDriver");
	}
	
	@Test
	public void add_driver_with_name_passes_then_remove_driver() {
		dr.verifyInput("AddDriver Noah");
		dr.verifyInput("RemoveDriver Noah");
	}
	
	@Test
	public void add_trip_then_remove_trip() {
		dr.verifyInput("AddTrip noah,2020-02-03 12:12,2020-02-03 12:12,24");
		dr.verifyInput("RemoveTrip 1");
	}
	
	@Test
	public void multiple_failures_for_adding_trip() {
		dr.verifyInput("AddTrip 2020-02-03 12:12,2020-02-03 12:12,24");
		dr.verifyInput("AddTrip noah,22020-02-03 12:12,2020-02-03 12:12,24");
		dr.verifyInput("AddTrip noah,2020-02-03 1212,2020-02-03 12:12,24");
		dr.verifyInput("AddTrip noah,2020-02-03 12:12,2020-02-03 12:1224");
		dr.verifyInput("AddTrip noah,2020-02-033 12:12,2020-02-03 12:12,24");
	}
	
	@Test
	public void run_report() {
		dr.verifyInput("RemoveDriver Noah");
		dr.verifyInput("Report");
	}

	
	
}
