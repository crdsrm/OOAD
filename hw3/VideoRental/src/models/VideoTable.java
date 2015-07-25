package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataTypes.Video;
import utils.Model;

public class VideoTable extends Model {

	private static VideoTable videosObj = null;
	private ArrayList<Video> data = new ArrayList<Video>();
	private String fileName = "database/Videos.csv";
	
	public static VideoTable videos() {
		if(videosObj == null)
			videosObj = new VideoTable();
		return videosObj;
	}
	
	public VideoTable() {
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
        	data = new ArrayList<Video>();
        	
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
                	//Create a new Video object and fill his  data
            		int sno = Integer.parseInt(tokens[0]);
            		if(sno > maxSNo) {
            			maxSNo = sno;
            		}
					Video Video = new Video(sno, tokens[1], tokens[2], Double.parseDouble(tokens[3]));
					data.add(Video);
				}
            }
            Video.count = maxSNo;
            
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

	public void insertRow(String name, String category, double price) throws Exception {
		if(fetchRow(name) != null)
			throw new Exception("Video already exists!!");
		Video video = new Video(name, category, price);
		data.add(video);
		InventoryTable.inventories().insertRow(video.id);
		write();
		read();
	}

	public Video fetchRow(String name) {
		Video video = null;
		for(Video vid: data)
		{
			if (vid.name.contentEquals(name))
				video = new Video(vid.id, vid.name, vid.category.toString(), vid.price);
		}
		return video;
	}

	public Video fetchRow(int key) {
		Video video = null;
		for(Video vid: data)
		{
			if (vid.id == key)
				video = new Video(vid.id, vid.name, vid.category.toString(), vid.price);
		}
		return video;
	}

	@Override
	protected void write() {
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(Video.header.toString());
			
			//Add a new line separator after the header
			fileWriter.append("\n");
			
			//Write a new Video object list to the CSV file
			for (Video video : data) {
				fileWriter.append(Integer.toString(video.id));
				fileWriter.append(",");
				fileWriter.append(video.name);
				fileWriter.append(",");
				fileWriter.append(video.category.toString());
				fileWriter.append(",");
				fileWriter.append(Double.toString(video.price));
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
