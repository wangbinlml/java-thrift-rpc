package com.hx.rpc.core.impl;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpcClient;
import com.hx.rpc.gen.InvalidRequestException;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.gen.TimedOutException;

public class RpcClientImpl implements IRpcClient {
	RPCInvokeService.Iface serivce;
	RPCInvokeService.AsyncIface asycSerivce;

	@Override
	public void init(String config, JSONObject opt) {
		System.out.println("RpcClientImpl init.....");
	}

	@Override
	public void start() {
		final ApplicationContext context = new ClassPathXmlApplicationContext("spring-context-thrift-client.xml");
		serivce = (RPCInvokeService.Iface) context.getBean("echoSerivce");
		asycSerivce = (RPCInvokeService.AsyncIface) context.getBean("echoSerivce");
		System.out.println("RpcClientImpl start.....");
	}

	@Override
	public Msg invoke(String service, String method, Msg msg) {
		Msg result;
		try {
			result = serivce.invoke(service, method, msg);
			return result;
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimedOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void invoke(String service, String method, Msg msg, AsyncMethodCallback callback) {
		try {
			asycSerivce.invoke(service, method, msg, callback);
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
