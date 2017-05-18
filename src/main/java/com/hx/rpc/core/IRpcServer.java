package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;

public interface IRpcServer {
	public void initBiz();
	public void init(JSONObject opt);
	public void start();
}
