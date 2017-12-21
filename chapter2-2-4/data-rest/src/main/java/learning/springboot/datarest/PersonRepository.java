package learning.springboot.datarest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/20
 * Time                 : 17:19
 * Description          :
 */
@RepositoryRestResource(path="people")
public interface PersonRepository extends JpaRepository<Person, Long>{
    @RestResource(path = "nameStartsWith",rel = "nameStartsWith")
    List<Person> findByNameStartsWith(@Param("name") String name);
}


