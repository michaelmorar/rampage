package com.ares.common.util.data;

import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

public class NameSurnameDSBuilder {

	private CSVReader csvr; 
	
	public NameSurnameDSBuilder(Reader r) { 
		csvr = new CSVReader(r); 
	}
	
	public void loadDataset() {
		try {
			String[] line = csvr.readNext();
			
			for (String l: line) {
				
			}
			
		} catch (IOException e) {
			System.out.println("IO Exception when trying to read next line of CSV file");
			e.printStackTrace();
		} 
		
	}
	
	public NameSurnameDataSet generateNSD() {
		
		NameSurnameDataSet nsds = new NameSurnameDataSet(); 
		
		/*
		 * TODO - Loop through the CSV file, line by line, and add each name/surname to the nsds
		 */
		
		StringFlyWeightPool firstNamePool = new StringFlyWeightPool(); 
		StringFlyWeightPool surnamePool = new StringFlyWeightPool(); 
		
		return nsds;
		
	}
	
}
