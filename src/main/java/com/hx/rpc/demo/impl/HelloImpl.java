package com.hx.rpc.demo.impl;

import com.hx.rpc.core.IApplication;
import com.hx.rpc.demo.IHelloBiz;
import com.hx.rpc.gen.Msg;

public class HelloImpl implements IHelloBiz {
	IApplication app;

	public IApplication getApp() {
		return app;
	}

	public void setApp(IApplication app) {
		this.app = app;
	}

	@Override
	public void sayHello(Msg reqMsg) {
		String body = reqMsg.getBody();
		reqMsg.setBody(body + " world");
		System.out.println("sayHello: " + body + " world");
		System.out.println("rpcClient: " + app.getRpcClient());
	}

}
