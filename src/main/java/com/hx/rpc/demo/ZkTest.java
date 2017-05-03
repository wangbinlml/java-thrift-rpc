package com.hx.rpc.demo;

import org.apache.curator.framework.CuratorFramework;

import com.hx.rpc.zk.ZookeeperCuratorUtils;

public class ZkTest {

	public static void main(String[] args) {
		CuratorFramework client = ZookeeperCuratorUtils.clientOne();
		System.out.println("start.....");
		try {
			ZookeeperCuratorUtils.createEphemeral(client, "/zookeeper/127.0.0.1", "abc".getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
