package de.tuberlin.dima.hbasealgebra.coprocessors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;
import org.apache.hadoop.hbase.regionserver.InternalScanner;

import de.tuberlin.dima.hbasealgebra.dal.Tuple;
import de.tuberlin.dima.hbasealgebra.util.TupleMaker;

public class QueryProtocolImpl extends BaseEndpointCoprocessor implements
		QueryProtocol {

	public List<Tuple> feed() {
		System.out.println("Inside feed for endpoint");
		Scan scan = new Scan();
		scan.setMaxVersions(1);
		RegionCoprocessorEnvironment env = (RegionCoprocessorEnvironment)getEnvironment();
		List<Tuple> tupleList = new ArrayList<Tuple>();
		try {
			InternalScanner scanner = env.getRegion().getScanner(scan);
			long sum = 0;
			List<KeyValue> results = new ArrayList<KeyValue>();
			boolean hasMore = false;
			do {
				hasMore = scanner.next(results);
				sum += results.size();
				tupleList.add(TupleMaker.fromKeyValues(results));
				results.clear();
			} while (hasMore);
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tupleList;
	}

	public List<Tuple> feedWithFilter(Object filter) {
		System.out.println("Inside feed with filter endpoint");
		return null;
	}

}
