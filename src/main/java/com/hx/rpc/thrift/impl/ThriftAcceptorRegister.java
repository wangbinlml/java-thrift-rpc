package com.hx.rpc.thrift.impl;

import java.io.UnsupportedEncodingException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.StringUtils;

import com.hx.rpc.thrift.IThriftAcceptorRegister;

public class ThriftAcceptorRegister implements IThriftAcceptorRegister {
	private static Logger logger = Logger.getLogger(ThriftAcceptorRegister.class);

	private CuratorFramework zkClient;

	public ThriftAcceptorRegister() {
	}

	public ThriftAcceptorRegister(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}

	@Override
	public void register(String service, String version, String address) {
		if (zkClient.getState() == CuratorFrameworkState.LATENT) {
			zkClient.start();
		}
		if (StringUtils.isEmpty(version)) {
			version = "1.0.0";
		}
		// 临时节点
		try {
			zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
					.forPath(service + "/" + version + "/" + address);
			logger.info("regist path " + service + "/" + version + "/" + address);
		} catch (UnsupportedEncodingException e) {
			logger.error("register service address to zookeeper exception:{}", e);
			logger.error("register service address to zookeeper exception: address UnsupportedEncodingException", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("register service address to zookeeper exception:{}", e);
			logger.error("register service address to zookeeper exception:{}", e);
			e.printStackTrace();
		}

	}

	public void close() {
		zkClient.close();
	}
}
