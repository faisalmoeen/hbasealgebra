package de.tuberlin.dima.hbasealgebra.spatial.datatypes;

import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.jts.JtsLineString;
import de.tuberlin.dima.hbasealgebra.jts.JtsMultiLineString;
import de.tuberlin.dima.hbasealgebra.util.Transformer;

public class Line extends LineString{

	private double length;
	private int numLines;
	private class bbox{
		public bbox(Point[] points){
			this.points=points;
		}
		public bbox(){}
		public void set(Geometry geom){
			if(geom.getNumPoints()>4){
				points[0]=geom.getCoordinates()[0];
				points[1]=geom.getCoordinates()[2];
			}
			else if(geom.getNumPoints()==2){
				points[0]=geom.getCoordinates()[0];
				points[1]=geom.getCoordinates()[1];
			}
			else if(geom.getNumPoints()==1){
				points[0]=geom.getCoordinates()[0];
				points[1]=geom.getCoordinates()[0];
			}
		}
		private Coordinate[] points=new Coordinate[2];
	};
	private bbox bBox;
	public Line(Coordinate[] points, PrecisionModel precisionModel, int SRID) {
		super(points, precisionModel, SRID);
	}
	
	public Line(Coordinate[] points, PrecisionModel precisionModel, int SRID, int length, Point[] bboxPoints, int numLines) {
		super(points, precisionModel, SRID);
		this.length=length;
		this.bBox=new bbox(bboxPoints);
		this.numLines=numLines;
	}

	public Line(Coordinate[] points){
		super(points,null,0);
	}
	@Override
	public double getLength() {
		if(this.length!=-1){
			this.length = super.getLength();
		}
		return this.length;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Line createLine(double[] darray,int lineIndex){
		if(getNumLines(darray)<lineIndex){
			return null;
		}
		int ptr=DataTypes.LINEMETA.START_INDEX;
		int numPoints=0;
		for(int i=0;i<lineIndex-1;i++){
			numPoints=(int)darray[ptr];
			ptr=ptr+(numPoints*2)+1;
		}
		numPoints=(int)darray[ptr];
		Point[] points=null;
		boolean duplicate=false;
		if(numPoints==1){//if only one point in line, duplicate it
			duplicate=true;
			points = new Point[numPoints+1];
		}
		else{
			points = new Point[numPoints];
		}
		int index=0;
		int start=ptr+1;
		int end=ptr+(numPoints*2);
		for(int i=start;i<=end;i++){
			points[index]=new Point(darray[i],darray[++i]);
			index++;
		}
		if(duplicate){
			points[index]=new Point(points[index-1].x,points[index-1].y);
		}
		return new Line(points,null,0);
	}
	
	
	
	public static double getLength(double[] darray){
		double len = darray[DataTypes.LINEMETA.LENGTH_INDEX];
		if(len!=-1){
			return len;
		}
		else{
			//calculate new length
			len=0;
			Line line=null;
			for(int i=1;i<=getNumLines(darray);i++){
				line=createLine(darray, i);
				len+=line.getLength();
			}
			return len;
		}
	}
	public static DLine createLine(double[] darray,GeometryFactory factory){
		Line[] lines=createLines(darray);
		DLine dline = new DLine(lines, factory);
		return dline;
	}
	public static Line[] createLines(double[] darray){
		Line[] lines=new Line[getNumLines(darray)];
		for(int i=1;i<=getNumLines(darray);i++){
			lines[i-1]=createLine(darray, i);
		}
		return lines;
	}
		
	public static int getNumLines(double[] darray){
		return (int)darray[DataTypes.LINEMETA.NUMLINE_INDEX];
	}
	
	
	public double[] toDoubleArray(){
		int requiredSize = DataTypes.LINEMETA.START_INDEX+(this.getNumPoints()*2)+1; //+1 for the size of line
		double[] darray=new double[requiredSize];
		darray=fillMetaData(darray);
		darray=fillLine(darray);
		return darray;
	}
	
	public double[] fillLine(double[] darray){
		darray[DataTypes.LINEMETA.START_INDEX]=this.getNumPoints();
		int index = DataTypes.LINEMETA.START_INDEX;
		for(int i=0; i<this.getNumPoints(); i++){
			darray[++index]=this.getCoordinates()[i].x;
			darray[++index]=this.getCoordinates()[i].y;
		}
		return darray;
	}
	private double[] fillMetaData(double[] darray){
		darray[0]=DataTypes.LINE;
		if(this.bBox==null){
			this.bBox=new bbox();
			this.bBox.set(this.getEnvelope());
		}
		int index=0;
		for(int i=0;i<2;i++){
			darray[DataTypes.LINEMETA.BBOX_INDEX+index]=this.bBox.points[i].x;
			darray[DataTypes.LINEMETA.BBOX_INDEX+index+1]=this.bBox.points[i].y;
			index+=2;
		}
		darray[DataTypes.LINEMETA.NUMLINE_INDEX]=1;
		darray[DataTypes.LINEMETA.LENGTH_INDEX]=this.getLength();
		return darray;
	}
}
