package dataTypes;

import utils.DataType;

public class Inventory implements DataType{

	public static String header = "videoId,available";
	public Video video;
	public Boolean available;
	
	public Inventory(Video video, Boolean available){
		this.video = video;
		this.available = available;
	}
	
	public Inventory(){
		video = null;
		available = true;
	}

}
