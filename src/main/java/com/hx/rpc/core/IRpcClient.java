package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;

public interface IRpcClient {
	public void init(String config, JSONObject opt);
	public void start();
	public void send(String service, String method, JSONObject msg);
}
