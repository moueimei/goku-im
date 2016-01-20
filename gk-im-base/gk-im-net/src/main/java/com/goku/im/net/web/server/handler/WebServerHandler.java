package com.goku.im.net.web.server.handler;

import com.goku.im.net.web.server.action.ActionResolve;
import com.goku.im.net.web.server.context.impl.HttpRequestContext;
import com.goku.im.net.web.server.context.impl.WebSocketRequestContext;
import com.goku.im.net.web.server.logger.WebServerLogger;
import com.goku.im.net.web.server.logger.WebServerLoggerEntity;
import com.goku.im.net.web.server.proxy.impl.ActionDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 处理接收到的数据
 */
public class WebServerHandler extends ChannelInboundHandlerAdapter {
    private WebSocketServerHandshaker handshaker;
    private Class<?> closeHandlerClass;
    private ActionResolve websocketActionResolve;
    private ActionResolve httpActionResolve;
    private WebServerLogger serverLogger;

    public WebServerHandler() {
    }

    public void setWebSocketCloseHandlerClass(Class<?> handlerClass) {
        this.closeHandlerClass = handlerClass;
    }

    public void setWebSocketActionResolve(ActionResolve actionResolve) {
        this.websocketActionResolve = actionResolve;
    }

    public void setHttpActionResolve(ActionResolve actionResolve) {
        this.httpActionResolve = actionResolve;
    }

    public void setWebServerLogger(WebServerLogger serverLogger) {
        this.serverLogger = serverLogger;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest)
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        else if (msg instanceof WebSocketFrame)
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        if ("/favicon.ico".equals(request.getUri())) {
            sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
            return;
        }

        ///判断是否为websocket协议
        boolean isWebSocketProtocol = false;
        String upgrade = request.headers().get(Names.UPGRADE);
        String connection = request.headers().get(Names.CONNECTION);
        if ("websocket".equals(upgrade) && "Upgrade".equals(connection))
            isWebSocketProtocol = true;

        if (isWebSocketProtocol) {
            // Handshake
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(request), null, false);
            handshaker = wsFactory.newHandshaker(request);
            if (handshaker == null)
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            else
                handshaker.handshake(ctx.channel(), request);
        } else {
            if (is100ContinueExpected(request))
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));

            ///处理请求
            HttpRequestContext context = new HttpRequestContext(request);
            context.setRemoteAddress(ctx.channel().remoteAddress());
            ActionDispatcher dispatcher = ActionDispatcher.getInstance();
            dispatcher.setActionResolve(httpActionResolve);
            String rspdata = dispatcher.execute(context);
            if (null == rspdata)
                rspdata = "";

            ///返回应答
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(rspdata.getBytes()));
            response.headers().set(CONTENT_TYPE, "text/html; charset=utf-8");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            sendHttpResponse(ctx, request, response);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof CloseWebSocketFrame) {
            String message = "web server receive CloseWebsocket code from remote client.";
            logInfo(ctx, message);

            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            String message = "web server receive PING code from remote client.";
            logInfo(ctx, message);

            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            String message = "web server receive NotTextFrame from remote client.";
            logInfo(ctx, message);
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }

        String rspdata = "";
        try {
            ///装饰WebSocketRequestContext
            WebSocketRequestContext context = new WebSocketRequestContext(frame);
            context.setRemoteAddress(ctx.channel().remoteAddress());
            context.setChannelContext(ctx);

            ///处理请求
            ActionDispatcher dispatcher = ActionDispatcher.getInstance();
            dispatcher.setActionResolve(websocketActionResolve);
            rspdata = dispatcher.execute(context);
            if (null == rspdata)
                rspdata = "";
        } catch (Exception e) {
            logInfo(ctx, e.getMessage());
        }
        ///返回应答
        ctx.channel().writeAndFlush(new TextWebSocketFrame(rspdata));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }

        boolean keepAlive = isKeepAlive(req);
        if (!keepAlive) {
            ctx.write(res).addListener(ChannelFutureListener.CLOSE);
        } else {
            res.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.write(res);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        Channel channel = ctx.channel();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            IdleState state = e.state();
            if (state == IdleState.READER_IDLE) {
                String message = "remote client trigger READ_IDLE event.";
                logInfo(ctx, message);
                channel.close();
            } else if (state == IdleState.WRITER_IDLE) {
            } else if (state == IdleState.ALL_IDLE) {
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String message = "remote client trigger CHANNEL_INACTIVE event.";
        logInfo(ctx, message);

        websocketClose(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String message = "webserver throw an error: " + cause.getMessage();
        logInfo(ctx, message);
        ctx.close();
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        return "ws://" + req.headers().get(HOST);
    }

    private void websocketClose(ChannelHandlerContext ctx) throws Exception {
        try {
            WebSocketCloseHandler closeHandler = (WebSocketCloseHandler) closeHandlerClass.newInstance();
            closeHandler.handle(ctx);
        } catch (Exception e) {
            String message = "webserver close websocket throw an error: " + e.getMessage();
            logError(ctx, message, e);
        }
    }

    private void logInfo(ChannelHandlerContext context, String message) {
        WebServerLoggerEntity entity = new WebServerLoggerEntity();
        entity.setContext(context);
        entity.setMessage(message);
        if (null != serverLogger)
            serverLogger.info(entity);
    }

    private void logError(ChannelHandlerContext context, String message, Throwable cause) {
        WebServerLoggerEntity entity = new WebServerLoggerEntity();
        entity.setContext(context);
        entity.setMessage(message);
        entity.setCause(cause);
        if (null != serverLogger)
            serverLogger.error(entity);
    }
}