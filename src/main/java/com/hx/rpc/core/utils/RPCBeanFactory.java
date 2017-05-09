package com.hx.rpc.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RPCBeanFactory {

    private static ApplicationContext applicationContext;
    
	static {
		//加载application-context.xml文件，初始化bean
		applicationContext = new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});
	}
	
	public static Object getBean(String name){
        return applicationContext.getBean(name); 
        
    } 

	public static String[] getBeanDefinitionNames(){
		return applicationContext.getBeanDefinitionNames();
	}


}
