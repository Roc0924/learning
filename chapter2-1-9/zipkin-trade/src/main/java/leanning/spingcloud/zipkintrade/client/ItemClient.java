package leanning.spingcloud.zipkintrade.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/24
 * Time                 : 17:10
 * Description          :
 */
@FeignClient("zipkin-item")
public interface ItemClient {

    @RequestMapping(value = "/item/item", method = RequestMethod.GET)
    Object getItemInfoById(@RequestParam(name = "id") String id);
}
