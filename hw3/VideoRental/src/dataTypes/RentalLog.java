package dataTypes;

import utils.DataType;

public class RentalLog implements DataType{

	public static int count = 0;
	public static String header = "id,date,customer,video,orderedForDays,returnedDate";
	public int id;
	public int date;
	public Customer customer;
	public Video video;
	public int orderedDays;
	public int returnedDate;
	public double price;

	public RentalLog(int id, int date, Customer customer, Video video, int orderedDays, int returnedDate, double price) {
		this.id = id;
		this.date = date;
		this.customer = customer;
		this.video = video;
		this.orderedDays = orderedDays;
		this.returnedDate = returnedDate;
		this.price = price;
	}

	public RentalLog(int date, Customer customer, Video video, int orderedDays, int returnedDate, double price) {
		this.id = ++count;
		this.date = date;
		this.customer = customer;
		this.video = video;
		this.orderedDays = orderedDays;
		this.returnedDate = returnedDate;
		this.price = price;
	}
	
	public RentalLog() {
		this.id = ++count;
		this.date = 0;
		this.customer = null;
		this.video = null;
		this.orderedDays = 0;
		this.returnedDate = 0;
		this.price = 0.0;
	}
	
}
