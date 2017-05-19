package com.hx.rpc.core.thrift;

public interface IThriftAcceptorRegister {
	void register(String service, String version, String address);
}
