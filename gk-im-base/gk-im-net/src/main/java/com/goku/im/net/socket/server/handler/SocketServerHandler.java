package com.goku.im.net.socket.server.handler;

import com.goku.im.net.socket.server.context.SocketRequestContext;
import com.goku.im.net.socket.server.context.SocketResponse;
import com.goku.im.net.socket.server.logger.ServerLogger;
import com.goku.im.net.socket.server.logger.ServerLoggerEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class SocketServerHandler extends ChannelInboundHandlerAdapter {
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("{\"action\":\"pong\"}",
                    CharsetUtil.UTF_8));
    private final static int MAX_HEARTBEAT_COUNT = 3;
    private SocketExecuteHandler executor;
    private SocketCloseHandler closer;
    private ServerLogger serverLogger;
    private int heartbeatCount;

    public SocketServerHandler() {
    }

    public void setExecutor(SocketExecuteHandler executor) {
        this.executor = executor;
    }

    public void setCloser(SocketCloseHandler closer) {
        this.closer = closer;
    }

    public void setWebServerLogger(ServerLogger serverLogger) {
        this.serverLogger = serverLogger;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ///封装SocketRequestContext对象
        SocketRequestContext context = new SocketRequestContext();
        context.setContext(ctx);
        context.setRequestData(msg);
        context.setAddress(ctx.channel().remoteAddress());
        heartbeatCount = 0;

        ///执行业务逻辑处理
        SocketResponse response = executor.execute(context);
        if (null != response) {
            String result = response.toJsonString();
            ///返回应答
            ctx.channel().writeAndFlush(result);

            ///是否需要关闭通道
            if (response.needCloseChannel())
                ctx.channel().close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            IdleState state = e.state();
            if (state == IdleState.READER_IDLE) {
                heartbeatCount++;
                if (heartbeatCount >= MAX_HEARTBEAT_COUNT) {
                    String message = "remote client trigger READ_IDLE event.";
                    logInfo(message);
                    socketClose(ctx);
                }
            } else if (state == IdleState.WRITER_IDLE) {
            } else if (state == IdleState.ALL_IDLE) {
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        socketClose(ctx);
        String message = "remote client trigger CHANNEL_INACTIVE event.";
        logInfo(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        socketClose(ctx);
        String message = "server throw an error: " + cause.getMessage();
        logError(message, cause);
    }

    private void socketClose(ChannelHandlerContext ctx) throws Exception {
        try {
            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
                    (future) -> {
                        if (!future.isSuccess()) {
                            //future.channel().close();
                            if (null != closer) {
                                closer.handle(ctx);
                            }
                        }
                    });
            ///ctx.close();

        } catch (Exception e) {
            String message = "server close socket throw an error: " + e.getMessage();
            logError(message, e);
        }
    }

    private void logInfo(String message) {
        ServerLoggerEntity entity = new ServerLoggerEntity();
        entity.setMessage(message);
        if (null != serverLogger)
            serverLogger.info(entity);
    }

    private void logError(String message, Throwable cause) {
        ServerLoggerEntity entity = new ServerLoggerEntity();
        entity.setMessage(message);
        entity.setCause(cause);
        if (null != serverLogger)
            serverLogger.error(entity);
    }
}