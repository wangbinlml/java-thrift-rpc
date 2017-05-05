package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpc;

public class RpcImpl implements IRpc {
	private ApplicationImpl app;

	public ApplicationImpl createApp(JSONObject opt) {
		app = new ApplicationImpl();
		app.init(opt);
		return app;
	}

	public RpcClientImpl getAccessService() {
		return app.getAccessService();
	}

	public RpcServerImpl getRpcService() {
		return app.getRpcServer();
	}

	public void setApp(ApplicationImpl app) {
		this.app = app;
	}

	public ApplicationImpl getApp() {
		return app;
	}
}
