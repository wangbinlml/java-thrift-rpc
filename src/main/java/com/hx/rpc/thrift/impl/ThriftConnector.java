package com.hx.rpc.thrift.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.utils.PropertiesUtils;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.thrift.IThriftConnector;
import com.hx.rpc.thrift.impl.ThriftConnectorPool.PoolOperationCallBack;

public class ThriftConnector implements IThriftConnector{
	private static Logger logger = Logger.getLogger(ThriftConnector.class);
	private static PropertiesUtils properties = new PropertiesUtils();
	JSONObject connector = JSONObject.parseObject(properties.getPropertyValue("connector"));
	private ThriftConnectorAddressProvider serverAddressProvider;
	private Map<String, GenericObjectPool<RPCInvokeService.Client>> map = new HashMap<String, GenericObjectPool<RPCInvokeService.Client>>();

	private PoolOperationCallBack callback = new PoolOperationCallBack() {
		public void make(RPCInvokeService.Client client) {
			logger.info("create pool");
		}

		public void destroy(RPCInvokeService.Client client) {
			logger.info("destroy pool");
		}
	};
	@Override
	public void init(JSONObject opt) {
	}

	@Override
	public void start() {
		try {
			this.createConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createConnect() throws Exception {
		//只作为服务端时候connector是null
		if(connector == null){
			return ;
		}
		Set<String> set= connector.keySet();
		Iterator<String> iterator= set.iterator();
		while(iterator.hasNext()) {
			String key = (String)iterator.next();
			JSONObject connectorObj = connector.getJSONObject(key);
			String service = connectorObj.getString("service");
			int maxActive = connectorObj.getInteger("maxPoolSize");
			int idleTime = connectorObj.getInteger("idleTimeout");
			String version = connectorObj.getString("version");
			
			serverAddressProvider.init(service, version);
			ThriftConnectorPool clientPool = new ThriftConnectorPool(serverAddressProvider,
					callback);
			GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
			poolConfig.maxActive = maxActive;
			poolConfig.minIdle = 0;
			poolConfig.minEvictableIdleTimeMillis = idleTime;
			poolConfig.timeBetweenEvictionRunsMillis = idleTime / 2L;
			GenericObjectPool<RPCInvokeService.Client> pool = new GenericObjectPool<RPCInvokeService.Client>(clientPool, poolConfig);
			map.put(service, pool);
		}
	}

	@Override
	public Msg invoke(String service_name, String method, Msg msg) {
		try {
			JSONObject connectorObj = connector.getJSONObject(service_name);
			String service = connectorObj.getString("service");
			GenericObjectPool<RPCInvokeService.Client> pool = (GenericObjectPool<RPCInvokeService.Client>) map.get(service);
			RPCInvokeService.Client client = (RPCInvokeService.Client)pool.borrowObject();
			return client.invoke(service, method, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ThriftConnectorAddressProvider getServerAddressProvider() {
		return serverAddressProvider;
	}

	public void setServerAddressProvider(ThriftConnectorAddressProvider serverAddressProvider) {
		this.serverAddressProvider = serverAddressProvider;
	}
	
}
