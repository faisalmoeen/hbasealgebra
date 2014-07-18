package de.tuberlin.dima.hbasealgebra.temporal.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;

import de.tuberlin.dima.hbasealgebra.datatypes.DataTypes;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Bbox;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.DLine;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Line;
import de.tuberlin.dima.hbasealgebra.spatial.datatypes.Curve;

public class mPoint {
	
	//double[n]-->{61,bbox,no_components,periods(deftime),{n1,x1,y1,t1,x2,y2,t2.....xn1,yn1,tn1},{n2,x1,y1,t1....,xn2,yn2,tn2}...}
	//periods{start1,end1,start2,end2,start3,end3...start_no3dlines,end_no3dlines}
	
	public List<List<Coordinate>> pointCollection=null;
	public mPoint(){
		
	}
	public mPoint(double[] darray){
		if(!mPoint.isValid(darray)){
			System.out.println("invalid mpoint");
			return;
		}
		pointCollection = new ArrayList<List<Coordinate>>();
		int numComponents = (int)darray[DataTypes.MPOINT_META.NO_COMPONENTS_INDEX];
		int index = DataTypes.MPOINT_META.PERIODS_INDEX + 2*numComponents; //index where mpoints start
		Coordinate[] coords = null;
		Line[] lines = new Line[numComponents];
		int lineIndex=0;
		for(int i=0;i<numComponents;i++){
			int numPts=(int)darray[index++];
			coords = new Coordinate[numPts];
			int coordIndex=0;
			List<Coordinate> list = new ArrayList<Coordinate>();
			for(int j=0;j<numPts;j++){
				list.add(new Coordinate(darray[index++], darray[index++],darray[index++]));
			}
			pointCollection.add(list);
		}
		
	}
	
	public boolean equals(Object o){
		if(o instanceof mPoint) {
			mPoint t = (mPoint) o;
			if(this.pointCollection.equals(t.pointCollection)){
				return true;
			}
		}
		return false;
	}
	public void add(List<Coordinate> coords){
		if(pointCollection==null){
			pointCollection=new ArrayList<List<Coordinate>>();
		}
		pointCollection.add(coords);
	}
	

	public Bbox getBoundingBox(){
		Line[] lines = new Line[pointCollection.size()];
		for(int i=0;i<pointCollection.size();i++){
			lines[i] = new Line(pointCollection.get(i).toArray(new Coordinate[0]));
		}
		MultiLineString multiLine = new MultiLineString(lines, new GeometryFactory());
		Envelope envelop = (Envelope)multiLine.getEnvelopeInternal();
		Bbox bbox = new Bbox(envelop);
		return bbox;
	}
	public double[] toDoubleArray(){
		Line[] lines = new Line[pointCollection.size()];
		for(int i=0;i<pointCollection.size();i++){
			lines[i] = new Line(pointCollection.get(i).toArray(new Coordinate[0]));
		}
		MultiLineString multiLine = new MultiLineString(lines, new GeometryFactory());
		Envelope envelop = (Envelope)multiLine.getEnvelopeInternal();
		Bbox bbox = new Bbox(envelop);
		Double[] bboxArray = bbox.toDoubleArray();
		int noComponents = pointCollection.size();
		//finding periods
		double[] startTimes = new double[noComponents],endTimes = new double[noComponents]; 
		for(int i=0;i<lines.length; i++){
			int size = lines[i].getCoordinates().length;
			startTimes[i]=lines[i].getCoordinates()[0].z;
			endTimes[i] = lines[i].getCoordinates()[size-1].z;
		}
		int noPoints=0;
		for(int i=0;i<lines.length;i++){
			noPoints+=lines[i].getCoordinates().length;
		}
		
		int finalSize = 1+4+1+startTimes.length+endTimes.length+lines.length+(noPoints*3);
		double[] outMpoint = new double[finalSize];
		outMpoint[0] = DataTypes.MPOINT;
		outMpoint[DataTypes.MPOINT_META.BBOX_INDEX]=bboxArray[0];
		outMpoint[DataTypes.MPOINT_META.BBOX_INDEX+1]=bboxArray[1];
		outMpoint[DataTypes.MPOINT_META.BBOX_INDEX+2]=bboxArray[2];
		outMpoint[DataTypes.MPOINT_META.BBOX_INDEX+3]=bboxArray[3];
		outMpoint[DataTypes.MPOINT_META.NO_COMPONENTS_INDEX]=noComponents;
		int i=0;
		for(i=0;i<startTimes.length;i++){
			outMpoint[DataTypes.MPOINT_META.PERIODS_INDEX+(i*2)]=startTimes[i];
			outMpoint[DataTypes.MPOINT_META.PERIODS_INDEX+(i*2+1)]=endTimes[i];
		}
		i--;
		int start = DataTypes.MPOINT_META.PERIODS_INDEX+(i*2+1)+1;
		for(i=0;i<lines.length;i++){
			outMpoint[start++]=lines[i].getCoordinates().length;
			for(int j=0;j<lines[i].getCoordinates().length;j++){
				outMpoint[start++]=lines[i].getCoordinates()[j].x;
				outMpoint[start++]=lines[i].getCoordinates()[j].y;
				outMpoint[start++]=lines[i].getCoordinates()[j].z;
			}
		}
		return outMpoint;
	}
	public String toString(){
		double[] darray = this.toDoubleArray();
		String str="";
		for(int i=0;i<darray.length;i++){
			str=str+","+Double.toString(darray[i]);
		}
		return str.substring(1);
	}
	public String[] toStringArray(){
		double[] darray = this.toDoubleArray();
		List<String> strList = new ArrayList<String>();
		String str="";
		for(int i=0;i<darray.length;i++){
			strList.add(Double.toString(darray[i]));
		}
		return strList.toArray(new String[0]);
	}
	public static boolean present(double[] inputMpoint, double time){
		if(!isValid(inputMpoint))
			return false;
		int numComponents=(int)inputMpoint[DataTypes.MPOINT_META.NO_COMPONENTS_INDEX];
		TimeInterval interval=null;
		boolean inside=false;
		int index=DataTypes.MPOINT_META.PERIODS_INDEX;
		int x;
		for(x=0;x<numComponents;x++){
			interval = new TimeInterval(inputMpoint, index);
			if(interval.inside((float)time)){
				inside=true;
				break;
			}
			index+=2;
		}
		if(!inside){
			//return null float array;
			return false;
		}
		return true;
	}
	
	public static int presentIndex(double[] inputMpoint, double time){
		if(!isValid(inputMpoint))
			return -1;
		int numComponents=(int)inputMpoint[DataTypes.MPOINT_META.NO_COMPONENTS_INDEX];
		TimeInterval interval=null;
		boolean inside=false;
		int index=DataTypes.MPOINT_META.PERIODS_INDEX;
		int x;
		for(x=0;x<numComponents;x++){
			interval = new TimeInterval(inputMpoint, index);
			if(interval.inside((float)time)){
				inside=true;
				break;
			}
			index+=2;
		}
		if(!inside){
			//return null float array;
			return -1;
		}
		return x;
	}
	
	public static boolean isValid(double[] mpoint){
		int numComponents=0;
		if(mpoint==null ||
				mpoint.length<=DataTypes.MPOINT_META.NO_COMPONENTS_INDEX ||
				mpoint[numComponents=DataTypes.MPOINT_META.NO_COMPONENTS_INDEX]<=0){
			return false;
		}
		if(mpoint.length<=DataTypes.MPOINT_META.PERIODS_INDEX+numComponents*2){
			return false;
		}
		return true;
	}
	
	public static double[] atInstant(double[] mpoint1,double time){
		int numComponents=(int)mpoint1[DataTypes.MPOINT_META.NO_COMPONENTS_INDEX];
//		Bbox bbox = new Bbox(mpoint1, DataTypes.MPOINT_META.BBOX_INDEX);
		if(!isValid(mpoint1)){
			return null;
		}
		
		int presenceIndex=presentIndex(mpoint1, time);
		if(presenceIndex<0){
			return null;
		}
		
		//find the index of the corresponding mpoint
		int startOfData=DataTypes.MPOINT_META.PERIODS_INDEX + numComponents*2;
		int index=startOfData;
		int numPts = (int)mpoint1[index];
		for(int k=1;k<=presenceIndex;k++){
			index=index+ (numPts*3)+ 1;
			numPts = (int)mpoint1[index];
		}
		MPoint mpoint_start=null;
		MPoint mpoint_end=null;
		UnitPoint upoint=null;
		for(int i=0;i<numComponents;i++){
			int numPoints=(int)mpoint1[index]-1;
			index++;
			for(int j=0;j<numPoints;j++){
				if(time>=mpoint1[index+2] && time<mpoint1[index+5]){
					if(time<( (mpoint1[index+2]+mpoint1[index+5])/2 )){
						mpoint_start=new MPoint((float)mpoint1[index], (float)mpoint1[index+1]); //make left point as reference
						mpoint_end=new MPoint((float)(mpoint1[index]+mpoint1[index+3])/2, 
								(float)(mpoint1[index+1]+mpoint1[index+4])/2 );
						upoint = new UnitPoint(
								new TimeInterval((float)mpoint1[index+2],(float) (mpoint1[index+2]+mpoint1[index+5])/2), 
								mpoint_start,mpoint_end);
						MPoint result = upoint.getValue((float)time);
//						System.out.println(result);
						return new double[]{DataTypes.IPOINT,result.x,result.y,time};
					}
					else{
						mpoint_start=new MPoint((float)(mpoint1[index]+mpoint1[index+3])/2, 
								(float)(mpoint1[index+1]+mpoint1[index+4])/2 );
						mpoint_end=new MPoint((float)mpoint1[index+3], (float)mpoint1[index+4]);
						upoint = new UnitPoint(
								new TimeInterval((float) (mpoint1[index+2]+mpoint1[index+5])/2, (float)mpoint1[index+5]), 
								mpoint_start,mpoint_end);
						MPoint result = upoint.getValue((float)time);
//						System.out.println(result);
						return new double[]{DataTypes.IPOINT,result.x,result.y,time};
					}
				}
				index+=3;
			}
		}
		return null;
	}
	
	public static double[] toTrajectory(double[] mpoint){
		if(!mPoint.isValid(mpoint)){
			System.out.println("invalid mpoint");
			return null;
		}
		int numComponents = (int)mpoint[DataTypes.MPOINT_META.NO_COMPONENTS_INDEX];
		int index = DataTypes.MPOINT_META.PERIODS_INDEX + 2*numComponents; //index where mpoints start
		Coordinate[] coords = null;
		Line[] lines = new Line[numComponents];
		int lineIndex=0;
		for(int i=0;i<numComponents;i++){
			int numPts=(int)mpoint[index++];
			coords = new Coordinate[numPts];
			int coordIndex=0;
			for(int j=0;j<numPts;j++){
				coords[coordIndex++]=new Coordinate(mpoint[index], mpoint[index+1]);
				index+=3;
			}
			lines[lineIndex++]= new Line(coords);
		}
		DLine dline = new DLine(lines, new GeometryFactory());
		double[] result = dline.toDoubleArray();
		return result;
	}
	
}
