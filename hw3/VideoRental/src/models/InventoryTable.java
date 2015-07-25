package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utils.Model;
import dataTypes.Inventory;
import dataTypes.Video;

public class InventoryTable extends Model {

	private static InventoryTable inventoriesObj = null;
	private ArrayList<Inventory> data = new ArrayList<Inventory>();
	private String fileName = "database/Inventories.csv";
	
	public static InventoryTable inventories(){
		if(inventoriesObj == null)
			inventoriesObj = new InventoryTable();
		return inventoriesObj;
	}
	
	public InventoryTable() {
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

        	//Create a new list of Inventory to be filled by CSV file data 
        	data = new ArrayList<Inventory>();

            String line = "";

            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));

            //Read the CSV file header to skip it
            fileReader.readLine();

            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(",");
                if (tokens.length > 0) {
                	//Create a new Inventory object and fill his  data
            		Video video = VideoTable.videos().fetchRow(Integer.parseInt(tokens[0]));
					Inventory Inventory = new Inventory(video, Boolean.parseBoolean(tokens[1]));
					data.add(Inventory);
				}
            }

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
	
	public void insertRow(int videoId) throws Exception {
		Video video = VideoTable.videos().fetchRow(videoId);
		if(video == null) {
			throw new Exception("No Such Video");
		}
		if(InventoryTable.inventories().fetchRow(videoId) == null) {
			Inventory Inventory = new Inventory(video, true);
			data.add(Inventory);
			write();
			read();
		}
	}
	
	public Boolean isAvailable(int videoId) throws Exception {
		int i;
		for(i=0; i<data.size(); i++)
		{
			if(data.get(i).video.id == videoId) {
				return(data.get(i).available);
			}
		}
		throw new Exception("No Such Inventory !!!");
	}
	
	public void setAvailability(int videoId, Boolean available) throws Exception {
		int i;
		for(i=0; i<data.size(); i++)
		{
			if(data.get(i).video.id == videoId) {
				Inventory Inventory = new Inventory(data.get(i).video, available);
				data.set(i, Inventory);
				write();
				read();
				break;
			}
		}
		if(i == data.size()) {
			throw new Exception("No Such Inventory !!!");
		}
	}

	public Inventory fetchRow(int key) {
		Inventory inventory = null;
		for(Inventory inv: data)
		{
			if (inv.video.id == key)
				inventory = new Inventory(inv.video, inv.available);
		}
		return inventory;
	}
	
	public ArrayList<Inventory> fetchAvailableVideos() {
		ArrayList<Inventory> result = new ArrayList<Inventory>();
		for(Inventory inv: data)
		{
			if(inv.available)
				result.add(new Inventory(inv.video, inv.available));
		}
		return result;
	}

	@Override
	protected void write() {
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(Inventory.header.toString());
			
			//Add a new line separator after the header
			fileWriter.append("\n");
			
			//Write a new Inventory object list to the CSV file
			for (Inventory inventory : data) {
				fileWriter.append(Integer.toString(inventory.video.id));
				fileWriter.append(",");
				fileWriter.append(Boolean.toString(inventory.available));
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
