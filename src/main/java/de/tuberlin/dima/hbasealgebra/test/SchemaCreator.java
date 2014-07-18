package de.tuberlin.dima.hbasealgebra.test;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.client.HTable;

public class SchemaCreator {
	public static HTable createTripsTable(HBaseTestingUtility utility){
		try {
			return utility.createTable("trips".getBytes(), "a".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
