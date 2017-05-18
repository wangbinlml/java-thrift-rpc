package com.hx.rpc.thrift;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.gen.Msg;

public interface IThriftConnector {
	public Msg invoke(String service, String method, Msg msg);
	public void init(JSONObject config, JSONObject opt);
	public void start();
}
