package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataTypes.Customer;
import dataTypes.RentalLog;
import dataTypes.Video;
import utils.Model;

public class RentalLogTable extends Model {

	private static RentalLogTable rentalLogObj = null;
	private ArrayList<RentalLog> data = new ArrayList<RentalLog>();
	private String fileName = "database/RentalLogs.csv";
	
	public static RentalLogTable rentals() {
		if(rentalLogObj == null)
			rentalLogObj = new RentalLogTable();
		return rentalLogObj;
	}

	public RentalLogTable() {
		File file = new File(fileName);
		if (file.isFile()){
			read();
		}
		else
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error while creating csv file !!!");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void read() {
		BufferedReader fileReader = null;
	     
        try {
        	
        	//Create a new list of RentalLog to be filled by CSV file data 
        	data = new ArrayList<RentalLog>();
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));
            
            //Read the CSV file header to skip it
            fileReader.readLine();
            
            int maxSNo = 0;
            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(",");
                if (tokens.length > 0) {
                	//Create a new RentalLog object and fill his  data
                	int sno = Integer.parseInt(tokens[0]);
                	if(sno > maxSNo)
                		maxSNo = sno;
                	Customer customer = CustomerTable.customers().fetchRow(Integer.parseInt(tokens[2]));
                	Video video = VideoTable.videos().fetchRow(Integer.parseInt(tokens[3]));
					RentalLog rentalLog = new RentalLog(sno, Integer.parseInt(tokens[1]), customer, video, Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Double.parseDouble(tokens[6]));
					data.add(rentalLog);
				}
            }
            RentalLog.count = maxSNo;
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

	}

	public void insertRow(int date, int customerId, int videoId, int orderedDays, int returnedDate) throws Exception {
		Customer customer = CustomerTable.customers().fetchRow(customerId);
		if(customer == null)
			throw new Exception("No such Customer!!");
    	Video video = VideoTable.videos().fetchRow(videoId);
    	if(video == null)
    		throw new Exception("No such Video!!");
    	if(!InventoryTable.inventories().isAvailable(videoId))
    		throw new Exception("Video is not available.");
    	InventoryTable.inventories().setAvailability(videoId, false);
		RentalLog rentalLog = new RentalLog(date, customer, video, orderedDays, returnedDate, video.price * orderedDays);
		data.add(rentalLog);
		write();
		read();
	}
	
	public void insertRows(int date, int customerId, ArrayList<Integer> videoIds, int orderedDays, int returnedDate) throws Exception {
		Customer customer = CustomerTable.customers().fetchRow(customerId);
		if(customer == null)
			throw new Exception("No such Customer!!");
		ArrayList<Video> videos = new ArrayList<Video>();
		for(int videoId : videoIds) {
			Video video = VideoTable.videos().fetchRow(videoId);
			if(video == null)
				throw new Exception("No such Video!!");
			if(!InventoryTable.inventories().isAvailable(videoId))
				throw new Exception("Video is not available.");
			videos.add(video);
		}
		for(Video video: videos) {
			InventoryTable.inventories().setAvailability(video.id, false);
			RentalLog rentalLog = new RentalLog(date, customer, video, orderedDays, returnedDate, video.price * orderedDays);
			data.add(rentalLog);
		}
		write();
		read();
	}
	
	public void updateReturnedDate(int id, int returnedDate) throws Exception {
		int i;
		for(i=0; i<data.size(); i++)
		{
			if(data.get(i).id == id) {
				if(data.get(i).returnedDate != 0)
					throw new Exception("Already video is returned by Customer!!");
				InventoryTable.inventories().setAvailability(data.get(i).video.id, true);
				data.get(i).returnedDate = returnedDate;
				break;
			}
		}
		if(i == data.size()) {
			throw new Exception("No Such Rental!!!");
		}
		write();
		read();
	}

	public ArrayList<RentalLog> fetchActiveRentalsByCustomer(int customerId) {
		ArrayList<RentalLog> rentalLogs = new ArrayList<RentalLog>();
		for(RentalLog rental: data)
		{
			if (rental.customer.id == customerId && rental.returnedDate == 0) {
				RentalLog rentalLog = new RentalLog(rental.id, rental.date, rental.customer, rental.video, rental.orderedDays, rental.returnedDate, rental.price);
				rentalLogs.add(rentalLog);
			}
		}
		return rentalLogs;
	}
	
	public ArrayList<RentalLog> fetchActiveRentalsByVideo(int videoId) {
		ArrayList<RentalLog> rentalLogs = new ArrayList<RentalLog>();
		for(RentalLog rental: data)
		{
			if (rental.video.id == videoId && rental.returnedDate == 0) {
				RentalLog rentalLog = new RentalLog(rental.id, rental.date, rental.customer, rental.video, rental.orderedDays, rental.returnedDate, rental.price);
				rentalLogs.add(rentalLog);
			}
		}
		return rentalLogs;
	}

	public ArrayList<RentalLog> fetchAll() {
		ArrayList<RentalLog> rentalLogs = new ArrayList<RentalLog>();
		for(RentalLog rental: data)
		{
			RentalLog rentalLog = new RentalLog(rental.id, rental.date, rental.customer, rental.video, rental.orderedDays, rental.returnedDate, rental.price);
			rentalLogs.add(rentalLog);
		}
		return rentalLogs;
	}

	public ArrayList<RentalLog> fetchInactiveRentals() {
		ArrayList<RentalLog> rentalLogs = new ArrayList<RentalLog>();
		for(RentalLog rental: data)
		{
			if (rental.returnedDate != 0) {
				RentalLog rentalLog = new RentalLog(rental.id, rental.date, rental.customer, rental.video, rental.orderedDays, rental.returnedDate, rental.price);
				rentalLogs.add(rentalLog);
			}
		}
		return rentalLogs;
	}

	public ArrayList<RentalLog> fetchAllActiveRentals() {
		ArrayList<RentalLog> rentalLogs = new ArrayList<RentalLog>();
		for(RentalLog rental: data)
		{
			if (rental.returnedDate == 0) {
				RentalLog rentalLog = new RentalLog(rental.id, rental.date, rental.customer, rental.video, rental.orderedDays, rental.returnedDate, rental.price);
				rentalLogs.add(rentalLog);
			}
		}
		return rentalLogs;
	}

	@Override
	protected void write() {
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(RentalLog.header.toString());
			
			//Add a new line separator after the header
			fileWriter.append("\n");
			
			//Write a new customer object list to the CSV file
			for (RentalLog rentalLog : data) {
				fileWriter.append(Integer.toString(rentalLog.id));
				fileWriter.append(",");
				fileWriter.append(Integer.toString(rentalLog.date));
				fileWriter.append(",");
				fileWriter.append(Integer.toString(rentalLog.customer.id));
				fileWriter.append(",");
				fileWriter.append(Integer.toString(rentalLog.video.id));
				fileWriter.append(",");
				fileWriter.append(Integer.toString(rentalLog.orderedDays));
				fileWriter.append(",");
				fileWriter.append(Integer.toString(rentalLog.returnedDate));
				fileWriter.append(",");
				fileWriter.append(Double.toString(rentalLog.price));
				fileWriter.append("\n");
			}
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}

	}

}
