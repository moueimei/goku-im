package com.goku.im.net.socket.server.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import tv.acfun.im.net.socket.server.conf.ServerConfig;
import tv.acfun.im.net.socket.server.handler.SocketCloseHandler;
import tv.acfun.im.net.socket.server.handler.SocketExecuteHandler;
import tv.acfun.im.net.socket.server.logger.ServerLogger;

/**
 * 
 * @author zhaodx
 *
 */
public class SocketServer 
{
	private int port;
	private SocketExecuteHandler executor;
    private SocketCloseHandler closer;
	private ServerLogger serverLogger;
	private ServerConfig serverConfig;
	
	public SocketServer(int port)
	{ 
		this.port = port;
	}
	
	public void setExecutor(SocketExecuteHandler executor) 
	{
		this.executor = executor;
	}

	public void setCloser(SocketCloseHandler closer)
	{
		this.closer = closer;
	}

	public void setServerConfig(ServerConfig serverConfig)
	{
		this.serverConfig = serverConfig;
	}

	public void setWebServerLogger(ServerLogger serverLogger)
	{
		this.serverLogger = serverLogger;
	}

	public void start() throws Exception
	{
		//Acceptor线程池,负责处理客户端的TCP连接请求
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		//是真正负责I/O读写操作的线程组
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try
		{
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup);
			server.channel(NioServerSocketChannel.class);
			if(null == serverConfig)
				serverConfig = new ServerConfig();

			///配置server
			server.option(ChannelOption.SO_BACKLOG, serverConfig.getBacklog());
			server.option(ChannelOption.SO_TIMEOUT, serverConfig.getSoTimeout());
			server.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, serverConfig.getConnTimeout());
			server.option(ChannelOption.SO_REUSEADDR,  serverConfig.isReuseAddr());
			server.option(ChannelOption.SO_KEEPALIVE, serverConfig.isKeepAlive());
			server.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			server.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			//FixedRecvByteBufAllocator：固定长度的接收缓冲区分配器
			//AdaptiveRecvByteBufAllocator：容量动态调整的接收缓冲区分配器，它会根据之前Channel接收到的数据报大小进行计算，
			// 如果连续填充满接收缓冲区的可写空间，则动态扩展容量。如果连续2次接收到的数据报都小于指定值，则收缩当前的容量，以节约内存
			server.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);

			SocketServerInitializer initializer = new SocketServerInitializer();
			initializer.setCloser(closer);
			initializer.setExecutor(executor);
			initializer.setWebServerLogger(serverLogger);
			initializer.setServerConfig(serverConfig);
			server.handler(new LoggingHandler(LogLevel.INFO));
			server.childHandler(initializer);
			//服务器绑定端口监听
			ChannelFuture future = server.bind(port).sync();
			// 监听服务器关闭监听
			future.channel().closeFuture().sync();
		}
		finally
		{
			bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
		}
	}
}