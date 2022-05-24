package cykuo.chat.message;

/**
 * @author chengyuankuo
 * @date 2022/05/24
 */
public class ChatResponseMessage extends Message{
    @Override
    public int getMessageType() {
        return CHAT_RESPONSE_MESSAGE;
    }

}
