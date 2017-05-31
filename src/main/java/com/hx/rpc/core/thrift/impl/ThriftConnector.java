package com.hx.rpc.core.thrift.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.thrift.IThriftConnector;
import com.hx.rpc.core.thrift.impl.ThriftConnectorPool.PoolOperationCallBack;
import com.hx.rpc.core.utils.PropertiesUtils;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;

public class ThriftConnector implements BeanFactoryAware, IThriftConnector {
	private static Logger logger = Logger.getLogger(ThriftConnector.class);
	private static PropertiesUtils properties = new PropertiesUtils();
	private ThriftConnectorAddressProvider serverAddressProvider;
	private BeanFactory factory;
	private JSONArray connectorList = JSONArray.parseArray(properties.getPropertyValue("connector"));
	private Map<String, GenericObjectPool<RPCInvokeService.Client>> map = new HashMap<String, GenericObjectPool<RPCInvokeService.Client>>();
	private Map<String, JSONObject> serviceMap = new HashMap<String, JSONObject>();
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
		// 只作为服务端时候connector是null
		if (connectorList == null) {
			return;
		}
		for (int i = 0; i < connectorList.size(); i++) {
			JSONObject connectorObj = connectorList.getJSONObject(i);
			String key = connectorObj.getString("name");
			serviceMap.put(key, connectorObj);
			String service = connectorObj.getString("service");
			int maxActive = connectorObj.getInteger("maxPoolSize");
			int idleTime = connectorObj.getInteger("idleTimeout");
			String version = connectorObj.getString("version");

			// TODO
			// 为什么不能注入，注入后变成单实例（http://blog.csdn.net/gladyoucame/article/details/8245036）
			serverAddressProvider = (ThriftConnectorAddressProvider) factory.getBean("serverAddressProvider");
			serverAddressProvider.init(service, version);
			ThriftConnectorPool clientPool = new ThriftConnectorPool(serverAddressProvider, callback);
			GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
			poolConfig.maxActive = maxActive;
			poolConfig.minIdle = 0;
			poolConfig.minEvictableIdleTimeMillis = idleTime;
			poolConfig.timeBetweenEvictionRunsMillis = idleTime / 2L;
			GenericObjectPool<RPCInvokeService.Client> pool = new GenericObjectPool<RPCInvokeService.Client>(clientPool,
					poolConfig);
			map.put(service, pool);
		}
	}

	@Override
	public Msg invoke(String service_name, String method, Msg msg) {
		try {
			JSONObject connectorObj = serviceMap.get(service_name);
			String service = connectorObj.getString("service");
			GenericObjectPool<RPCInvokeService.Client> pool = (GenericObjectPool<RPCInvokeService.Client>) map
					.get(service);
			RPCInvokeService.Client client = (RPCInvokeService.Client) pool.borrowObject();
			Msg msg2 = client.invoke(service_name, method, msg);
			pool.returnObject(client);
			return msg2;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ThriftConnectorAddressProvider getServerAddressProvider() {
		return serverAddressProvider;
	}

	/*
	 * public void setServerAddressProvider(ThriftConnectorAddressProvider
	 * serverAddressProvider) { this.serverAddressProvider =
	 * serverAddressProvider; }
	 */

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		factory = beanFactory;
	}

}
