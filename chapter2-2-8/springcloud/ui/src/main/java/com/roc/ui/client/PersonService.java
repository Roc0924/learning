package com.roc.ui.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/25
 * Time                 : 17:56
 * Description          :
 */
@FeignClient("person")
public interface PersonService {
    @RequestMapping(value = "/save", method = RequestMethod.POST
    , produces = MediaType.APPLICATION_JSON_VALUE)
    List<Person> save(@RequestBody String personName);
}
