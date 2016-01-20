package com.goku.im.net.socket.server.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by moueimei on 15/12/26.
 */
public class ImBinaryEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] buffer = msg.getBytes("UTF-8");
        int length = buffer.length;
        out.writeByte(ImBinaryUtil.START_FLAG);
        out.writeInt(length);
        out.writeBytes(buffer);
        out.writeByte(ImBinaryUtil.END_FLAG);
    }
}