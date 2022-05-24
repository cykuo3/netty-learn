package cykuo.chat.message;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author chengyuankuo
 * @date 2022/05/24
 */
public abstract class Message implements Serializable {

    public static final int LOGIN_REQUEST_MESSAGE = 0;
    public static final int LOGIN_RESPONSE_MESSAGE = 1;
    public static final int CHAT_REQUEST_MESSAGE = 2;
    public static final int CHAT_RESPONSE_MESSAGE = 3;

    private int sequenceId;

    public abstract int getMessageType();

    public int getSequenceId(){
        return sequenceId;
    }
}
