package com.hx.rpc.core.thrift.impl;

import java.net.InetSocketAddress;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.hx.rpc.core.thrift.IThriftConnectorAddressProvider;
import com.hx.rpc.gen.RPCInvokeService;

public class ThriftConnectorPool extends BasePoolableObjectFactory<RPCInvokeService.Client>{
	private final IThriftConnectorAddressProvider serverAddressProvider;
	private PoolOperationCallBack callback;
	protected ThriftConnectorPool(IThriftConnectorAddressProvider serverAddressProvider) throws Exception {
		this.serverAddressProvider = serverAddressProvider;
	}

	protected ThriftConnectorPool(IThriftConnectorAddressProvider serverAddressProvider, PoolOperationCallBack callback) throws Exception {
		this.serverAddressProvider = serverAddressProvider;
		this.callback = callback;
	}
	
	static interface PoolOperationCallBack {
		// 销毁client之前执行
		void destroy(RPCInvokeService.Client client);

		// 创建成功是执行
		void make(RPCInvokeService.Client client);
	}
	public void destroyObject(RPCInvokeService.Client client) throws Exception {
		if (callback != null) {
			try {
				callback.destroy(client);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		TTransport pin = client.getInputProtocol().getTransport();
		pin.close();
	}
	public boolean validateObject(TServiceClient client) {
		TTransport pin = client.getInputProtocol().getTransport();
		return pin.isOpen();
	}
	@Override
	public RPCInvokeService.Client makeObject() throws Exception {
		InetSocketAddress address = serverAddressProvider.selector();
		TTransport transport = new TSocket(address.getHostName(), address.getPort());
		TProtocol protocol = new TBinaryProtocol(transport);
		RPCInvokeService.Client client = new RPCInvokeService.Client(protocol);
		transport.open();
		if (callback != null) {
			try {
				callback.make(client);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

}
