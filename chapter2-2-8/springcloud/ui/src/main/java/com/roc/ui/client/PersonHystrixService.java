package com.roc.ui.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.roc.ui.client.Person;
import com.roc.ui.client.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/25
 * Time                 : 18:00
 * Description          :
 */
@Service
public class PersonHystrixService {
    @Autowired
    PersonService personService;

    @HystrixCommand(fallbackMethod = "fallbackSave")
    public List<Person> save(String name) {
        return personService.save(name);
    }

    public List<Person> fallbackSave() {
        List<Person> list  = new ArrayList<>();
        Person p = new Person("Person Service 事障");
        list.add(p);
        return list;
    }
}
