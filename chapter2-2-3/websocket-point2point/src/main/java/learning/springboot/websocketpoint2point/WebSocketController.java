package learning.springboot.websocketpoint2point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/18
 * Time                 : 14:32
 * Description          :
 */
@Controller
public class WebSocketController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;



    @MessageMapping("/chat")
    public void handleChat(Principal principal, String msg) {
        if (principal.getName().equals("wzp")) {
            messagingTemplate.convertAndSendToUser(
                    "roc",
                    "/queue/notifications",
                    principal.getName() + "-send:" + msg
            );
        } else {
            messagingTemplate.convertAndSendToUser(
                    "wzp",
                    "/queue/notifications",
                    principal.getName() + "-send:" +msg
            );
        }

    }

}
