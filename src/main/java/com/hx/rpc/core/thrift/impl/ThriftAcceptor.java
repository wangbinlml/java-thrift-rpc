package com.hx.rpc.core.thrift.impl;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hx.rpc.core.thrift.IThriftAcceptor;
import com.hx.rpc.core.utils.PropertiesUtils;
import com.hx.rpc.gen.RPCInvokeService;
public class ThriftAcceptor implements IThriftAcceptor{
	private static Logger logger = Logger.getLogger(ThriftAcceptor.class);
	private static PropertiesUtils properties = new PropertiesUtils();
	private RPCInvokeService.Iface serverSerivce;
	private JSONObject acceptor = JSONObject.parseObject(properties.getPropertyValue("acceptor"));
	
	private String host;
	private int port;
	private String name;
	private String service;
	private int weight = 1;
	private String version = "1.0.0";

	private ServerThread serverThread;
	private ThriftAcceptorRegister register;
	private ServerAddress serverAddress;
	
	@Override
	public void initBiz() {
		
	}

	@Override
	public void init(JSONObject opt) {
		//只作为客户端时acceptor是null
		if(acceptor == null){
			return;
		}
		this.setHost(acceptor.getString("host"));
		this.setPort(acceptor.getInteger("port"));
		this.setName(acceptor.getString("name"));
		this.setService(acceptor.getString("service"));
		if(acceptor.containsKey("weight")){
			this.setWeight(Integer.valueOf(acceptor.getString("weight")));
		}
		if(acceptor.containsKey("version")) {
			this.setVersion(acceptor.getString("version"));
		}
	}

	@Override
	public void start() throws Exception {
		//只作为客户端时acceptor是null
		if(acceptor == null){
			return;
		}
		if (serverAddress == null) {
			serverAddress = new ServerAddress();
		}
		String serverIP = serverAddress.getServerIP();
		if (StringUtils.isEmpty(serverIP)) {
			throw new Exception("cant find server ip...");
		}

		String hostname = serverIP + ":" + port + ":" + weight;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		RPCInvokeService.Processor processor = new RPCInvokeService.Processor(serverSerivce);
		// 需要单独的线程,因为serve方法是阻塞的.
		serverThread = new ServerThread(processor, port);
		serverThread.start();
		// 注册服务
		if (register != null) {
			register.register(service, version, hostname);
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
				logger.info("run acceptor server");
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

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
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

	public RPCInvokeService.Iface getServerSerivce() {
		return serverSerivce;
	}

	public void setServerSerivce(RPCInvokeService.Iface serverSerivce) {
		this.serverSerivce = serverSerivce;
	}
	

}
