package dataTypes;

import utils.CategoryType;
import utils.DataType;

public class Video implements DataType{
	
	public static int count = 0;
	public static String header = "id,name,categoryId,price";
	public int id;
	public String name;
	public CategoryType category;
	public double price;

	public Video(String name, String category, double price) {
		this.id = ++count;
		this.name = name;
		this.category = CategoryType.valueOf(category);
		this.price = price;
	}
	
	public Video(int id, String name, String category, double price) {
		this.id = id;
		this.name = name;
		this.category = CategoryType.valueOf(category);
		this.price = price;
	}
	
	public Video() {
		this.id = ++count;
		this.name = null;
		this.category = null;
		this.price = (double) 0.00;
	}

}
