package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.impl.RpcClientImpl;
import com.hx.rpc.core.impl.RpcServerImpl;

public interface IApplication {
	public void init(JSONObject opt);
	public void start();
}
