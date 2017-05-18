package com.hx.rpc.thrift;

public interface IThriftAcceptorRegister {
	void register(String service, String version, String address);
}
