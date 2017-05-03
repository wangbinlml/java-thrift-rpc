package com.hx.rpc.gen.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.thrift.async.AsyncMethodCallback;

import com.hx.rpc.gen.RPCInvokeService.AsyncClient.invoke_call;

public class MethodCallback implements AsyncMethodCallback<invoke_call> {
	private CountDownLatch latch;  
	Object res = null; 
    public MethodCallback(CountDownLatch latch) {  
        this.latch = latch;  
    }  
    public Object getResult() { 
        // 返回结果值
        return this.res; 
    } 

    @Override  
    public void onComplete(invoke_call response) {  
        System.out.println("onComplete");  
        try {  
        	this.res = response.getResult(); 
            System.out.println("AsynCall result :" + response.getResult().toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            latch.countDown();  
        }  
    }  
    @Override  
    public void onError(Exception exception) {  
        System.out.println("onError :" + exception.getMessage());  
        latch.countDown();  
    }
}
