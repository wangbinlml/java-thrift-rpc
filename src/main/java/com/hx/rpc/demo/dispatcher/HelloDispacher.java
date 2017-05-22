package com.hx.rpc.demo.dispatcher;

import com.hx.rpc.core.IBusinessService;
import com.hx.rpc.demo.IHelloBiz;
import com.hx.rpc.gen.Msg;

public class HelloDispacher implements IBusinessService {
	public IHelloBiz helloBiz;

	@Override
	public Msg doBusiness(String messageName, Msg reqMsg) {
		if("sayHello".equals(messageName)) {
			helloBiz.sayHello(reqMsg);
		}
		return reqMsg;
	}

	public IHelloBiz getHelloBiz() {
		return helloBiz;
	}

	public void setHelloBiz(IHelloBiz helloBiz) {
		this.helloBiz = helloBiz;
	}

}
