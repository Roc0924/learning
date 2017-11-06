package learning.kotlin.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/6
 * Time                 : 13:48
 * Description          :
 */
@RestController
class HelloKotlin {
    @RequestMapping(value = "/sayHello", method = arrayOf(RequestMethod.GET))
    fun sayHello() : String {
        return "hello,kotlin"
    }
}