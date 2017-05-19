package com.hx.rpc.demo;

import com.hx.rpc.core.IApplication;
import com.hx.rpc.core.IBusinessService;
import com.hx.rpc.gen.Msg;

public class HelloImpl implements IBusinessService {
	IApplication app;

	@Override
	public Msg doBusiness(String messageName, Msg reqMsg) {
		String body = reqMsg.getBody();
		reqMsg.setBody(body + " world");
		return reqMsg;
	}

	public IApplication getApp() {
		return app;
	}

	public void setApp(IApplication app) {
		this.app = app;
	}

}
