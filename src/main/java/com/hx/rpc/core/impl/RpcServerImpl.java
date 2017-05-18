package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpcServer;
import com.hx.rpc.thrift.IThriftAcceptor;

public class RpcServerImpl implements IRpcServer {
	private IThriftAcceptor acceptor;
	@Override
	public void init(JSONObject opt) {
		acceptor.init(opt);
		System.out.println("RpcServerImpl init.....");
	}

	@Override
	public void start() {
		try {
			acceptor.start();
			System.out.println("RpcServerImpl start.....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void initBiz() {
		acceptor.initBiz();
	}

	public IThriftAcceptor getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(IThriftAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	

}
