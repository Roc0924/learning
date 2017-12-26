package com.roc.ui.client;



/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/12/25
 * Time                 : 10:32
 * Description          :
 */
public class Person {

    private Long id;

    private String name;

    public Person() {
        super();
    }

    public Person(String name) {
        super();
        this.name = name;
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
}
