package com.hx.rpc.gen.impl;

import org.apache.thrift.TException;

import com.hx.rpc.core.IBusinessService;
import com.hx.rpc.core.utils.RPCBeanFactory;
import com.hx.rpc.gen.InvalidRequestException;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.gen.TimedOutException;

public class RPCInvokeServiceImpl implements RPCInvokeService.Iface {
	public Msg invoke(String serviceName, String methodName, Msg msg)
			throws InvalidRequestException, TimedOutException, TException {
		System.out.println("============="+serviceName);
		IBusinessService biz = (IBusinessService) RPCBeanFactory.getBean(serviceName);
		return biz.doBusiness(methodName, msg);
	}
}
