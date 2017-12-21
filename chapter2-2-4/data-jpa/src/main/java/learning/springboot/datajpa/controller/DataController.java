package learning.springboot.datajpa.controller;

import learning.springboot.datajpa.entity.Person;
import learning.springboot.datajpa.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/20
 * Time                 : 16:42
 * Description          :
 */
@RestController
public class DataController {
    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/save")
    public Person save(String name, String address, Integer age){
        Person p = personRepository.save(new Person(null, name, age, address));
        return p;
    }
    /** * 测试 findByAddress */
    @RequestMapping("/q1")
    public List<Person> q1(String address){
        List<Person> people = personRepository.findByAddress(address);
        return people;
    }
    /** * 测试 findByNameAndAddress */
    @RequestMapping("/q2")
    public Person q2(String name, String address){
        Person people = personRepository.findByNameAndAddress(name, address);
        return people;
    }
    /** * 测试 withNameAndAddressQuery */
    @RequestMapping("/q3")
    public Person q3(String name, String address){
        Person p = personRepository.withNameAndAddressQuery(name, address);
        return p;
    }
    /** * 测试 withNameAndAddressNamedQuery */
    @RequestMapping("/q4")
    public Person q4(String name, String address){
        Person p = personRepository.withNameAndAddressNamedQuery(name, address);
        return p;
    }
    /** * 测试 排序 */
    @RequestMapping("/sort")
    public List<Person> sort(){
        List<Person> people = personRepository.findAll(new Sort(Sort.Direction.ASC," age"));
        return people;
    } /** * 测试 分页 */
    @RequestMapping("/page")
    public Page<Person> page(){
        Page<Person> pagePeople = personRepository.findAll(new PageRequest(1, 2));
        return pagePeople;
    }


}
