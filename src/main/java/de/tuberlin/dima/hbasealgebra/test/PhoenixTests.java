package de.tuberlin.dima.hbasealgebra.test;

import org.apache.phoenix.schema.PDataType;
import org.apache.phoenix.schema.PhoenixArray.PrimitiveDoublePhoenixArray;
import org.apache.phoenix.util.PhoenixRuntime;
import org.junit.Test;

import com.vividsolutions.jts.geom.LineString;

import de.tuberlin.dima.hbasealgebra.temporal.datatypes.iPoint;

public class PhoenixTests {
	@Test
	public void testPhoenix(){
		PhoenixRuntime.main(new String[]{"localhost","/home/faisal/Documents/phoenix-data/query.sql"});
	}
	
	@Test
	public void testPhoenixCustomDataType(){
		PhoenixRuntime.main(new String[]{"localhost","/home/faisal/Documents/phoenix-data/create.sql"});
	}
	
	@Test
	public void testPhoenixDataLoad(){
		PhoenixRuntime.main(new String[]{"-t","MOVEMENT","localhost","/home/faisal/Documents/phoenix-data/movement.csv","-a",":"});
		LineString line = new LineString(null, null);
	}
	
}
