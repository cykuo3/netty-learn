package cykuo.chat.protocol;

import cykuo.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author chengyuankuo
 * @date 2022/05/24
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        log.debug("encode------------------");
        //1 魔数
        byteBuf.writeBytes(new byte[]{1,2,3,4});
        //2 版本
        byteBuf.writeByte(1);
        //3 序列化方式 自己约定
        byteBuf.writeByte(0);
        //4 指令类型
        byteBuf.writeByte(message.getMessageType());
        //5 序列ID
        byteBuf.writeInt(message.getSequenceId());
        //无意义字节 占位
        byteBuf.writeByte(0xff);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        out.writeObject(message);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        //6 内容长度
        byteBuf.writeInt(bytes.length);
        //7 内容
        byteBuf.writeBytes(bytes);
    }

    @Override
    public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.debug("decode--------------------");
        int magicNum = byteBuf.readInt();
        byte version = byteBuf.readByte();
        byte serializeType = byteBuf.readByte();
        byte messageType = byteBuf.readByte();
        int sequenceId = byteBuf.readInt();
        byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes,0,length);
        if (serializeType == 0){
            ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = inStream.readObject();
            if(obj instanceof Message msg){
                //得到message对象
                log.debug("{},{},{},{},{},{}",magicNum,version,serializeType,messageType,sequenceId,length);
                log.debug("message={}",msg);
                list.add(msg);
            }
        }
    }
}
