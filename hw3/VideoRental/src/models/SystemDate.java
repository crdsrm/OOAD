package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import utils.Model;

public class SystemDate extends Model {
	
	private static SystemDate datObj = null;
	private int data = 0;
	private String fileName = "database/date.csv";
	
	public static SystemDate date() {
		if(datObj == null)
			datObj = new SystemDate();
		return datObj;
	}
	
	public SystemDate() {
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
        	
        	//Create a new list of Video to be filled by CSV file data 
        	data = 0;
        	
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
					data = Integer.parseInt(tokens[0]);
					break;
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

	public void update(int date) {
		data = date;
		write();
		read();
	}
	
	public int now() {
		return data;
	}

	@Override
	protected void write() {
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append("systemDate");
			
			//Add a new line separator after the header
			fileWriter.append("\n");
			
			//Write a new Video object list to the CSV file
			fileWriter.append(Integer.toString(data));
			
			fileWriter.append("\n");
			
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
