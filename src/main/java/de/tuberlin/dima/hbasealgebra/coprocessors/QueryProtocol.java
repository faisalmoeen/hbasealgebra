package de.tuberlin.dima.hbasealgebra.coprocessors;

import java.util.List;

import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;

import de.tuberlin.dima.hbasealgebra.dal.Tuple;

public interface QueryProtocol extends CoprocessorProtocol {
	public List<Tuple> feed();
	public List<Tuple> feedWithFilter(Object filter);
}
