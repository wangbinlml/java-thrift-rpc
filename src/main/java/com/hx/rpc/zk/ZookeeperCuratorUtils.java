package com.hx.rpc.zk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

/**
 * curator管理zookeeper的相关方法工具类
 * 
 * @author songqinghu 要点: 1.连接的建立 (两种 OK--使用权限方式) 2.节点的管理操作,增删改查--层叠节点的操作(OK
 *         --设立命名空间) 3.节点的监听操作,和无限监听事件触发 4.节点的访问控制ACL操作,密码的添加,修改 5.节点原子性操作
 *         6.节点的分布式锁操作
 */
public class ZookeeperCuratorUtils {

	private static PathChildrenCache cachedPath;

	/**
	 * 先测试玩玩
	 * 
	 * @描述：XXXXXXX
	 * @param args
	 * @return void
	 * @exception @createTime：2016年5月17日
	 * @author: songqinghu
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// nodesList(clientOne(), "/");
		CuratorFramework client = clientOne();
		System.out.println("start.....");
		setListenterThreeThree(client,"/zookeeper");
	}

	/**
	 * 
	 * @描述：创建一个zookeeper连接---连接方式一: 最简单的连接
	 * @return void
	 * @exception @createTime：2016年5月17日
	 * @author: songqinghu
	 */
	public static CuratorFramework clientOne() {
		// zk 地址
		String connectString = "127.0.0.1:2181";
		// 连接时间 和重试次数
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		client.start();
		return client;
	}

	/**
	 * 
	 * @描述：创建一个zookeeper连接---连接方式二:优选这个
	 * @return void
	 * @exception @createTime：2016年5月17日
	 * @author: songqinghu
	 */
	private static CuratorFramework clientTwo() {

		// 默认创建的根节点是没有做权限控制的--需要自己手动加权限???----
		ACLProvider aclProvider = new ACLProvider() {
			private List<ACL> acl;

			@Override
			public List<ACL> getDefaultAcl() {
				if (acl == null) {
					ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
					acl.clear();
					acl.add(new ACL(Perms.ALL, new Id("auth", "admin:admin")));
					this.acl = acl;
				}
				return acl;
			}

			@Override
			public List<ACL> getAclForPath(String path) {
				return acl;
			}
		};
		String scheme = "digest";
		byte[] auth = "admin:admin".getBytes();
		int connectionTimeoutMs = 5000;
		String connectString = "10.125.2.44:2181";
		String namespace = "testnamespace";
		CuratorFramework client = CuratorFrameworkFactory.builder().aclProvider(aclProvider).authorization(scheme, auth)
				.connectionTimeoutMs(connectionTimeoutMs).connectString(connectString).namespace(namespace)
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).build();
		client.start();
		return client;
	}

	public static void create(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this will create the given ZNode with the given data
		client.create().forPath(path, payload);
	}

	public static void createEphemeral(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this will create the given EPHEMERAL ZNode with the given data
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path, payload);
	}

	public static String createEphemeralSequential(CuratorFramework client, String path, byte[] payload)
			throws Exception {
		// this will create the given EPHEMERAL-SEQUENTIAL ZNode with the given
		// data using Curator protection.
		return client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, payload);
	}

	public static void setData(CuratorFramework client, String path, byte[] payload) throws Exception {
		// set data for the given node
		client.setData().forPath(path, payload);
	}

	public static void setDataAsync(CuratorFramework client, String path, byte[] payload) throws Exception {
		// this is one method of getting event/async notifications
		CuratorListener listener = new CuratorListener() {
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				// examine event for details
			}
		};
		client.getCuratorListenable().addListener(listener);
		// set data for the given node asynchronously. The completion
		// notification
		// is done via the CuratorListener.
		client.setData().inBackground().forPath(path, payload);
	}

	public static void setDataAsyncWithCallback(CuratorFramework client, BackgroundCallback callback, String path,
			byte[] payload) throws Exception {
		// this is another method of getting notification of an async completion
		client.setData().inBackground(callback).forPath(path, payload);
	}

	public static void delete(CuratorFramework client, String path) throws Exception {
		// delete the given node
		client.delete().forPath(path);
	}

	public static void guaranteedDelete(CuratorFramework client, String path) throws Exception {
		// delete the given node and guarantee that it completes
		client.delete().guaranteed().forPath(path);
	}

	public static List<String> watchedGetChildren(CuratorFramework client, String path) throws Exception {
		/**
		 * Get children and set a watcher on the node. The watcher notification
		 * will come through the CuratorListener (see setDataAsync() above).
		 */
		return client.getChildren().watched().forPath(path);
	}

	public static List<String> watchedGetChildren(CuratorFramework client, String path, Watcher watcher)
			throws Exception {
		/**
		 * Get children and set the given watcher on the node.
		 */
		return client.getChildren().usingWatcher(watcher).forPath(path);
	}

	// 3.Tree Cache
	// 监控 指定节点和节点下的所有的节点的变化--无限监听 可以进行本节点的删除(不在创建)
	private static void setListenterThreeThree(CuratorFramework client,String path) throws Exception {
		ExecutorService pool = Executors.newCachedThreadPool();
		// 设置节点的cache
		TreeCache treeCache = new TreeCache(client, path);
		// 设置监听器和处理过程
		treeCache.getListenable().addListener(new TreeCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
				ChildData data = event.getData();
				if (data != null) {
					switch (event.getType()) {
					case NODE_ADDED:
						System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
						break;
					case NODE_REMOVED:
						System.out.println("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
						break;
					case NODE_UPDATED:
						System.out.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
						break;

					default:
						break;
					}
				} else {
					System.out.println("data is null : " + event.getType());
				}
			}
		});
		// 开始监听
		treeCache.start();

	}

	public static Collection<CuratorTransactionResult> transaction(CuratorFramework client) throws Exception {
		// this example shows how to use ZooKeeper's new transactions
		Collection<CuratorTransactionResult> results = client.inTransaction().create()
				.forPath("/a/path", "some data".getBytes()).and().setData()
				.forPath("/another/path", "other data".getBytes()).and().delete().forPath("/yet/another/path").and()
				.commit(); // IMPORTANT!
							// called
		for (CuratorTransactionResult result : results) {
			System.out.println(result.getForPath() + " - " + result.getType());
		}
		return results;
	}

	/*
	 * These next four methods show how to use Curator's transaction APIs in a
	 * more traditional - one-at-a-time - manner
	 */
	public static CuratorTransaction startTransaction(CuratorFramework client) {
		// start the transaction builder
		return client.inTransaction();
	}

	public static CuratorTransactionFinal addCreateToTransaction(CuratorTransaction transaction) throws Exception {
		// add a create operation
		return transaction.create().forPath("/a/path", "some data".getBytes()).and();
	}

	public static CuratorTransactionFinal addDeleteToTransaction(CuratorTransaction transaction) throws Exception {
		// add a delete operation
		return transaction.delete().forPath("/another/path").and();
	}

	public static void commitTransaction(CuratorTransactionFinal transaction) throws Exception {
		// commit the transaction
		transaction.commit();
	}
}