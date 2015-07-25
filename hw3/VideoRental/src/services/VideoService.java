package services;

import java.util.HashMap;

import models.VideoTable;

public class VideoService {

	public static HashMap<String, String> addVideo(String name, String category, double price) {
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			VideoTable.videos().insertRow(name, category, price);
			response.put("status", "success");
			response.put("msg", "Video added successfully.");
		}
		catch (Exception e) {
			response.put("status", "failure");
			response.put("msg", e.getMessage());
		}
		return response;
	}
	
}
