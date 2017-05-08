package com.hx.rpc.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IApplication;
import com.hx.rpc.core.IRpc;
import com.hx.rpc.core.IRpcClient;
import com.hx.rpc.core.IRpcServer;

public class RpcImpl implements IRpc {
	private IApplication app;
	ApplicationImpl appImpl;
	
	public ApplicationImpl createApp(JSONObject opt) {
		appImpl = app.init(opt);
		return appImpl;
	}

	public void setApp(ApplicationImpl app) {
		this.app = app;
	}

	public IApplication getApp() {
		return app;
	}
	public IRpcClient getAccessService() {
		return appImpl.getRpcClient();
	}

	public IRpcServer getRpcService() {
		return appImpl.getRpcServer();
	}

}
