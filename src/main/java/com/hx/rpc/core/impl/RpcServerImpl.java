package com.hx.rpc.core.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpcServer;

public class RpcServerImpl implements IRpcServer {

	@Override
	public void init(String config, JSONObject opt) {
		System.out.println("RpcServerImpl init.....");
	}

	@Override
	public void start() {
		new ClassPathXmlApplicationContext("classpath:spring-context-thrift-server.xml");
		System.out.println("RpcServerImpl start.....");
	}


	@Override
	public void initBiz() {
		
	}

}
