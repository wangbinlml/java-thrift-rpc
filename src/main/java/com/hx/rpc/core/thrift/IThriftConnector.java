package com.hx.rpc.core.thrift;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.gen.Msg;

public interface IThriftConnector {
	public Msg invoke(String service, String method, Msg msg);
	public void init(JSONObject opt);
	public void start();
}
