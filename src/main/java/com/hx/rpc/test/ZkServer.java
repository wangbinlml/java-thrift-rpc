package com.hx.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ZkServer {

	public static void main(String[] args) {
		try {
			new ClassPathXmlApplicationContext("classpath:spring-context-thrift-server.xml");
			System.out.println("server start");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {

			TServerSocket serverTransport = new TServerSocket(1234);

			RPCInvokeService.Processor processor = new RPCInvokeService.Processor(new RPCInvokeServiceImpl());

			Factory portFactory = new TBinaryProtocol.Factory(true, true);

			Args args2 = new Args(serverTransport);
			args2.processor(processor);
			args2.protocolFactory(portFactory);

			System.out.println("start............");
			TServer server = new TThreadPoolServer(args2);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}*/
	}

}
