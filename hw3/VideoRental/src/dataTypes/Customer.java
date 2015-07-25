package dataTypes;

import utils.CustomerType;
import utils.DataType;

public class Customer implements DataType{
	
	public static int count = 0;
	public static String header = "id,name,customerType"; 
	public int id;
	public String name;
	public CustomerType customerType;

	/*
	 * To create new Customer
	 */
	public Customer(String name, String customerType) {
		this.id = ++count;
		this.name = name;
		this.customerType = CustomerType.valueOf(customerType);
	}
	
	/*
	 * To create customer object for existing customer
	 */
	public Customer(int id, String name, String customerType) {
		this.id = id;
		this.name = name;
		this.customerType = CustomerType.valueOf(customerType);
	}

	public Customer() {
		id = ++count;
		name = null;
		customerType = null;
	}

}
