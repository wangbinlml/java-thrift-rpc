package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpcServer;
import com.hx.rpc.core.utils.PropertiesUtils;
import com.hx.rpc.thrift.impl.ThriftAcceptor;

public class RpcServerImpl extends ThriftAcceptor implements IRpcServer {
	private static PropertiesUtils properties = new PropertiesUtils();
	@Override
	public void init(String config, JSONObject opt) {
		JSONObject acceptor = JSONObject.parseObject(properties.getPropertyValue("acceptor"));
		String name = acceptor.getString("name");
		String service = acceptor.getString("service");
		String ip = acceptor.getString("ip");
		int port = acceptor.getInteger("port");
		String version = acceptor.getString("version");
		int weight = acceptor.getInteger("weight");
		setHost(ip);
		setName(name);
		setPort(port);
		setVersion(version);
		setWeight(weight);
		setZkPath(service);
		System.out.println("RpcServerImpl init.....");
	}

	@Override
	public void start() {
		try {
			start();
			System.out.println("RpcServerImpl start.....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void initBiz() {
		initBiz();
	}

}
