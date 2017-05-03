package com.hx.rpc.test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.thrift.TException;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.hx.rpc.gen.Header;
import com.hx.rpc.gen.Msg;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.gen.impl.MethodCallback;

public class Client {
	public void startClient() {
        TTransport transport;
        try {
            transport = new TSocket("localhost", 1234);
            TProtocol protocol = new TBinaryProtocol(transport);
            RPCInvokeService.Client client = new RPCInvokeService.Client(protocol);
            transport.open();
            Msg msg = new Msg();
            Header header = new Header();
            header.setTid("1212121212");
            header.setProtocol("thrift");
            header.setMsgType(0);
            msg.setHeader(header);
            msg.setBody("hello");
            System.out.println("Client calls ....."); 
            Msg msg2 = client.invoke("abc", "abc", msg);
            System.out.println(msg2.getBody());
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
	public void startSyncClient() {
        try {
        	TAsyncClientManager clientManager = new TAsyncClientManager();
            TNonblockingTransport transport = new TNonblockingSocket( 
                    "localhost", 10005); 

            TProtocolFactory protocol = new TBinaryProtocol.Factory(); 
            RPCInvokeService.AsyncClient asyncClient = new RPCInvokeService.AsyncClient(protocol,clientManager,transport);
            System.out.println("Client calls ....."); 
    		CountDownLatch latch = new CountDownLatch(1);
            MethodCallback callBack = new MethodCallback(latch); 
            
            Msg msg = new Msg();
            Header header = new Header();
            header.setTid("1212121212");
            header.setProtocol("thrift");
            header.setMsgType(0);
            msg.setHeader(header);
            msg.setBody("hello2");
            asyncClient.invoke("abc", "abc", msg, callBack);
            Object res = callBack.getResult(); 
            while (res == null) { 
                res = callBack.getResult(); 
            } 
            System.out.println(res); 
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
	public static void main(String[] args) {
		Client client = new Client();
        client.startClient();
        client.startSyncClient();
	}

}
