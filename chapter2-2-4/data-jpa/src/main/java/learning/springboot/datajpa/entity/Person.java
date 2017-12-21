package learning.springboot.datajpa.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/20
 * Time                 : 16:29
 * Description          :
 */
@Entity
@NamedQuery(name="Persion.withNameAndAddressNamedQuery", query = "select p from Person p where p.name=?1 and address=?2")
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer age;
    private String address;

    public Person() {
        super();
    }

    public Person(Long id, String name, Integer age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
