package com.hx.rpc.thrift;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.gen.Msg;

public interface IThriftAcceptor {
	public void initBiz();
	public void start() throws Exception;
	public void init(JSONObject config, JSONObject opt);
}
