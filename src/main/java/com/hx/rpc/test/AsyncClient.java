package com.hx.rpc.test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TTransportException;

import com.hx.rpc.gen.impl.MethodCallback;
import com.hx.rpc.gen.impl.RPCInvokeServiceImpl;
import com.hx.rpc.gen.Header;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;

public class AsyncClient {
	public static void main(String[] args) {
		try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			// 设置传输通道，调用非阻塞IO
			TNonblockingTransport transport = new TNonblockingSocket("127.0.0.1", 1234, 3000);
			// 协议要和服务端一致
			// TProtocolFactory tprotocol = new TCompactProtocol.Factory();
			TProtocolFactory tprotocol = new TBinaryProtocol.Factory();

			RPCInvokeService.AsyncClient asyncClient = new RPCInvokeService.AsyncClient(tprotocol, clientManager,
					transport);
			CountDownLatch latch = new CountDownLatch(1);
			MethodCallback callBack = new MethodCallback(latch);
			System.out.println("call method sayHello start ...");
			// 调用服务
			Msg msg = new Msg();
			Header header = new Header();
			header.setTid("1212121212");
			header.setProtocol("thrift");
			header.setMsgType(0);
			msg.setHeader(header);
			msg.setBody("hello");
			asyncClient.invoke("abc", "abc", msg, callBack);
			System.out.println("call method invoke .... end");
			// 等待完成异步调用
			boolean wait = latch.await(30, TimeUnit.SECONDS);
			System.out.println("latch.await =:" + wait);
			Msg res = (Msg)callBack.getResult(); 
            while (res == null) { 
                res = (Msg) callBack.getResult(); 
            } 
            System.out.println(res.getBody()); 
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
