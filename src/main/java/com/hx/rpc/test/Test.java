package com.hx.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.IRpc;
import com.hx.rpc.core.IRpcClient;
import com.hx.rpc.core.impl.ApplicationImpl;
import com.hx.rpc.core.impl.RpcImpl;
import com.hx.rpc.gen.Header;
import com.hx.rpc.gen.Msg;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		@SuppressWarnings("resource")
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		IRpc rpc = (RpcImpl) context.getBean("rpc");
		ApplicationImpl app = rpc.createApp(new JSONObject());
		app.start();
		Thread.sleep(1000);
		IRpcClient client = app.getRpcClient();
		
		Msg msg = new Msg();
        Header header = new Header();
        header.setTid("1212121212");
        header.setProtocol("thrift");
        header.setMsgType(0);
        msg.setHeader(header);
        msg.setBody("hello");
        System.out.println("Client calls ....."); 
		Msg msg2;
		msg2 = client.invoke("biz_service", "sendMsg", msg);
        System.out.println(msg2.getBody());
	}
}
