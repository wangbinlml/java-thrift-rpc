package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IApplication;
import com.hx.rpc.core.IRpcClient;
import com.hx.rpc.core.IRpcServer;

public class ApplicationImpl implements IApplication {
	public IRpcClient rpcClient;
	public IRpcServer rpcServer;

	@Override
	public ApplicationImpl init(JSONObject opt) {
		System.out.println("ApplicationImpl init.....");
		rpcClient.init("", new JSONObject());
		rpcServer.init("", new JSONObject());
		return this;
	}

	@Override
	public void start() {
		System.out.println("ApplicationImpl start.....");
		rpcServer.start();
		rpcClient.start();
	}

	public IRpcClient getRpcClient() {
		return rpcClient;
	}

	public void setRpcClient(IRpcClient rpcClient) {
		this.rpcClient = rpcClient;
	}

	public IRpcServer getRpcServer() {
		return rpcServer;
	}

	public void setRpcServer(IRpcServer rpcServer) {
		this.rpcServer = rpcServer;
	}


}
