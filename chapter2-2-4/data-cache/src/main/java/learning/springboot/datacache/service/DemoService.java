package learning.springboot.datacache.service;

import learning.springboot.datacache.entity.Person;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/21
 * Time                 : 14:48
 * Description          :
 */
public interface DemoService {
    public Person save(Person person);
    public void remove(Long id);
    public Person findOne(Person person);
}
