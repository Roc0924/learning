package learning.springboot.springtest;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/25
 * Time                 : 10:35
 * Description          :
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
