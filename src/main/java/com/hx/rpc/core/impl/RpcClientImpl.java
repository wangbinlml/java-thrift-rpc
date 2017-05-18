package com.hx.rpc.core.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TException;
import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpcClient;
import com.hx.rpc.core.utils.PropertiesUtils;
import com.hx.rpc.core.utils.RPCBeanFactory;
import com.hx.rpc.gen.InvalidRequestException;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.gen.TimedOutException;
import com.hx.rpc.thrift.impl.ThriftConnector;
import com.hx.rpc.thrift.impl.ThriftServiceClientProxyFactory;

public class RpcClientImpl extends ThriftConnector implements IRpcClient {
	private static PropertiesUtils properties = new PropertiesUtils();
	RPCInvokeService.Iface serivce;
	RPCInvokeService.AsyncIface asycSerivce;
	ThriftServiceClientProxyFactory cs;
	Map map = new HashMap();

	public ThriftServiceClientProxyFactory getCs() {
		return cs;
	}

	public void setCs(ThriftServiceClientProxyFactory cs) {
		this.cs = cs;
	}

	@Override
	public void init(String config, JSONObject opt) {
		JSONObject connector = JSONObject.parseObject(properties.getPropertyValue("connector"));
		Set set= connector.keySet();
		Iterator iterator= set.iterator();
		while(iterator.hasNext()) {
			String key = (String)iterator.next();
			JSONObject connectorObj = connector.getJSONObject(key);
			String service = connectorObj.getString("service");
			int maxActive = connectorObj.getInteger("maxPoolSize");
			int idleTime = connectorObj.getInteger("idleTimeout");
			String version = connectorObj.getString("version");
			/*cs.setIdleTime(idleTime);
			cs.setMaxActive(maxActive);
			cs.setVersion(version);
			cs.setService(service);
			cs.init();*/
			map.put(key, this);
		}
		System.out.println("RpcClientImpl init.....");
	}

	@Override
	public void start() {
		//final ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-thrift-client.xml");
		try {
			Set set = map.keySet();
			Iterator iterator= set.iterator();
			while(iterator.hasNext()) {
				String key = (String)iterator.next();
				RpcClientImpl obj = (RpcClientImpl)map.get(key);
				obj.start();
			}
			serivce = (RPCInvokeService.Iface) RPCBeanFactory.getBean("clientSerivce");
			System.out.println("RpcClientImpl start.....");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Msg invoke(String service, String method, Msg msg) {
		Msg result;
		try {
			result = serivce.invoke(service, method, msg);
			return result;
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (TimedOutException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		return null;
	}
}
