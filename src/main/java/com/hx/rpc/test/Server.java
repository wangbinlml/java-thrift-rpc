package com.hx.rpc.test;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.hx.rpc.gen.impl.RPCInvokeServiceImpl;
import com.hx.rpc.gen.RPCInvokeService;

public class Server {

	public static void main(String[] args) {
		try {

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
		}
	}

}
