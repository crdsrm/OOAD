package utils;

/**
 * The Data is stored in the form of CSV Files. 
 * The Model Class contains the methods to read & write the CSV files and 
 * insert & fetch rows from the table.
 */
public abstract class Model {

	public String fileName = null;

	/*
	 * Reads the table from database(CSV)
	 */
	protected abstract void read();
	
	/*
	 * Writes the table to database(CSV)
	 */
	protected abstract void write();
}
