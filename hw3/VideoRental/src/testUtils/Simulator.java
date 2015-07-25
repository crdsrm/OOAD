package testUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import models.CustomerTable;
import models.RentalLogTable;
import models.SystemDate;
import services.CustomerService;
import services.InventoryService;
import services.RentalService;
import services.VideoService;
import dataTypes.RentalLog;

public class Simulator {

	private int date;
	private ArrayList<String> availableVideosForDay;
	HashMap<String, String> customers;
	
	public Simulator() throws Exception {
		customers = new HashMap<String, String>();
		customers.put("Emma", "Regular");
		customers.put("Carl", "Hoarders");
		customers.put("Lisa", "Breezy");
		customers.put("Ethan", "Regular");
		customers.put("Ashley", "Hoarders");
		customers.put("Mathew", "Breezy");
		customers.put("Samantha", "Regular");
		customers.put("Ryan", "Hoarders");
		customers.put("Abigail", "Breezy");
		customers.put("James", "Regular");
		setupBusiness();
		date = SystemDate.date().now();
	}

	public void simulateDays(int n) throws Exception {
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		System.out.println("Setting up the Business.");
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		for(int i=0; i<n; i++) {
			System.out.println("Started Day - " + (i + 1));
			moveToNextDay();
			simulateDay();
			// TODO renting videos
			System.out.println("Ended Day - " + (i + 1));
			System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		}
	}

	public static void generateReport(String fileName) throws Exception {
		HashMap<String, String> response;
		response = RentalService.generateReport(fileName);
		if(response.get("status").equals("failure")) {
			throw new Exception(response.get("msg"));
		}
		else {
			System.out.println("Report has been generated!!");
		}
	}
	
	private void simulateDay() throws Exception {
		returnAnyRentedVideos();
		fetchAvailableVideos();
		if(businessClosed()) {
			System.out.println("Business is closed for the day - " + date + ".");
		}
		else {
			System.out.println("The number of videos available to rent are - " + availableVideosForDay.size());
			rentSomeVideos();
		}
	}

	private void setupBusiness() throws Exception {
		try {
			// Adding Customers
			for(String customer : customers.keySet()) {
				addCustomer(customer, customers.get(customer));
			}
			// Adding Videos
			addVideo("Unbroken", "NewRelease", 15.00);
			addVideo("Exodus: Gods and Kings", "NewRelease", 15.00);
			addVideo("Interstellar", "NewRelease", 15.00);
			addVideo("Penguins of Madagascar", "NewRelease", 15.00);
			addVideo("Cinderella", "Drama", 12.00);
			addVideo("The Age of Adaline", "Drama", 12.00);
			addVideo("True Story", "Drama", 12.00);
			addVideo("The Godfather", "Drama", 12.00);
			addVideo("Home", "Comedy", 3.00);
			addVideo("The Voices", "Comedy", 3.00);
			addVideo("Birdman: Or (The Unexpected Virtue of Ignorance)", "Comedy", 3.00);
			addVideo("The Wolf of Wall Street", "Comedy", 3.00);
			addVideo("The Longest Ride", "Romance", 6.00);
			addVideo("Beauty and the Beast", "Romance", 6.00);
			addVideo("Titanic", "Romance", 6.00);
			addVideo("Maleficent", "Romance", 6.00);
			addVideo("Unfriended", "Horror", 9.00);
			addVideo("The Conjuring", "Horror", 9.00);
			addVideo("What We Do in the Shadows", "Horror", 9.00);
			addVideo("Alien", "Horror", 9.00);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Simulation interrupted!!!");
		}
	}

	private void moveToNextDay() throws Exception{
		SystemDate.date().update(++date);
	}

	private Boolean businessClosed() {
		if(availableVideosForDay.size() == 0)
			return true;
		return false;
	}

	private void returnAnyRentedVideos() throws Exception {
		try {
			ArrayList<String> names = returningCustomers();
			if(names.size() == 0) {
				System.out.println("There are no videos to be returned today.");
			}
			else {
				for(String name: names) {
					HashMap<String, String> response = RentalService.returnVideos(name);
					if(response.get("status").equals("failure")) {
						throw new Exception("Error While returning videos rented by ".concat(name).
																					  concat("\n").
																					  concat(response.get("msg")));
					}
					else {
						System.out.println(response.get("data").trim());
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Simulation interrupted!!!");
		}
	}

	private void addCustomer(String custName, String customerType) throws Exception{
		HashMap<String, String> response;
		response = CustomerService.addCustomer(custName, customerType);
		if(response.get("status").equals("failure")) {
			throw new Exception("Error while adding ".concat(custName).concat(".\n").concat(response.get("msg")));
		}
	}

	private void addVideo(String videoName, String videoCategory, double price) throws Exception{
		HashMap<String, String> response;
		response = VideoService.addVideo(videoName, videoCategory, price);
		if(response.get("status").equals("failure")) {
			throw new Exception("Error while adding ".concat(videoName).concat(".\n").concat(response.get("msg")));
		}
	}

	private ArrayList<String> returningCustomers() {
		ArrayList<String> customerNames = new ArrayList<String>();
		ArrayList<RentalLog> rentals = RentalLogTable.rentals().fetchAllActiveRentals();
		for(RentalLog rental : rentals) {
			if(!customerNames.contains(rental.customer.name) && ((rental.date + rental.orderedDays) == SystemDate.date().now()))
				customerNames.add(rental.customer.name);
		}
		return customerNames;
	}

	private void fetchAvailableVideos() throws Exception {
		HashMap<String, String> response;
		response = InventoryService.availableVideos();
		if(response.get("status").equals("success")) {
			String data = response.get("data");
			availableVideosForDay = new ArrayList<String>();
			if(!data.equals("")) {
				String[] names = data.split(",");
				for(int i = 0; i < names.length; i++) {
					availableVideosForDay.add(names[i]);
				}
			}
		}
		else {
			throw new Exception("Error while fetching available videos.\n".concat(response.get("msg")));
		}
	}

	private ArrayList<String> availableCustomers() {
		ArrayList<String> result = new ArrayList<String>();
		for(String customer : customers.keySet())
		{
			int customerId = CustomerTable.customers().fetchRow(customer).id;
			int noOfRentedVideos = RentalLogTable.rentals().fetchActiveRentalsByCustomer(customerId).size();
			if(noOfRentedVideos == 0) {
				result.add(customer);
			}
		}
		return result;
	}

	private void rentSomeVideos() throws Exception {
		ArrayList<String> availableCustomers = availableCustomers();
		int n = randomNumber(availableCustomers.size(), 1);
		System.out.println(n);
		while((n > 0) && (availableCustomers.size() > 0)) {
			int rand = randomNumber(availableCustomers.size(), 1);
			String customer = availableCustomers.get(rand - 1);
			ArrayList<String> videos = new ArrayList<String>();
			int days = 0;
			if(customers.get(customer).equals("Hoarders")) {
				if(availableVideosForDay.size() >= 3) {
					days = 7;
					videos = fetchRandomAvailableVideos(3);
				}
			}
			else if(customers.get(customer).equals("Regular")) {
				if(availableVideosForDay.size() >= 3) {
					days = randomNumber(5, 3);
					videos = fetchRandomAvailableVideos(randomNumber(3, 1));
				}
			}
			else {
				if(availableVideosForDay.size() >= 2) {
					days = randomNumber(2, 1);
					videos = fetchRandomAvailableVideos(randomNumber(2, 1));
				}
			}
			if(videos.size() != 0) {
				HashMap<String, String> response = RentalService.rentVideos(customer, videos, days);
				if(response.get("status").equals("failure")) {
					throw new Exception(response.get("msg"));
				}
				else {
					for(String video : videos)
						System.out.println("'" + customer + "' rented '" + video + "' for '" + days + "' days.");
				}
			}
			availableCustomers.remove(customer);
			n -= 1;
		}
	}

	private ArrayList<String> fetchRandomAvailableVideos(int n) {
		ArrayList<String> videos = new ArrayList<String>();
		for(int i=0; i<n; i++) {
			int index = randomNumber(availableVideosForDay.size(), 1) - 1;
			String video = availableVideosForDay.get(index);
			videos.add(video);
			availableVideosForDay.remove(video);
		}
		return videos;
	}

	private int randomNumber(int maxValue, int minValue) {
		Random rand = new Random();
		return (rand.nextInt(maxValue - minValue + 1) + minValue);
	}
}
