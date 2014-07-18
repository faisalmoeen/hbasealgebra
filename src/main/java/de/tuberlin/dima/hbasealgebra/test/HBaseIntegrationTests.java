package de.tuberlin.dima.hbasealgebra.test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import de.tuberlin.dima.hbasealgebra.api.Client;
import de.tuberlin.dima.hbasealgebra.dal.Tuple;
import de.tuberlin.dima.hbasealgebra.dataload.MiniClusterLoad;

public class HBaseIntegrationTests {
	private static HBaseTestingUtility utility;
	byte[] CF = "CF".getBytes();
	byte[] CQ1 = "CQ-1".getBytes();
	byte[] CQ2 = "CQ-2".getBytes();
	HTable trips;

	@Before
	public void setup() throws Exception {
		utility = new HBaseTestingUtility();
		utility.getConfiguration().set("dfs.datanode.data.dir.perm", "775");
		utility.getConfiguration().set("hbase.coprocessor.region.classes", "de.tuberlin.dima.hbasealgebra.coprocessors.QueryProtocolImpl");
		utility.getConfiguration().set("hbase.rpc.timeout", "500000");
		
//		utility.getConfiguration().set(name, value);
		utility.startMiniCluster();
		trips = SchemaCreator.createTripsTable(utility);
		MiniClusterLoad.loadTrips(trips, 4);
	}

	@Test
	public void testInsert() throws Exception {
		HTableInterface table = utility.createTable(Bytes.toBytes("MyTest"),
				Bytes.toBytes("CF"));
		HBaseTestObj obj = new HBaseTestObj();
		obj.setRowKey("ROWKEY-1");
		obj.setData1("DATA-1");
		obj.setData2("DATA-2");
		MyHBaseDAO.insertRecord(table, obj);
		Get get = new Get(Bytes.toBytes(obj.getRowKey()));
		get.addColumn(CF, CQ1);
		Result result = table.get(get);
		assertEquals(Bytes.toString(result.getRow()), obj.getRowKey());
		assertEquals(Bytes.toString(result.value()), obj.getData1());
	}
	
	@Test
	public void testDistributedFeed(){
		HTablePool pool = new HTablePool();
		Client cl = new Client(pool);
		try {
			List<Tuple> tupleList = cl.distributedFeed(trips);
			Iterator<Tuple> i = tupleList.iterator();
			while(i.hasNext()){
				System.out.println(i.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testingFilePerm(){
		File workingDirectory = new File("./");
        Configuration conf = new Configuration();
        System.setProperty("test.build.data", workingDirectory.getAbsolutePath());
        conf.set("test.build.data", new File(workingDirectory, "zookeeper").getAbsolutePath());
        conf.set("fs.default.name", "file:///");
        conf.set("zookeeper.session.timeout", "180000");
        conf.set("hbase.zookeeper.peerport", "2888");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.addResource(new Path("conf/hbase-site1.xml"));
        File masterDir;
        org.apache.log4j.Logger logger = null;
        try
        {
            masterDir = new File(workingDirectory, "hbase");
            conf.set(HConstants.HBASE_DIR, masterDir.toURI().toURL().toString());
        }
        catch (MalformedURLException e1)
        {
            logger.error(e1.getMessage());
        }

        Configuration hbaseConf = HBaseConfiguration.create(conf);
        utility = new HBaseTestingUtility(hbaseConf);

        // Change permission for dfs.data.dir, please refer
        // https://issues.apache.org/jira/browse/HBASE-5711 for more details.
        try
        {
            Process process = Runtime.getRuntime().exec("/bin/sh -c umask");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int rc = process.waitFor();
            if (rc == 0)
            {
//                String umask = br.readLine();
//
//                int umaskBits = Integer.parseInt(umask, 8);
//                int permBits = 0777 & ~umaskBits;
//                String perms = Integer.toString(permBits, 8);
            	String perms="775";
//                logger.info("Setting dfs.datanode.data.dir.perm to " + perms);
                utility.getConfiguration().set("dfs.datanode.data.dir.perm", perms);
                utility.startMiniCluster();
            }
            else
            {
                logger.warn("Failed running umask command in a shell, nonzero return value");
            }
        }
        catch (Exception e)
        {
            // ignore errors, we might not be running on POSIX, or "sh" might
            // not be on the path
            logger.warn("Couldn't get umask", e);
        }
//        if (!checkIfServerRunning())
//        {
//            hTablePool = new HTablePool(conf, 1);
//            try
//            {
//                zkCluster = new MiniZooKeeperCluster(conf);
//                zkCluster.setDefaultClientPort(2181);
//                zkCluster.setTickTime(18000);
//                zkDir = new File(utility.getClusterTestDir().toString());
//                zkCluster.startup(zkDir);
//                utility.setZkCluster(zkCluster);
//                utility.startMiniCluster();
//                utility.getHBaseCluster().startMaster();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//                throw new RuntimeException(e);
//            }
//        }
	}
}