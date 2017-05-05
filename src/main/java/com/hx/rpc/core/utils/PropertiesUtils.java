package com.hx.rpc.core.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	String file = "rpcServiceConfig.properties";

	public PropertiesUtils() {
	}

	public PropertiesUtils(String inputFile) {
		file = inputFile;
	}

	public String getPropertyValue(String key) {
		// 生成输入流
		InputStream ins = getClass().getResourceAsStream("/" + file);
		// 生成properties对象
		Properties properties = new Properties();
		String value = "";
		try {
			properties.load(ins);
			value = properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void main(String[] args) {
		PropertiesUtils util = new PropertiesUtils();
		String a = util.getPropertyValue("mas");
		if ("2.0".equals(a)) {
			System.out.println("-11--" + a);
		}
		System.out.println("-22--" + a);
	}

}