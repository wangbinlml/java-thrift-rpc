package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.gen.Msg;

public interface IRpcClient {
	public void init(String config, JSONObject opt);
	public void start();
	public Msg invoke(String service, String method, Msg msg);
}
