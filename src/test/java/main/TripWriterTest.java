package main;

import java.io.File;

import org.junit.*;

public class TripWriterTest {
	
	private TripWriter tw = new TripWriter();
	private static File file = new File("TripsAndDriver.txt");

	@BeforeClass
	public static void setup() {
		file.delete();
	}
	
	@Test
	public void testing_writing_to_file_and_check_method() {
		tw.useFileWriter("Driver,Test");
		Assert.assertTrue(tw.checkIfExists("Driver,Test"));
	}
	
	@Test
	public void checking_case_sensitivity_of_check_method() {
		Assert.assertFalse(tw.checkIfExists("Driver,test"));
	}
	
	@Test
	public void removing_a_driver() {
		tw.useFileWriter("Driver,Noah");
		tw.removeDriver("RemoveDriver Noah");
		Assert.assertFalse(tw.checkIfExists("Driver,Noah"));
	}
	
	@Test
	public void adding_2_trips_with_consecutive_ids_then_removing_one() {
		tw.addTrip("AddTrip noah,2020-02-03 12:12,2020-02-03 12:12,24");
		tw.addTrip("AddTrip noah,2020-02-03 12:12,2020-02-03 12:12,24");
		tw.removeTrip("RemoveTrip 1");
	}
	
	@Test
	public void getting_1_back_as_highest_trip_id() {
		Assert.assertEquals(1, tw.getHighestId());
	}
	
	@Test
	public void generate_report() {
		tw.generateReport();
	}
	
	
	
}
