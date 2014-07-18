package de.tuberlin.dima.hbasealgebra.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.util.Bytes;

import ch.hsr.geohash.GeoHash;
import de.tuberlin.dima.hbasealgebra.coprocessors.QueryProtocol;
import de.tuberlin.dima.hbasealgebra.coprocessors.QueryProtocolImpl;
import de.tuberlin.dima.hbasealgebra.dal.Tuple;
import de.tuberlin.dima.hbasealgebra.util.Transformer;

public class Client {
	@SuppressWarnings("deprecation")
	static HTablePool pool;
	public Client(HTablePool pool){
		this.pool=pool;
	}
	public Iterator<Tuple> feed(String tableName, byte[] colFamily) throws IOException{
		Scan scan = new Scan();
	    scan.setMaxVersions(1);
	    scan.setCaching(2000);
	    List<Tuple> tupleList = new ArrayList<Tuple>();

	    HTableInterface table = pool.getTable(tableName);

	    int cnt = 0;
	    ResultScanner scanner = table.getScanner(scan);
	    for (Result r : scanner) {
	        Tuple tuple = Tuple.createTuple(r);
	        tupleList.add(tuple);
	        cnt++;
	      }
	    System.out.println(cnt);
	    return tupleList.iterator();
	}
	
	public List<Tuple> get(String tableName, byte[] rowKey, byte[] colFamily,int versions) throws IOException{
		Get getObj = new Get(rowKey);
		getObj.setMaxVersions(versions);
		getObj.addFamily(colFamily);
		HTableInterface table = pool.getTable(tableName);
		Result result = table.get(getObj);
		return Tuple.createTuples(result);
	}
	
	public Tuple get(String tableName, byte[] rowKey, byte[] colFamily) throws IOException{
		Get getObj = new Get(rowKey);
		getObj.setMaxVersions();
		getObj.addFamily(colFamily);
		HTableInterface table = pool.getTable(tableName);
		Result result = table.get(getObj);
		return Tuple.createTuple(result);
	}
	
	public void distributedFeed() throws IOException, Throwable{
		HTableInterface table = pool.getTable("trips");
		distributedFeed(table);
	}
	public List<Tuple> distributedFeed(HTableInterface table) throws IOException, Throwable{
		
		final byte[] startKey= Bytes.toBytes((GeoHash.withCharacterPrecision(Transformer.getBottomLat(), Transformer.getLeftLong(), 12)).toBase32());
		final byte[] endKey= Bytes.toBytes((GeoHash.withCharacterPrecision(Transformer.getTopLat(), Transformer.getRightLong(), 12)).toBase32());
		Batch.Call<QueryProtocol, List<Tuple>> callable =
				new Batch.Call<QueryProtocol, List<Tuple>>() {

					public List<Tuple> call(QueryProtocol instance)
							throws IOException {
						return instance.feed();
					}
				};
				Map<byte[], List<Tuple>> result= table.coprocessorExec(QueryProtocol.class, Bytes.toBytes("0000000000000000000000000"), Bytes.toBytes("zzzzzzzzzzzzzzzzzzzzzzzzz"), callable);
				System.out.println(result.size());
				Iterator<List<Tuple>> i = result.values().iterator();
				return (i.hasNext()?i.next():null);
	}
	
	public static void main(String[] args) throws Throwable{
		HTablePool pool = new HTablePool();
		Client cl = new Client(pool);
//		Iterator<Tuple> i = cl.feed("trips", Bytes.toBytes("a"));
//		while(i.hasNext()){
//			System.out.println(i.next());
//		}
		//**************************************
//		Tuple tuple = cl.get("trips", Bytes.toBytes("u33fb1d3hkj3"), Bytes.toBytes("a"));
//		tuple.print();
		//**************************************
//		List<Tuple> tupleList = cl.get("trips", Bytes.toBytes("u33fb1d3hkj3"), Bytes.toBytes("a"),Integer.MAX_VALUE);
//		Iterator<Tuple> i = tupleList.iterator();
//		while(i.hasNext()){
//			Tuple tuple = i.next();
//			tuple.print();
//		}
		//**************************************
		
		cl.distributedFeed();
	}
}
