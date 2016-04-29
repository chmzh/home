package com.cmz.web.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlumeHttpClient {
	private static final Logger logger = LoggerFactory.getLogger(FlumeHttpClient.class);
	private HttpPost method = null;
	private long startTime = 0L;
	private long endTime = 0L;
	private int status = 0;
	private String apiURL = "http://slave3:9922";
	private String HOSTNAME = "slave3";
	private static FileUtil fileUtil = new FileUtil();
	private static String[] hosts = new String[]{"slave1","slave2","slave3"};
	
	private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	private static CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
	public FlumeHttpClient(){
		int index = RandomUtil.randInt(0, 2);
		apiURL = "http://"+hosts[index]+":9922";
		
		method = new HttpPost(apiURL);
		
	}
	
	/**
	 * 接口地址
	 * 
	 * @param url
	 */
	public FlumeHttpClient(String host) {
		if (!host.equals("")) {
			apiURL = "http://" + host + ":9922";
			HOSTNAME = host;
		}else{
			int index = RandomUtil.randInt(0, 2);
			apiURL = "http://"+hosts[index]+":9922";
		}
		httpClient = HttpClients.createDefault();
		method = new HttpPost(apiURL);
	}

//	public void close() {
//		try {
//			httpClient.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * 调用 API
	 * 
	 * @param parameters
	 * @return
	 */
	private int post(String parameters) {

		// logger.info("parameters:" + parameters);

		if (method != null & parameters != null && !"".equals(parameters.trim())) {
			try {

				// 建立一个NameValuePair数组，用于存储欲传送的参数
				method.addHeader("Content-type", "application/json; charset=utf-8");
				method.setHeader("Accept", "application/json");
				method.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));
				startTime = System.currentTimeMillis();

				CloseableHttpResponse response = httpClient.execute(method);

				endTime = System.currentTimeMillis();
				int statusCode = response.getStatusLine().getStatusCode();

				// logger.info("statusCode:" + statusCode);
				// logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
				if (statusCode != HttpStatus.SC_OK) {
					// logger.error("Method failed:" +
					// response.getStatusLine());
					logger.error("=====错误状态码====="+statusCode+"====="+parameters);
					status = 1;
				}

				// Read the response body
				HttpEntity entity = response.getEntity();
				String respContent = EntityUtils.toString(entity, "utf-8").trim();
				
				//logger.info("flume response======="+respContent);
				
				// method.abort();

				response.close();
			} catch (IOException e) {
				// 网络错误
				status = 3;
				if (status != 0) {
					// 重发 记录到本地

					logger.error(e.getLocalizedMessage()+"======="+parameters);
					

					/*
					 * if(HOSTNAME.equals("slave3")){ FlumeHttpClient
					 * flumeHttpClient = new FlumeHttpClient("slave2");
					 * flumeHttpClient.post(parameters);
					 * flumeHttpClient.close(); }else
					 * if(HOSTNAME.equals("slave2")){ FlumeHttpClient
					 * flumeHttpClient = new FlumeHttpClient("slave1");
					 * flumeHttpClient.post(parameters);
					 * flumeHttpClient.close(); }
					 */
				}
			}

		}

		return status;
	}

	public int postJson(String json) {
		return post(json);
	}

	/**
	 * 0.成功 1.执行方法失败 2.协议错误 3.网络错误
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

}
