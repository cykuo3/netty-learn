package cykuo.chat.message;

/**
 * @author chengyuankuo
 * @date 2022/05/24
 */
public class LoginResponseMessage extends Message{
    @Override
    public int getMessageType() {
        return LOGIN_RESPONSE_MESSAGE;
    }
}
