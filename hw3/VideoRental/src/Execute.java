import testUtils.Simulator;


public class Execute {

	public static void main(String args[]) throws Exception {
		int noOfDaysToSimulate = 35;
		Simulator simulator = new Simulator();
		simulator.simulateDays(noOfDaysToSimulate);
		Simulator.generateReport("BusinessStatusReport.html");
	}

}
