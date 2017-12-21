package learning.springboot.datacache.repository;

import learning.springboot.datacache.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/21
 * Time                 : 12:51
 * Description          :
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
