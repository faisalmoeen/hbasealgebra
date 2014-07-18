package de.tuberlin.dima.hbasealgebra.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.DLine;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Point;

public class LineTests {
	//{4,bb{xmin,ymin,xmax,ymax},numLines,length,numPts,x1,y1,x2,y2,...xn,yn,numPts,x1,y1,x2,y2,...xn,yn,BREAK,...}
	public GeometryFactory factory;
	public static double[] line1 = new double[]{4,2,2,6,6,
		2,//numlines
		4.242,//length
		2,//numPts
		2,2,3,3,//x1,y1,x2,y2
		3,//numPts
		4,4,5,5,6,6};
	public static double[] line2 = new double[]{4,2,2,10,10,
		2,//numlines
		20,//length
		2,//numPts
		2,1,3,2,//x1,y1,x2,y2
		3,//numPts
		9,9,10,10,11,11};
	public static double[] line3 = new double[]{4,1,1,2,2,
		1,//numlines
		1.414,//length
		2,//numPts
		2,1,1,2//x1,y1,x2,y2
		};
	public static double[] line4 = new double[]{4,2,2,10,10, //with one point
		1,//numlines
		20,//length
		1,//numPts
		2,1//x1,y1,x2,y2
		};
	@Before
	public void setup(){
		factory=new GeometryFactory();
	}
	@Test
	public void testLength(){
		double distance = Line.getLength(line1);
		System.out.println(distance);
		Assert.assertEquals(4.242, distance,0.001);
		
		line1[DataTypes.LINEMETA.LENGTH_INDEX]=-1;
		distance = Line.getLength(line1);
		System.out.println(distance);
		Assert.assertFalse(distance==-1);
		Assert.assertEquals(4.242640687119286,distance,0.001);
		
		Assert.assertEquals(Line.createLine(line1,factory).getLength(), distance,0.001);
		System.out.println(Line.createLine(line1,factory).getLength());
	}
	
	@Test
	public void testBoundingBox(){
		Line line = new Line(new Point[]{new Point(2,2),new Point(3,3)}, null, 0);
		System.out.println(line.getLength());
		System.out.println(line.getEnvelope().getCoordinates().length);
		Geometry geom = line.getEnvelope();
		for(int i=0;i<geom.getCoordinates().length;i++){
			System.out.println(geom.getCoordinates()[i].x+":"+geom.getCoordinates()[i].y);
		}
	}
	
	@Test
	public void encodeDecodeLine(){
//		Line line=Line.createLine(line3,1);
//		double len=0;
//		Assert.assertArrayEquals(line3, line.toDoubleArray(), 0.001);
		
		DLine dline = Line.createLine(line1, factory);
		Assert.assertArrayEquals(line1, dline.toDoubleArray(),0.001);
	}
	
	
	@Test
	public void encodeDecodeDLine(){
		DLine line = Line.createLine(line1,factory);
		System.out.println(line.getNumGeometries());
		System.out.println(line.getNumPoints());
		System.out.println(line.getGeometryN(1).getCoordinates()[1]);
		System.out.println("**********************");
		double[] darray=line.toDoubleArray();
		for(int j=0;j<darray.length;j++){
			System.out.println(darray[j]);
		}
		System.out.println("**********************");
	}
	
	@Test
	public void testLineDistance(){
		DLine dline1=Line.createLine(line1,factory);
		DLine dline2=Line.createLine(line3,factory);
		double distance = dline1.distance(dline2); 
		System.out.println(distance);
		Assert.assertEquals(0.707, distance,0.001);
	}
}
