package com.cmz.web.zookeeper;

public interface Constant {
	 
    String ZK_CONNECTION_STRING = "119.29.164.47:2181,119.29.1.219:2181,119.29.155.236:2181,119.29.153.168:2181,119.29.59.232:2181";
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/dv_web_service";
    String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";
}
