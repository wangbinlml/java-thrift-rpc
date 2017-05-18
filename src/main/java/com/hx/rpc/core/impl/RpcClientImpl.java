package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpcClient;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.thrift.IThriftConnector;

public class RpcClientImpl  implements IRpcClient {
	private IThriftConnector connector;
	@Override
	public void init(JSONObject opt) {
		connector.init(opt);
		System.out.println("RpcClientImpl init.....");
	}

	@Override
	public void start() {
		connector.start();
		System.out.println("RpcClientImpl start.....");
	}

	@Override
	public Msg invoke(String service, String method, Msg msg) {
		return connector.invoke(service, method, msg);
	}

	public IThriftConnector getConnector() {
		return connector;
	}

	public void setConnector(IThriftConnector connector) {
		this.connector = connector;
	}
	
}
