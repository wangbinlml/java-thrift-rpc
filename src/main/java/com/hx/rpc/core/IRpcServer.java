package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;

public interface IRpcServer {
	public void initBiz();
	public void init(String config, JSONObject opt);
	public void start();
}
