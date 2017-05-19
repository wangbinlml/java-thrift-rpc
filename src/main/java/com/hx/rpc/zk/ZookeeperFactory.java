package com.hx.rpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

import com.hx.rpc.core.utils.PropertiesUtils;

/**
 * 获取zookeeper客户端链接
 */
public class ZookeeperFactory implements FactoryBean<CuratorFramework> {
	private PropertiesUtils properties = new PropertiesUtils("config.properties");
	private static Logger logger = Logger.getLogger(ZookeeperFactory.class);
	private String zkHosts;
	// session超时
	private int sessionTimeout = 30000;
	private int connectionTimeout = 30000;

	// 共享一个zk链接
	private boolean singleton = true;

	// 全局path前缀,常用来区分不同的应用
	private String namespace;

	private final static String ROOT = "";

	private CuratorFramework zkClient;
	
	public void init(){
		if (StringUtils.isEmpty(namespace)) {
			namespace = ROOT;
		} else {
			namespace = ROOT + "/" + namespace;
		}
		this.setNamespace(namespace);
		zkHosts = properties.getPropertyValue("zkHosts");
		this.setZkHosts(zkHosts);
		if (!StringUtils.isEmpty(properties.getPropertyValue("sessionTimeout"))) {
			sessionTimeout = Integer.valueOf(properties.getPropertyValue("sessionTimeout"));
			this.setSessionTimeout(sessionTimeout);
		}
		if (!StringUtils.isEmpty(properties.getPropertyValue("connectionTimeout"))) {
			connectionTimeout = Integer.valueOf(properties.getPropertyValue("connectionTimeout"));
			this.setConnectionTimeout(connectionTimeout);
		}
	}
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}

	@Override
	public CuratorFramework getObject() throws Exception {
		if (singleton) {
			if (zkClient == null) {
				logger.info("zkClient start");
				zkClient = create();
				zkClient.start();
			}
			return zkClient;
		}
		return create();
	}

	@Override
	public Class<?> getObjectType() {
		return CuratorFramework.class;
	}

	@Override
	public boolean isSingleton() {
		return singleton;
	}

	public CuratorFramework create() throws Exception {
		return create(zkHosts, sessionTimeout, connectionTimeout, namespace);
	}

	public static CuratorFramework create(String connectString, int sessionTimeout, int connectionTimeout,
			String namespace) {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
		return builder.connectString(connectString).sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(30000)
				.canBeReadOnly(true).namespace(namespace)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE)).defaultData(null).build();
	}
	
	public void close() {
		if (zkClient != null) {
			logger.info("zkClient close");
			zkClient.close();
		}
	}
	public String getZkHosts() {
		return zkHosts;
	}
	public void setZkHosts(String zkHosts) {
		this.zkHosts = zkHosts;
	}
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
}