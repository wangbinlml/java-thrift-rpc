package com.hx.rpc.core.thrift;

import java.net.InetSocketAddress;
import java.util.List;

public interface IThriftConnectorAddressProvider {
	// 获取服务名称
	String getService();

	/**
	 * 获取所有服务端地址
	 * 
	 * @return
	 */
	List<InetSocketAddress> findServerAddressList();

	/**
	 * 选取一个合适的address,可以随机获取等' 内部可以使用合适的算法.
	 * 
	 * @return
	 */
	InetSocketAddress selector();

	void init(String service, String version);

	void close();
}
