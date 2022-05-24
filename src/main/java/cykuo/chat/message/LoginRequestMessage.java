package cykuo.chat.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chengyuankuo
 * @date 2022/05/24
 */
@Data
@AllArgsConstructor
public class LoginRequestMessage extends Message {
    private String username;

    private String password;

    private String nickname;

    @Override
    public int getMessageType() {
        return LOGIN_REQUEST_MESSAGE;
    }

}
