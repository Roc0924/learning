package learning.springcloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/2
 * Time                 : 9:58
 * Description          : 计算服务接口
 */
@FeignClient("compute-service")
public interface ComputeClient {
    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    Integer add(@RequestParam Integer a, @RequestParam Integer b);
}
