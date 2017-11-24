package leanning.spingcloud.zipkintrade.controller;

import leanning.spingcloud.zipkintrade.client.ItemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/24
 * Time                 : 17:09
 * Description          :
 */
@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    ItemClient itemClient;

    @RequestMapping(value = "/trade", method = RequestMethod.GET)
    public Object getTrade(@RequestParam(name = "id") String id) {

        return itemClient.getItemInfoById(id);
    }
}
