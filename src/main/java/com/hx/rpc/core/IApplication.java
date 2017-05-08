package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.impl.ApplicationImpl;

public interface IApplication {
	public ApplicationImpl init(JSONObject opt);
	public void start();
}
