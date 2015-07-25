package services;

import java.util.ArrayList;
import java.util.HashMap;
import models.InventoryTable;
import dataTypes.Inventory;

public class InventoryService {

	public static HashMap<String, String> availableVideos() {
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			String data = "";
			ArrayList<Inventory> inventories = InventoryTable.inventories().fetchAvailableVideos();
			for(Inventory inventory : inventories) {
				data = data.concat(inventory.video.name);
				data = data.concat(",");
			}
			if(!data.equals(""))
				data = data.substring(0, data.length() - 1);
			response.put("status", "success");
			response.put("msg", "Available videos fetched successfully.");
			response.put("data", data);
		}
		catch (Exception e) {
			response.put("status", "failure");
			response.put("msg", e.getMessage());
		}
		return response;
	}

}
