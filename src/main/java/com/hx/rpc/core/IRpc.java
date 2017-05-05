package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.impl.ApplicationImpl;

public interface IRpc {
	public ApplicationImpl createApp(JSONObject opt);
}
