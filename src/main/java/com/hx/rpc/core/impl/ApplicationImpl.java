package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IApplication;

public class ApplicationImpl implements IApplication {
	public RpcClientImpl rpcClient;
	public RpcServerImpl rpcServer;

	@Override
	public void init(JSONObject opt) {
		
	}

	@Override
	public void start() {
		
	}


	public RpcServerImpl getRpcService() {
		return rpcServer;
	}

	public RpcClientImpl getAccessService() {
		return rpcClient;
	}

	public RpcClientImpl getRpcClient() {
		return rpcClient;
	}

	public void setRpcClient(RpcClientImpl rpcClient) {
		this.rpcClient = rpcClient;
	}

	public RpcServerImpl getRpcServer() {
		return rpcServer;
	}

	public void setRpcServer(RpcServerImpl rpcServer) {
		this.rpcServer = rpcServer;
	}

}
