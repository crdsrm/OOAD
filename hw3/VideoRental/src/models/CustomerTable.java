package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dataTypes.Customer;
import utils.Model;

public class CustomerTable extends Model {

	private static CustomerTable customersObj = null;
	private ArrayList<Customer> data = new ArrayList<Customer>();
	private String fileName = "database/Customers.csv";
	
	public static CustomerTable customers() {
		if(customersObj == null)
			customersObj = new CustomerTable();
		return customersObj;
	}
	
	public CustomerTable() {
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
        	
        	//Create a new list of Customer to be filled by CSV file data 
        	data = new ArrayList<Customer>();
        	
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
                	//Create a new Customer object and fill his  data
                	int sno = Integer.parseInt(tokens[0]);
                	if(sno > maxSNo)
                		maxSNo = sno;
					Customer customer = new Customer(sno, tokens[1], tokens[2]);
					data.add(customer);
				}
            }
            Customer.count = maxSNo;
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

	public void insertRow(String name, String customerType) throws Exception {
		if(fetchRow(name) != null)
			throw new Exception("Customer already exists!!");
		Customer customer = new Customer(name, customerType);
		data.add(customer);
		write();
		read();
	}

	public Customer fetchRow(String name) {
		Customer customer = null;
		for(Customer cust: data)
		{
			if (cust.name.contentEquals(name))
				customer = new Customer(cust.id, cust.name, cust.customerType.toString());
		}
		return customer;
	}

	public Customer fetchRow(int key) {
		Customer customer = null;
		for(Customer cust: data)
		{
			if (cust.id == key)
				customer = new Customer(cust.id, cust.name, cust.customerType.toString());
		}
		return customer;
	}

	public ArrayList<Customer> all() {
		ArrayList<Customer> result = new ArrayList<Customer>();
		for(Customer cust: data) {
			result.add(new Customer(cust.id, cust.name, cust.customerType.toString()));
		}
		return result;
	}
	
	@Override
	protected void write() {
		FileWriter fileWriter = null;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(Customer.header.toString());
			
			//Add a new line separator after the header
			fileWriter.append("\n");
			
			//Write a new customer object list to the CSV file
			for (Customer customer : data) {
				fileWriter.append(String.valueOf(customer.id));
				fileWriter.append(",");
				fileWriter.append(customer.name);
				fileWriter.append(",");
				fileWriter.append(customer.customerType.toString());
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
