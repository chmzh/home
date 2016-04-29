package com.cmz.web.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.thrift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.thrift.generated.Hbase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cmz.web.constant.GlobalConstant;

public class HbaseThriftClient {
	private Hbase.Client client = null;
	private TTransport transport = null;
//	private static HbaseThriftClient instance = new HbaseThriftClient();
//	
//	public static HbaseThriftClient getInstance(){
//		return instance;
//	}
	
	public HbaseThriftClient(){
		int timeout = 10000;
		boolean framed = false;
		String host = GlobalConstant.HBASE_HOST;
		int port = 9090;
		transport = new TSocket(host, port, timeout);
		if (framed) {
			transport = new TFramedTransport(transport);
		}
		TProtocol protocol = new TBinaryProtocol(transport);

		client = new Hbase.Client(protocol);
		try {
			transport.open();
		} catch (TTransportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void createTable(String tbName,String column) {
		ColumnDescriptor columnDescriptor = new ColumnDescriptor();
		columnDescriptor.setName(column.getBytes());
		columnDescriptor.setTimeToLive(60*60*24);    //存活时间
		List<ColumnDescriptor> columnFamilies = new ArrayList<ColumnDescriptor>();
		columnFamilies.add(columnDescriptor);
		ByteBuffer tableName = ByteBuffer.wrap(tbName.getBytes());
		try {
			client.createTable(tableName, columnFamilies);
		} catch (TException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if((e.getMessage()).equals("table name already in use")){
				System.out.println(tbName+"表已经存在");
			}
			
		}
	}
}
