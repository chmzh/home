package com.cmz.web.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HBase工具类
 * @author chenmingzhou
 *
 */
public class HBaseUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(HBaseUtil.class);
	
	private final Connection conn;
	public HBaseUtil(Connection conn) {
		this.conn = conn;
	}
	/**
	 * 表是否存在
	 * @param name 表名
	 * @return true:存在,false:不存在
	 * @throws IOException
	 */
	public boolean existsTable(String name) throws IOException{
		Admin admin = conn.getAdmin();
		TableName tableName = TableName.valueOf(name);
		return admin.tableExists(tableName);
	}
	/**
	 * 创建表
	 * @param name 表名
	 * @param familyName 列簇
	 * @throws IOException
	 */
	public void createTable(String name,String familyName) throws IOException{
		Admin admin = conn.getAdmin();
		TableName tableName = TableName.valueOf(name);
		HTableDescriptor descriptor = new HTableDescriptor(tableName);
		HColumnDescriptor family = new HColumnDescriptor(familyName);
		descriptor.addFamily(family);
		admin.createTable(descriptor);
	}
	
	/**
	 * 批量插入
	 * @param name
	 * @param rowKey
	 * @param familyName
	 * @param cols
	 * @param values
	 * @throws IOException
	 */
	public void put(String name,String rowKey,String familyName, String[] cols,String[] values) throws IOException{
		
		String[] cols1 = cols;
		String[] values1 = values;
		if(Strings.isEmpty(name) || Strings.isEmpty(rowKey) || Strings.isEmpty(familyName) || cols1==null || cols1.length==0 || values1 == null || values1.length==0 || cols1.length!=values1.length){
			LOG.error("argument is missing!");
			return;
		}
		
		TableName tableName = TableName.valueOf(name);
		Table table = conn.getTable(tableName);
		Put put = new Put(Bytes.toBytes(rowKey));
		int len = cols.length;
		for(int i=0;i<len;i++){
			put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(cols1[i]), Bytes.toBytes(values1[i]));
		}
		table.put(put);
	}
	
	/**
	 * 单条插入
	 * @param name
	 * @param rowKey
	 * @param familyName
	 * @param col
	 * @param value
	 * @throws IOException 
	 */
	public void put(String name,String rowKey,String familyName, String qualifier,int value) throws IOException{
		if(Strings.isEmpty(name) || Strings.isEmpty(rowKey) || Strings.isEmpty(familyName) || Strings.isEmpty(qualifier)){
			LOG.error("argument is missing!");
			return;
		}
		
		TableName tableName = TableName.valueOf(name);
		Table table = conn.getTable(tableName);
		Put put = new Put(Bytes.toBytes(rowKey));

		put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
		table.put(put);
	}
	
	public void incVal(String name,String rowKey,String familyName,String qualifier,long value) throws IOException{
		if(Strings.isEmpty(name) || Strings.isEmpty(rowKey) || Strings.isEmpty(familyName) || Strings.isEmpty(qualifier)){
			LOG.error("argument is missing!");
			return;
		}
		
		TableName tableName = TableName.valueOf(name);
		Table table = conn.getTable(tableName);
		table.incrementColumnValue(Bytes.toBytes(rowKey), Bytes.toBytes(familyName), Bytes.toBytes(qualifier), value);
	}
	
	/**
	 * 获取某一列的数据
	 * @param name
	 * @param rowKey
	 * @param familyName
	 * @param qualifier
	 * @return
	 * @throws IOException
	 */
	public Result get(String name,String rowKey) throws IOException{
		TableName tableName = TableName.valueOf(name);
		Table table = conn.getTable(tableName);
		Get get = new Get(Bytes.toBytes(rowKey));
		//get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier));
		Result result = table.get(get);
		return result;
	}
	
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
    	configuration.set("hbase.zookeeper.quorum","192.168.23.91,192.168.21.7,192.168.23.207");
    	configuration.set("hbase.zookeeper.property.clientPort","2181");
    	Configuration cfg = HBaseConfiguration.create(configuration);
    	try {
			Connection connection = ConnectionFactory.createConnection(cfg);
			HBaseUtil hBaseUtil = new HBaseUtil(connection);
			if(hBaseUtil.existsTable("test_info")){
				LOG.info("表已经存在");
			}else{
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
