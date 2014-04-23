package de.tuberlin.dima.hbasealgebra.dataload;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import au.com.bytecode.opencsv.CSVReader;
import de.tuberlin.dima.hbasealgebra.util.HBaseHelper;


public class CSVLoad {

	private static HBaseHelper helper;
	private static HTable table;
	private static Configuration conf;
	private static String BerlinMOD="/home/faisal/drive/data/BerlinMOD/";
	public static void main(String[] args) throws IOException {
		conf = HBaseConfiguration.create(); // co PutExample-1-CreateConf Create the required configuration.
		// ^^ PutExample
		//	    helper = HBaseHelper.getHelper(conf);
		//	    helper.dropTable("testtable");
		//	    helper.createTable("testtable", "colfam1");
		// vv PutExample
		//	    table = new HTable(conf, "cars"); // NewTable Instantiate a new client.
		//	    loadCars();
		try {
			loadStreets();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void test() throws ParseException
	{
		String string_date = "2007-05-28 19:08:40.114";

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date d = f.parse(string_date);
		long milliseconds = d.getTime();
		System.out.println(milliseconds);
	}
	
	public static void loadStreets() throws IOException, ParseException
	{
		table = new HTable(conf, "streets"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"streets.csv"));
		String [] nextLine;
		reader.readNext();
		Integer i=0;
		while ((nextLine = reader.readNext()) != null) {
			Put put = new Put(Bytes.toBytes(i.toString())); 

			put.add(Bytes.toBytes("streets"), Bytes.toBytes("vmax"),
					Bytes.toBytes(nextLine[1])); 
			
			put.add(Bytes.toBytes("streets"), Bytes.toBytes("x1"),
					Bytes.toBytes(nextLine[2])); 
			
			put.add(Bytes.toBytes("streets"), Bytes.toBytes("y1"),
					Bytes.toBytes(nextLine[3])); 

			put.add(Bytes.toBytes("streets"), Bytes.toBytes("x2"),
					Bytes.toBytes(nextLine[4])); 
			
			put.add(Bytes.toBytes("streets"), Bytes.toBytes("y2"),
					Bytes.toBytes(nextLine[5])); 
			table.put(put); 
			i++;
		}
	}
	
	public static void loadQueryPoints() throws IOException, ParseException
	{
		table = new HTable(conf, "querypoints"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"querypoints.csv"));
		String [] nextLine;
		reader.readNext();
		Integer i=0;
		while ((nextLine = reader.readNext()) != null) {
			Put put = new Put(Bytes.toBytes(i.toString())); 

			put.add(Bytes.toBytes("points"), Bytes.toBytes("x"),
					Bytes.toBytes(nextLine[1])); 
			
			put.add(Bytes.toBytes("points"), Bytes.toBytes("y"),
					Bytes.toBytes(nextLine[2])); 

			table.put(put); 
			i++;
		}
	}
	
	public static void loadQueryPeriods() throws IOException, ParseException
	{
		table = new HTable(conf, "queryperiods"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"queryperiods.csv"));
		String [] nextLine;
		reader.readNext();
		Integer i=0;
		while ((nextLine = reader.readNext()) != null) {
			Put put = new Put(Bytes.toBytes(i.toString())); 

			put.add(Bytes.toBytes("periods"), Bytes.toBytes("begin"),
					Bytes.toBytes(timestampToLong(nextLine[1]))); 
			
			put.add(Bytes.toBytes("periods"), Bytes.toBytes("end"),
					Bytes.toBytes(timestampToLong(nextLine[2]))); 

			table.put(put); 
			i++;
		}
	}
	
	public static void loadQueryLicences() throws IOException, ParseException
	{
		table = new HTable(conf, "querylicences"); 
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"querylicences.csv"));
		String [] nextLine;
		reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			Put put = new Put(Bytes.toBytes(nextLine[0])); 

			put.add(Bytes.toBytes("licence"), Bytes.toBytes("licence"),
					Bytes.toBytes("1")); 

			table.put(put); 
		}
	}
	
	private static long timestampToLong(String timestamp) throws ParseException
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date d = f.parse(timestamp);
		return d.getTime();
	}
	public static void loadQueryInstants() throws IOException, ParseException
	{
		table = new HTable(conf, "queryinstants"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"queryinstants.csv"));
		String [] nextLine;
		reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			long milliseconds = timestampToLong(nextLine[1]);
			Put put = new Put(Bytes.toBytes(milliseconds)); 

			put.add(Bytes.toBytes("instant"), Bytes.toBytes("instant"),
					Bytes.toBytes("1")); 

			table.put(put); 
//			System.out.println(nextLine[0] + nextLine[1] + "etc...");
		}
	}
	
	public static void loadCars() throws IOException
	{
		table = new HTable(conf, "cars"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"datamcar.csv"));
		String [] nextLine;
		reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			Put put = new Put(Bytes.toBytes(nextLine[1])); 

			put.add(Bytes.toBytes("datamcar"), Bytes.toBytes("type"),
					Bytes.toBytes(nextLine[2])); 
			put.add(Bytes.toBytes("datamcar"), Bytes.toBytes("model"),
					Bytes.toBytes(nextLine[3])); 


			table.put(put); 
			System.out.println(nextLine[0] + nextLine[1] + "etc...");
		}
	}

}
