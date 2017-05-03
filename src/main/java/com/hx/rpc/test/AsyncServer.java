package com.hx.rpc.test;

import org.apache.thrift.TException;  
import org.apache.thrift.TProcessor;  
import org.apache.thrift.protocol.TBinaryProtocol;  
import org.apache.thrift.server.THsHaServer;  
import org.apache.thrift.server.TServer;  
import org.apache.thrift.transport.TFramedTransport;  
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.hx.rpc.gen.impl.RPCInvokeServiceImpl;
import com.hx.rpc.gen.RPCInvokeService;
public class AsyncServer {

	public static void main(String[] args) {
		TProcessor tprocessor = new RPCInvokeService.Processor<RPCInvokeService.Iface>(new RPCInvokeServiceImpl());  
        // 传输通道 - 非阻塞方式    
        TNonblockingServerSocket serverTransport;
		try {
			serverTransport = new TNonblockingServerSocket(1234);
			//半同步半异步  
	        THsHaServer.Args tArgs = new THsHaServer.Args(serverTransport);  
	        tArgs.processor(tprocessor);  
	        tArgs.transportFactory(new TFramedTransport.Factory());  
	        //二进制协议  
	        tArgs.protocolFactory(new TBinaryProtocol.Factory());  
	        // 半同步半异步的服务模型  
	        TServer server = new THsHaServer(tArgs);  
	        System.out.println("HelloTHsHaServer start....");  
	        server.serve(); // 启动服务  
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

}
