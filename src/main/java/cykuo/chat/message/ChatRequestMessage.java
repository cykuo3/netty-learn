package cykuo.chat.message;

/**
 * @author chengyuankuo
 * @date 2022/05/24
 */
public class ChatRequestMessage extends Message{
    @Override
    public int getMessageType() {
        return CHAT_REQUEST_MESSAGE;
    }

}
