package learning.springboot.websocket.message;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/18
 * Time                 : 14:29
 * Description          :
 */
public class WebSocketResponse {
    private String responseMessage;

    public WebSocketResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
