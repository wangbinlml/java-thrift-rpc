package com.hx.rpc.gen.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.thrift.async.AsyncMethodCallback;

import com.hx.rpc.gen.RPCInvokeService.AsyncClient.invoke_call;

public class MethodCallback implements AsyncMethodCallback<invoke_call> {
	private Logger logger = Logger.getLogger(MethodCallback.class.getName());
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
    	logger.debug("onComplete");  
        try {  
        	this.res = response.getResult(); 
        	logger.debug("AsynCall result :" + response.getResult().toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            latch.countDown();  
        }  
    }  
    @Override  
    public void onError(Exception e) {  
    	latch.countDown();  
    	e.printStackTrace();
    }
}
