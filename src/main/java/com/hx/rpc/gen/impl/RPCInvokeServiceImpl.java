package com.hx.rpc.gen.impl;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import com.hx.rpc.gen.InvalidRequestException;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.gen.TimedOutException;

public class RPCInvokeServiceImpl implements RPCInvokeService.Iface {
	public Msg invoke(String serviceName, String methodName, Msg msg)
			throws InvalidRequestException, TimedOutException, TException {
		String body = msg.getBody();
		msg.setBody(body + " world");
		return msg;
	}
}
