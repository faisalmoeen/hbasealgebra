package de.tuberlin.dima.hbasealgebra.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Bbox;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.DLine;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.MPoint;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.TimeInterval;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.UnitPoint;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.mPoint;

public class MpointTests {

	//double[n]-->{61,bbox,no_3dlines,periods(deftime),{n1,x1,y1,t1,x2,y2,t2.....xn1,yn1,tn1},{n2,x1,y1,t1....,xn2,yn2,tn2}...}
	//periods{start1,end1,start2,end2,start3,end3...start_no3dlines,end_no3dlines}

	double[] mpoint1 = new double[]{61,2,2,9,9,
			2, //no of connected mpoints
			1,5, //periods
			6,7,
			5,//numPoints
			2,2,1  ,3,3,2  ,5,5,3  ,7,7,4   ,10,10,5,
			5,//numPoints
			2,2,6  ,3,3,7  ,5,5,8  ,7,7,9   ,10,10,10
	};

	@Test
	public void testLineString(){
		Coordinate coord1 = new Coordinate(1,2,3);
		Coordinate coord2 = new Coordinate(1,2,4);
		System.out.println(coord1.distance(coord2));
		//		Coordinate[] coord = new Coordinate[]{};
	}

	@Test
	public void testAtFunction(){
		double[] iPoint = mPoint.atInstant(mpoint1, 10);
		if(iPoint!=null){
			for(int i=0;i<iPoint.length;i++){
				System.out.println(iPoint[i]);
			}
		}
	}
	
	@Test
	public void testTrajectory(){
		DLine line = Line.createLine(mPoint.toTrajectory(mpoint1),new GeometryFactory());
		print(line.toDoubleArray());
	}
	
	public void print(double[] darray){
		if(darray==null){
			return;
		}
		for(int i=0;i<darray.length;i++){
			System.out.println(darray[i]);
		}
	}
}
