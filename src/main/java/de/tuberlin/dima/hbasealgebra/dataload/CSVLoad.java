package de.tuberlin.dima.hbasealgebra.dataload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.thrift.scheme.TupleScheme;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

import ch.hsr.geohash.GeoHash;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Bbox;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.mPoint;
import de.tuberlin.dima.hbasealgebra.util.HBaseHelper;
import de.tuberlin.dima.hbasealgebra.util.Transformer;


public class CSVLoad {

	private static HBaseHelper helper;
	private static HTable table;
	private static Configuration conf;
	private static String BerlinMOD="/home/faisal/drive/data/BerlinMOD/";
	
	public static void main(String[] args) throws IOException, ParseException {
		conf = HBaseConfiguration.create(); // co PutExample-1-CreateConf Create the required configuration.
		//			loadStreets();
//		loadTrips();
		transformTrips();
	}
	private static List<TupleObject> tuples = new ArrayList<TupleObject>();
	
	
	public static List<mPoint> readTrips() throws IOException, ParseException
	{
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"trips.csv"));
		String [] nextLine;
		List<mPoint> mpointList = new ArrayList<mPoint>();
		reader.readNext();
		int i=0;
		TupleObject to = new TupleObject();
//		mPoint mp = new mPoint();
		double xStartOld=0;
		double yStartOld=0;
		double xEndOld=0;
		double yEndOld=0;
		String startTimeOld;
		String endTimeOld = null;
		long moidOld = 0;
		long tripIdOld = 0;
		int line=1;
		TupleObject toObject=null;
		mPoint mpoint=null;
		List<Coordinate> coords=null;
		while ((nextLine = reader.readNext()) != null) {
//			Put put = new Put(Bytes.toBytes(nextLine[1])); 
			double xStart = Double.parseDouble(nextLine[4]);
			double yStart = Double.parseDouble(nextLine[5]);
			
			double xEnd = Double.parseDouble(nextLine[6]);
			double yEnd = Double.parseDouble(nextLine[7]);
			
			int moid = Integer.parseInt(nextLine[0]);
			long tripId = Long.parseLong(nextLine[1]);
			String startTime=nextLine[2];
			String endTime=nextLine[3];
			if(moidOld==moid && tripIdOld==tripId){//within a trip of an mpoint
//				if(xStart!=xEndOld || yStart!=yEndOld || startTime.compareTo(endTimeOld)!=0){
//					System.out.println("data is not clean at line "+line);
//				}
				coords.add(new Coordinate(xStart, yStart, timestampToLong(startTime)));
			}
			if(moidOld==moid && tripIdOld!=tripId){//end of the current trip
				//create a new trip in mpoint
				coords.add(new Coordinate(xEndOld, yEndOld, timestampToLong(endTimeOld)));
				if(mpoint==null){
					mpoint = new mPoint();
				}
				mpoint.add(coords);
				coords=new ArrayList<Coordinate>();
				coords.add(new Coordinate(xStart, yStart, timestampToLong(startTime)));
			}
			if(moidOld!=moid){
				if(coords!=null){
					coords.add(new Coordinate(xEndOld, yEndOld, timestampToLong(endTime)));
					to.setMpoint(mpoint);
					mpoint=new mPoint();
					mpointList.add(to.getMpoint());
					System.out.println(to.getMoid());
				}
				//create a new mpoint
				to = new TupleObject();
				to.setMoid(moid);
				coords=new ArrayList<Coordinate>();
				coords.add(new Coordinate(xStart, yStart, timestampToLong(startTime)));
				mpoint = new mPoint();
			}
			
			xStartOld=xStart;
			yStartOld=yStart;
			xEndOld=xEnd;
			yEndOld=yEnd;
			moidOld=moid;
			tripIdOld=tripId;
			startTimeOld=startTime;
			endTimeOld=endTime;
			
			line++;
			
			
			//starttime=nextLine[2]
			//endtime=nextLine[3]
			//moid=nextLine[0]
//			System.out.println(i++);
		}
		fillOtherTupleDetails();
		reader.close();
		System.out.println("Complete");
		return mpointList;
	}
	
	@Test
	public void transformQueryInstants() throws IOException, ParseException
	{
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"queryinstants.csv"));
		PrintWriter pw = new PrintWriter(new File(BerlinMOD+"queryinstantsdouble.csv"));
		String [] nextLine;
		reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			long milliseconds = timestampToLong(nextLine[1]);
			pw.println(nextLine[0]+","+milliseconds);
//			System.out.println(nextLine[0] + nextLine[1] + "etc...");
		}
		reader.close();
		pw.flush();
		pw.close();
	}
	
	public static void transformTrips() throws IOException, ParseException
	{
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"trips.csv"));
		LineBuilder lineBuilder = new LineBuilder(",",":",new File(BerlinMOD+"movement.csv"));
		HashMap<Integer, Integer> hashPrecisionFrequency = new HashMap<Integer,Integer>();
		List<CarDAO> carList = new ArrayList<CarDAO>();
		carList = loadCarsAsList();
		String [] nextLine;
		reader.readNext();
		int i=0;
		TupleObject to = new TupleObject();
//		mPoint mp = new mPoint();
		double xStartOld=0;
		double yStartOld=0;
		double xEndOld=0;
		double yEndOld=0;
		String startTimeOld;
		String endTimeOld = null;
		long moidOld = 0;
		long tripIdOld = 0;
		int line=1;
		int outLineCount=0;
		TupleObject toObject=null;
		mPoint mpoint=null;
		List<Coordinate> coords=null;
		while ((nextLine = reader.readNext()) != null) {
//			Put put = new Put(Bytes.toBytes(nextLine[1])); 
			double xStart = Double.parseDouble(nextLine[4]);
			double yStart = Double.parseDouble(nextLine[5]);
			
			double xEnd = Double.parseDouble(nextLine[6]);
			double yEnd = Double.parseDouble(nextLine[7]);
			
			int moid = Integer.parseInt(nextLine[0]);
			long tripId = Long.parseLong(nextLine[1]);
			String startTime=nextLine[2];
			String endTime=nextLine[3];
			if(moidOld==moid && tripIdOld==tripId){//within a trip of an mpoint
//				if(xStart!=xEndOld || yStart!=yEndOld || startTime.compareTo(endTimeOld)!=0){
//					System.out.println("data is not clean at line "+line);
//				}
				coords.add(new Coordinate(xStart, yStart, timestampToLong(startTime)));
			}
			if(moidOld==moid && tripIdOld!=tripId){//end of the current trip
				//create a new trip in mpoint
				coords.add(new Coordinate(xEndOld, yEndOld, timestampToLong(endTimeOld)));
				if(mpoint==null){
					mpoint = new mPoint();
				}
				mpoint.add(coords);
				coords=new ArrayList<Coordinate>();
				coords.add(new Coordinate(xStart, yStart, timestampToLong(startTime)));
			}
			if(moidOld!=moid){
				if(coords!=null){
					coords.add(new Coordinate(xEndOld, yEndOld, timestampToLong(endTime)));
					to.setMpoint(mpoint);
					mpoint=new mPoint();
					CarDAO carDao = carList.get(to.getMoid()-1);
					lineBuilder.appendCol(Integer.toString(to.getMoid()));
					lineBuilder.appendCol(carDao.getType());
					lineBuilder.appendCol(carDao.getModel());
					lineBuilder.appendCol(carDao.getLicense());
					Bbox bbox = to.getMpoint().getBoundingBox();
					//TODO: print out the frequency of 1-12 digits geohash
					Coordinate coordMin = bbox.getMinCoord();
					Coordinate coordMax = bbox.getMaxCoord();
					GeoHash hashMin = GeoHash.withCharacterPrecision(coordMin.y, coordMin.x, 12);
					String strHashMin = hashMin.toBase32();
					GeoHash hashMax = GeoHash.withCharacterPrecision(coordMax.y, coordMax.x, 12);
					String strHashMax = hashMax.toBase32();
					int x=0;
					for( x=0;x<strHashMin.length();x++){
						if(strHashMax.charAt(x)!=strHashMin.charAt(x)){
							break;
						}
					}
					if(hashPrecisionFrequency.containsKey(++x)){
						int count = hashPrecisionFrequency.get(x);
						hashPrecisionFrequency.put(x, ++count);
					}
					else{
						hashPrecisionFrequency.put(x, 1);
					}
					
					lineBuilder.appendArray(to.getMpoint().toDoubleArray(),"d");
					lineBuilder.writeFlush();
//					(to.getMpoint().toStringArray());
					System.out.println(to.getMoid());
					tuples.add(to);
					if(++outLineCount==10){
//						break;
					}
				}
				//create a new mpoint
				to = new TupleObject();
				to.setMoid(moid);
				coords=new ArrayList<Coordinate>();
				coords.add(new Coordinate(xStart, yStart, timestampToLong(startTime)));
				mpoint = new mPoint();
			}
			
			xStartOld=xStart;
			yStartOld=yStart;
			xEndOld=xEnd;
			yEndOld=yEnd;
			moidOld=moid;
			tripIdOld=tripId;
			startTimeOld=startTime;
			endTimeOld=endTime;
			
			line++;
			
			
			//starttime=nextLine[2]
			//endtime=nextLine[3]
			//moid=nextLine[0]
//			System.out.println(i++);
		}
		fillOtherTupleDetails();
		reader.close();
		lineBuilder.close();
		System.out.println("Complete");
		Set<Entry<Integer, Integer>> set = hashPrecisionFrequency.entrySet();
		Iterator<Entry<Integer,Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<Integer,Integer> e = iter.next();
			System.out.println(e.getKey()+":"+e.getValue());
		}
	}
	
	public static void fillOtherTupleDetails() throws IOException{
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"datamcar.csv"));
		String [] nextLine;
		reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			String moid = nextLine[0]; 
			String licence = nextLine[1];
			String type = nextLine[2]; 
			String model = nextLine[3]; 
			Iterator<TupleObject> i = tuples.iterator();
			while(i.hasNext()){
				TupleObject to = i.next();
				if(to.getMoid()==Integer.parseInt(moid)){
					to.setType(type);
					to.setModel(model);
					to.setLicence(licence);
					break;
				}
			}
		}
	}

	public static void loadTripsPhoenix() throws IOException, ParseException
	{
		table = new HTable(conf, "trips"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"trips.csv"));
		String [] nextLine;
		reader.readNext();
		int i=0;
		TupleObject to = new TupleObject();
		mPoint mp = new mPoint();
		while ((nextLine = reader.readNext()) != null) {
//			Put put = new Put(Bytes.toBytes(nextLine[1])); 
			double xStart = Double.parseDouble(nextLine[4]);
			double longStart = Transformer.toLong(xStart);
			double yStart = Double.parseDouble(nextLine[5]);
			double latStart = Transformer.toLat(yStart);
			
			double xEnd = Double.parseDouble(nextLine[6]);
			double longEnd = Transformer.toLong(xEnd);
			double yEnd = Double.parseDouble(nextLine[7]);
			double latEnd = Transformer.toLat(yEnd);
			
			GeoHash hashStart = GeoHash.withCharacterPrecision(latStart, longStart, 12);
//			System.out.println(xStart+"\t"+yStart+"\t"+longStart+"\t"+latStart+"\t"+hashStart.toBase32());
			GeoHash hashEnd = GeoHash.withCharacterPrecision(latEnd, longEnd, 12);
//			System.out.println(xEnd+"\t"+yEnd+"\t"+longEnd+"\t"+latEnd+"\t"+hashEnd.toBase32());
			
			Put put = new Put(Bytes.toBytes(hashStart.toBase32()+timestampToLong(nextLine[2])));
			
			put.add(Bytes.toBytes("a"), Bytes.toBytes("Moid"),
					Bytes.toBytes(nextLine[0])); 
//			put.add(Bytes.toBytes("a"), Bytes.toBytes("Tstart"),
//					Bytes.toBytes(timestampToLong(nextLine[2]))); 
			put.add(Bytes.toBytes("a"), Bytes.toBytes("Tend"),
					Bytes.toBytes(timestampToLong(nextLine[3])));
			put.add(Bytes.toBytes("a"), Bytes.toBytes("Pend"),
					Bytes.toBytes(hashEnd.toBase32()));
			

			table.put(put); 
//			System.out.println(nextLine[0] + nextLine[1] + "etc...");
			System.out.println(i++);
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
	
	public static long timestampToLong(String timestamp) throws ParseException
	{
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date d=null;
		try {
			d = f.parse(timestamp);
		} catch (ParseException e) {
			if(timestamp.length()==10){
				timestamp=timestamp+" "+"23:00:00.000";
			}else if(timestamp.length()==16){
				timestamp=timestamp+":00.000";
			}else if(timestamp.length()==19){
				timestamp=timestamp+".000";
			}else{
				throw new ParseException("Parse Exception for : "+timestamp, 0);
			}
			d = f.parse(timestamp);
		}
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
	
	public static List<CarDAO> loadCarsAsList() throws IOException
	{
		List<CarDAO> list = new ArrayList<CarDAO>();
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"datamcar.csv"));
		String [] nextLine;
		reader.readNext();
		while ((nextLine = reader.readNext()) != null) {
			CarDAO dao = new CarDAO();
			dao.setMoid(nextLine[0]);
			dao.setLicense(nextLine[1]);
			dao.setType(nextLine[2]);
			dao.setModel(nextLine[3]);
			list.add(dao);
		}
		return list;
	}
	public static void loadTrips() throws IOException, ParseException
	{
		table = new HTable(conf, "trips"); // NewTable Instantiate a new client.
		CSVReader reader = new CSVReader(new FileReader(BerlinMOD+"trips.csv"));
		String [] nextLine;
		reader.readNext();
		int i=0;
		while ((nextLine = reader.readNext()) != null) {
//			Put put = new Put(Bytes.toBytes(nextLine[1])); 
			double xStart = Double.parseDouble(nextLine[4]);
			double longStart = Transformer.toLong(xStart);
			double yStart = Double.parseDouble(nextLine[5]);
			double latStart = Transformer.toLat(yStart);
			
			double xEnd = Double.parseDouble(nextLine[6]);
			double longEnd = Transformer.toLong(xEnd);
			double yEnd = Double.parseDouble(nextLine[7]);
			double latEnd = Transformer.toLat(yEnd);
			
			GeoHash hashStart = GeoHash.withCharacterPrecision(latStart, longStart, 12);
//			System.out.println(xStart+"\t"+yStart+"\t"+longStart+"\t"+latStart+"\t"+hashStart.toBase32());
			GeoHash hashEnd = GeoHash.withCharacterPrecision(latEnd, longEnd, 12);
//			System.out.println(xEnd+"\t"+yEnd+"\t"+longEnd+"\t"+latEnd+"\t"+hashEnd.toBase32());
			
			Put put = new Put(Bytes.toBytes(hashStart.toBase32()+timestampToLong(nextLine[2])));
			
			put.add(Bytes.toBytes("a"), Bytes.toBytes("Moid"),
					Bytes.toBytes(nextLine[0])); 
//			put.add(Bytes.toBytes("a"), Bytes.toBytes("Tstart"),
//					Bytes.toBytes(timestampToLong(nextLine[2]))); 
			put.add(Bytes.toBytes("a"), Bytes.toBytes("Tend"),
					Bytes.toBytes(timestampToLong(nextLine[3])));
			put.add(Bytes.toBytes("a"), Bytes.toBytes("Pend"),
					Bytes.toBytes(hashEnd.toBase32()));
			

			table.put(put); 
//			System.out.println(nextLine[0] + nextLine[1] + "etc...");
			System.out.println(i++);
		}
	}

}
