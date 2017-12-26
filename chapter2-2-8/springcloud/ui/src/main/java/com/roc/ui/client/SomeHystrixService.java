package com.roc.ui.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/25
 * Time                 : 18:03
 * Description          :
 */
@Service
public class SomeHystrixService {
    @Autowired
    RestTemplate restTemplate;

    public String getSome() {
        return restTemplate.getForObject("http://some/getsome", String.class);
    }

    public String fallbackSome() {
        return "some service事障";
    }
}
