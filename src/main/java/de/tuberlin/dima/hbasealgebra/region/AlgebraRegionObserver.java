package de.tuberlin.dima.hbasealgebra.region;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.RegionScanner;

public class AlgebraRegionObserver extends BaseRegionObserver{

	@Override
	public void postScannerClose(
			ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s)
			throws IOException {
		System.out.println("In postScannerClose()");
		super.postScannerClose(e, s);
	}

	@Override
	public boolean postScannerFilterRow(
			ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s,
			byte[] currentRow, int offset, short length, boolean hasMore)
			throws IOException {
		System.out.println("In postScannerFilterRow()");
		return super.postScannerFilterRow(e, s, currentRow, offset, length, hasMore);
	}

	@Override
	public boolean postScannerNext(
			ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s,
			List<Result> results, int limit, boolean hasMore)
			throws IOException {
		System.out.println("In postScannerNext()");
		return super.postScannerNext(e, s, results, limit, hasMore);
	}

	@Override
	public RegionScanner postScannerOpen(
			ObserverContext<RegionCoprocessorEnvironment> e, Scan scan,
			RegionScanner s) throws IOException {
		System.out.println("In postScannerOpen()");
		return super.postScannerOpen(e, scan, s);
	}

	@Override
	public void preGet(ObserverContext<RegionCoprocessorEnvironment> e,
			Get get, List<KeyValue> results) throws IOException {
		System.out.println("In preGet()");
		super.preGet(e, get, results);
	}

	@Override
	public void preGetClosestRowBefore(
			ObserverContext<RegionCoprocessorEnvironment> e, byte[] row,
			byte[] family, Result result) throws IOException {
		System.out.println("In preGetClosestRowBefore()");
		super.preGetClosestRowBefore(e, row, family, result);
	}

	@Override
	public void preScannerClose(
			ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s)
			throws IOException {
		System.out.println("In preScannerClose()");
		super.preScannerClose(e, s);
	}

	@Override
	public boolean preScannerNext(
			ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s,
			List<Result> results, int limit, boolean hasMore)
			throws IOException {
		System.out.println("In preScannerNext()");
		return super.preScannerNext(e, s, results, limit, hasMore);
	}

	@Override
	public RegionScanner preScannerOpen(
			ObserverContext<RegionCoprocessorEnvironment> e, Scan scan,
			RegionScanner s) throws IOException {
		System.out.println("In preScannerOpen()");
		return super.preScannerOpen(e, scan, s);
	}
	
}
