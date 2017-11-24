package leanning.springcloud.zipkinitem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/24
 * Time                 : 16:49
 * Description          :
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public Object getItemInfoById(@RequestParam(name = "id") String id) {
        Map<String, String> itemMap = new HashMap<>();
        itemMap.put("name", "test");
        itemMap.put("id", id);
        itemMap.put("price", "1000");
        return itemMap;
    }
}
