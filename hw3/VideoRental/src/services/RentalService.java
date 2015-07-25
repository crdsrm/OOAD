package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import models.CustomerTable;
import models.RentalLogTable;
import models.SystemDate;
import models.VideoTable;
import dataTypes.RentalLog;

public class RentalService {

	public static HashMap<String, String> rentVideos(String custName, ArrayList<String> videoNames, int noOfDays) {
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			int custId = CustomerTable.customers().fetchRow(custName).id;
			ArrayList<Integer> videoIds = new ArrayList<Integer>();
			for(String videoName : videoNames) {
				videoIds.add(VideoTable.videos().fetchRow(videoName).id);
			}
			RentalLogTable.rentals().insertRows(SystemDate.date().now(), custId, videoIds, noOfDays, 0);
			response.put("status", "success");
			response.put("msg", "Videos Rented Successfully.");
		}
		catch (Exception e) {
			response.put("status", "failure");
			response.put("msg", e.getMessage());
		}
		return response;
	}

	public static HashMap<String, String> rentVideo(String custName, String videoName, int noOfDays) {
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			int custId = CustomerTable.customers().fetchRow(custName).id;
			int	videoId = VideoTable.videos().fetchRow(videoName).id;
			RentalLogTable.rentals().insertRow(SystemDate.date().now(), custId, videoId, noOfDays, 0);
			response.put("status", "success");
			response.put("msg", "Video Rented Successfully.");
		}
		catch (Exception e) {
			response.put("status", "failure");
			response.put("msg", e.getMessage());
		}
		return response;
	}

	public static HashMap<String, String> returnVideos(String custName) {
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			int custId = CustomerTable.customers().fetchRow(custName).id;
			ArrayList<RentalLog> rentalLogs = RentalLogTable.rentals().fetchActiveRentalsByCustomer(custId);
			if (rentalLogs.size() == 0)
				throw new Exception("There are no videos to be returned.");
			ArrayList<RentalLog> todaysRental = new ArrayList<RentalLog>();
			for(RentalLog rental: rentalLogs) {
				if((rental.date + rental.orderedDays) == SystemDate.date().now())
					todaysRental.add(rental);
			}
			if (todaysRental.size() == 0)
				throw new Exception("There are no videos to be returned today.");
			String data = "";
			for(RentalLog rental: todaysRental) {
				RentalLogTable.rentals().updateReturnedDate(rental.id, SystemDate.date().now());
				data = data.concat("'" + rental.customer.name + "' returned '" + rental.video.name + "'.\n");
			}
			response.put("status", "success");
			response.put("msg", "Video Returned Successfully.");
			response.put("data", data);
		}
		catch (Exception e) {
			response.put("status", "failure");
			response.put("msg", e.getMessage());
		}
		return response;
	}
	
	public static HashMap<String, String> generateReport(String fileName) {
		HashMap<String, String> response = new HashMap<String, String>();
		File file = new File(fileName);
		if (!file.isFile()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error while creating csv file !!!");
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			fileWriter.append("\n------------------------------------------------------------------------------------");
			fileWriter.append("\nGenerating Report for Day - " + SystemDate.date().now());
			fileWriter.append("\n------------------------------------------------------------------------------------");
			fileWriter.append("\nVideos available in the store:");
			HashMap<String, String> response1 = InventoryService.availableVideos();
			if(response1.get("status").equals("failure")) {
				throw new Exception(response1.get("msg"));
			}
			else {
				String data = response1.get("data");
				if(!data.equals("")) {
					String[] names = data.split(",");
					for(int i=0; i<names.length; i++) {
						fileWriter.append("\n\t" + i + ". " + names[i]);
					}
				}
				else {
					fileWriter.append("\n\tThere are no videos available in the store.");
				}
			}
			ArrayList<RentalLog> rentals = RentalLogTable.rentals().fetchAll();
			double totalAmount = 0.0;
			for(RentalLog rental: rentals) {
				totalAmount += rental.price;
			}
			fileWriter.append("\n\nThe amount of money the store made up to date is: " + Double.toString(totalAmount));
			fileWriter.append("\n\nList of Completed Rentals:");
			ArrayList<RentalLog> inactiveRentals = RentalLogTable.rentals().fetchInactiveRentals();
			fileWriter.append("  Count - " + inactiveRentals.size());
			int[] maxlengths = {8, 5, 4, 6};
			for(RentalLog rental: inactiveRentals) {
				if(rental.customer.name.length() > maxlengths[0])
					maxlengths[0] = rental.customer.name.length();
				if(rental.video.name.length() > maxlengths[1])
					maxlengths[1] = rental.video.name.length();
				if(Double.toString(rental.price).length() > maxlengths[3])
					maxlengths[3] = Double.toString(rental.price).length();
			}
			String format = "| %" + maxlengths[0] + "s | %" + maxlengths[1] + "s | %" + maxlengths[2] + "s | %" + maxlengths[3] + "s |";
			String header = String.format(format, "Customer", "Video", "Days", "Price");
			String stroke = "";
			for(int i=0; i<header.length(); i++) {
				stroke += "-";
			}
			fileWriter.append("\n".concat(stroke));
			fileWriter.append("\n".concat(header));
			fileWriter.append("\n".concat(stroke.replaceAll("-", "=")));
			for(RentalLog rental: inactiveRentals) {
				fileWriter.append("\n" + String.format(format, rental.customer.name, rental.video.name, rental.orderedDays, Double.toString(rental.price)));
				fileWriter.append("\n".concat(stroke));
			}
			ArrayList<RentalLog> activeRentals = RentalLogTable.rentals().fetchAllActiveRentals();
			int[] maxlengths1 = {8, 5, 4, 6};
			fileWriter.append("\n\nList of Active Rentals:");
			fileWriter.append("  Count - " + activeRentals.size());
			for(RentalLog rental: activeRentals) {
				if(rental.customer.name.length() > maxlengths1[0])
					maxlengths1[0] = rental.customer.name.length();
				if(rental.video.name.length() > maxlengths1[1])
					maxlengths1[1] = rental.video.name.length();
				if(Double.toString(rental.price).length() > maxlengths1[3])
					maxlengths1[3] = Double.toString(rental.price).length();
			}
			format = "| %" + maxlengths1[0] + "s | %" + maxlengths1[1] + "s | %" + maxlengths1[2] + "s | %" + maxlengths1[3] + "s |";
			header = String.format(format, "Customer", "Video", "Days", "Price");
			stroke = "";
			for(int i=0; i<header.length(); i++) {
				stroke += "-";
			}
			fileWriter.append("\n".concat(stroke));
			fileWriter.append("\n".concat(header));
			fileWriter.append("\n".concat(stroke.replaceAll("-", "=")));
			for(RentalLog rental: activeRentals) {
				fileWriter.append("\n" + String.format(format, rental.customer.name, rental.video.name, rental.orderedDays, Double.toString(rental.price)));
				fileWriter.append("\n".concat(stroke));
			}
			response.put("status", "success");
			response.put("msg", "report generated successfully.");
		}
		catch (Exception e) {
			response.put("status", "failure");
			e.printStackTrace();
			response.put("msg", e.getMessage());
		}
		finally {	
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
		}
		return response;
	}

}
