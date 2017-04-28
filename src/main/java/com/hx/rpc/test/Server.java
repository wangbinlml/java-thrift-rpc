package com.hx.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

//服务端启动
public class Server {

	public static void main(String[] args) {
		try {
			new ClassPathXmlApplicationContext("classpath:spring-context-thrift-server.xml");
			System.out.println("server start");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
