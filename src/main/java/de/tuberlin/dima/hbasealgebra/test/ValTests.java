package de.tuberlin.dima.hbasealgebra.test;

import org.junit.Assert;
import org.junit.Test;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.temporal.datatypes.iPoint;

public class ValTests {

	//double[4]-->{21,x,y,time}
	public static double[] ipoint1= new double[]{DataTypes.IPOINT,2,3,25000};
	public static double[] point1= new double[]{DataTypes.IPOINT,2,3};
	@Test
	public void testVal(){
		double[] point = iPoint.val(ipoint1);
		System.out.println(point.length+"  :  "+point[0]+"  :  "+point[1]+"  :  "+point[2]);
		Assert.assertArrayEquals(point1, point, 0);
	}
}
