package com.goku.im.net.socket.server.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by milo on 15/12/26.
 */
public class ImBinaryDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if(in.readableBytes() < 5)
            return;

        in.markReaderIndex();
        byte startFlag = in.readByte();
        if(startFlag != ImBinaryUtil.START_FLAG)
        {
            in.resetReaderIndex();
            return;
        }

        int dataLen = in.readInt();
        if(in.readableBytes() < (dataLen + 1))
        {
            in.resetReaderIndex();
            return;
        }

        byte[] buffer = new byte[dataLen];
        in.readBytes(buffer);

        byte endFlag = in.readByte();
        if (endFlag != ImBinaryUtil.END_FLAG) {
            in.resetReaderIndex();
            return;
        }

        out.add(new String(buffer, "UTF-8"));
    }
}
