package com.hx.rpc.thrift.impl;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.server.TThreadPoolServer;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.gen.RPCInvokeService;
import com.hx.rpc.gen.impl.RPCInvokeServiceImpl;
import com.hx.rpc.thrift.IThriftAcceptor;
public class ThriftAcceptor implements IThriftAcceptor{
	private static Logger logger = Logger.getLogger(ThriftAcceptor.class);
	String host;
	int port;
	String name;
	String zkPath;
	int weight = 1;
	String version = "1.0.0";

	private ServerThread serverThread;
	private ThriftAcceptorRegister register;
	private ServerAddress serverAddress;
	
	@Override
	public void initBiz() {
		
	}

	@Override
	public void init(JSONObject config, JSONObject opt) {
		this.setHost(config.getString("host"));
		this.setPort(config.getInteger("port"));
		this.setName(config.getString("name"));
		this.setZkPath(config.getString("zkPath"));
		if(config.containsKey("weight")){
			this.setWeight(Integer.valueOf(config.getString("weight")));
		}
		if(config.containsKey("version")) {
			this.setVersion(config.getString("version"));
		}
	}

	@Override
	public void start() throws Exception {
		if (serverAddress == null) {
			serverAddress = new ServerAddress();
		}
		String serverIP = serverAddress.getServerIP();
		if (StringUtils.isEmpty(serverIP)) {
			throw new Exception("cant find server ip...");
		}

		String hostname = serverIP + ":" + port + ":" + weight;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		RPCInvokeService.Processor processor = new RPCInvokeService.Processor(new RPCInvokeServiceImpl());
		// 需要单独的线程,因为serve方法是阻塞的.
		serverThread = new ServerThread(processor, port);
		serverThread.start();
		// 注册服务
		if (register != null) {
			register.register(zkPath, version, hostname);
		}
	}
	class ServerThread extends Thread {
		private TServer server;

		ServerThread(TProcessor processor, int port) throws Exception {
			TServerSocket serverTransport = new TServerSocket(port);
			Factory portFactory = new TBinaryProtocol.Factory(true, true);
			Args args2 = new Args(serverTransport);
			args2.processor(processor);
			args2.protocolFactory(portFactory);
			server = new TThreadPoolServer(args2);
		}

		@Override
		public void run() {
			try {
				// 启动服务
				logger.info("启动服务");
				server.serve();
			} catch (Exception e) {
                e.printStackTrace();
			}
		}

		public void stopServer() {
			server.stop();
		}
	}

	public void close() {
		logger.info("启动停止");
		serverThread.stopServer();
	}


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZkPath() {
		return zkPath;
	}

	public void setZkPath(String zkPath) {
		this.zkPath = zkPath;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ThriftAcceptorRegister getRegister() {
		return register;
	}

	public void setRegister(ThriftAcceptorRegister register) {
		this.register = register;
	}

	public ServerAddress getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(ServerAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	

}
