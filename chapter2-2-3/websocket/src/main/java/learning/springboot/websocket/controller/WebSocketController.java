package learning.springboot.websocket.controller;

import learning.springboot.websocket.message.WebSocketMessage;
import learning.springboot.websocket.message.WebSocketResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/18
 * Time                 : 14:32
 * Description          :
 */
@Controller
public class WebSocketController {



    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public WebSocketResponse say(WebSocketMessage webSocketMessage) throws Exception {
        Thread.sleep(3000);

        System.out.println(webSocketMessage.getName());
        WebSocketResponse message = new WebSocketResponse("Welcome, " + webSocketMessage.getName() + "!");
        return message;
    }
}
