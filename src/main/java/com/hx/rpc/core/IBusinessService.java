package com.hx.rpc.core;

import com.alibaba.fastjson.JSONObject;

public interface IBusinessService {
	public void doBusiness(String messageName, JSONObject reqMsg);
}
