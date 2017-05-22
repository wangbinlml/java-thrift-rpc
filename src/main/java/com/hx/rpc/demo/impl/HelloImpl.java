package com.hx.rpc.demo.impl;

import com.hx.rpc.core.IApplication;
import com.hx.rpc.core.IRpcClient;
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
		//调用nodejs common_service服务
		/*IRpcClient client = app.getRpcClient();
		Msg msg = client.invoke("common_service", "sendSMS", reqMsg);
		System.out.println("common_service rpcClient: " +msg.getBody());*/
	}

}
