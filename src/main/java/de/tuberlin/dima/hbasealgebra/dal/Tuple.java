package de.tuberlin.dima.hbasealgebra.dal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.HbaseObjectWritable;
import org.apache.hadoop.hbase.util.Bytes;

import ch.hsr.geohash.GeoHash;

public class Tuple extends HbaseObjectWritable{
	private String moId;
	private Date tStart;
	private Date tEnd;
	private double xStart;
	private double yStart;
	private double xEnd;
	private double yEnd;
	private static byte[] colFamily=Bytes.toBytes("a");
	
	
	
	public Tuple(String moId, long tStart, long tEnd, double xStart,
			double yStart, double xEnd, double yEnd) {
		super();
		this.moId = moId;
		this.tStart = new Date(tStart);
		this.tEnd = new Date(tEnd);
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}
	
	public Tuple(){
		
	}
	
	public void print(){
		System.out.println("moId:"+moId+"  tStart:"+tStart+"  tEnd:"+tEnd+"  xStart:"+xStart+"  yStart:"+yStart+" xEnd:"+xEnd+"  yEnd:"+yEnd);
	}
	
	public static Tuple createTuple(Result r){
		String hash = new String(r.getRow());
        GeoHash hashStart = GeoHash.fromGeohashString(hash);
        String Pend = new String(r.getValue(colFamily, Bytes.toBytes("Pend")));
        String moId = new String(r.getValue(colFamily, Bytes.toBytes("Moid")));
        Long tStart = Bytes.toLong(r.getValue(colFamily, Bytes.toBytes("Tstart")));
        Long tEnd = Bytes.toLong(r.getValue(colFamily, Bytes.toBytes("Tend")));
        GeoHash hashEnd = GeoHash.fromGeohashString(Pend);
        Tuple tuple = new Tuple(moId, tStart, tEnd, hashStart.getPoint().getLongitude(), 
        		hashStart.getPoint().getLatitude(), hashEnd.getPoint().getLongitude(), 
        		hashEnd.getPoint().getLatitude());
        return tuple;
	}
	
	public static List<Tuple> createTuples(Result r){
		List<Tuple> tupleList = new ArrayList<Tuple>();
		String hash = new String(r.getRow());
		List<KeyValue> moIdVersions = r.getColumn(colFamily, Bytes.toBytes("Moid"));
		List<KeyValue> pendVersions = r.getColumn(colFamily, Bytes.toBytes("Pend"));
		List<KeyValue> tstartVersions = r.getColumn(colFamily, Bytes.toBytes("Tstart"));
		List<KeyValue> tendVersions = r.getColumn(colFamily, Bytes.toBytes("Tend"));
        GeoHash hashStart = GeoHash.fromGeohashString(hash);
        Iterator<KeyValue> i = moIdVersions.iterator();
        int count=0;
        while(i.hasNext()){
        	KeyValue moidKV = i.next();
        	KeyValue pendKV = pendVersions.get(count);
        	KeyValue tstartKV = tstartVersions.get(count);
        	KeyValue tendKV = tendVersions.get(count);
        	String moid = new String(moidKV.getValue());
        	String Pend = new String(pendKV.getValue());
        	Long tStart = Bytes.toLong(tstartKV.getValue());
            Long tEnd = Bytes.toLong(tendKV.getValue());
            GeoHash hashEnd = GeoHash.fromGeohashString(Pend);
            Tuple tuple = new Tuple(moid, tStart, tEnd, hashStart.getPoint().getLongitude(), 
            		hashStart.getPoint().getLatitude(), hashEnd.getPoint().getLongitude(), 
            		hashEnd.getPoint().getLatitude());
            tupleList.add(tuple);
        }
        return tupleList;
	}
	
	public String getMoId() {
		return moId;
	}
	public void setMoId(String moId) {
		this.moId = moId;
	}
	public Date gettStart() {
		return tStart;
	}
	public void settStart(Date tStart) {
		this.tStart = tStart;
	}
	public Date gettEnd() {
		return tEnd;
	}
	public void settEnd(Date tEnd) {
		this.tEnd = tEnd;
	}
	public double getxStart() {
		return xStart;
	}
	public void setxStart(double xStart) {
		this.xStart = xStart;
	}
	public double getyStart() {
		return yStart;
	}
	public void setyStart(double yStart) {
		this.yStart = yStart;
	}
	public double getxEnd() {
		return xEnd;
	}
	public void setxEnd(double xEnd) {
		this.xEnd = xEnd;
	}
	public double getyEnd() {
		return yEnd;
	}
	public void setyEnd(double yEnd) {
		this.yEnd = yEnd;
	}

	@Override
	public String toString() {
		return new String(moId+":"+tStart.getTime()+":"+tEnd.getTime()+":"+Double.toString(xStart)
				+":"+Double.toString(yStart)+":"+Double.toString(xEnd)+":"+Double.toString(yEnd));
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		moId=in.readUTF();
		tStart=new Date(in.readLong());
		tEnd=new Date(in.readLong());
		xStart=in.readDouble();
		yStart=in.readDouble();
		xEnd=in.readDouble();
		yEnd=in.readDouble();
//		super.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(moId);
		out.writeLong(tStart.getTime());
		out.writeLong(tEnd.getTime());
		out.writeDouble(xStart);
		out.writeDouble(yStart);
		out.writeDouble(xEnd);
		out.writeDouble(yEnd);
//		super.write(out);
	}
	
	
	
	
}
