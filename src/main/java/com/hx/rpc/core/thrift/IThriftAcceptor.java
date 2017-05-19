package com.hx.rpc.core.thrift;

import com.alibaba.fastjson.JSONObject;

public interface IThriftAcceptor {
	public void initBiz();
	public void start() throws Exception;
	public void init(JSONObject opt);
}
