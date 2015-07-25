package services;

import java.util.HashMap;

import models.CustomerTable;

public class CustomerService {

	public static HashMap<String, String> addCustomer(String custName, String customerType) {
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			CustomerTable.customers().insertRow(custName, customerType);
			response.put("status", "success");
			response.put("msg", "Customer Added Successfully.");
		}
		catch (Exception e) {
			response.put("status", "failure");
			response.put("msg", e.getMessage());
		}
		return response;
	}

}
