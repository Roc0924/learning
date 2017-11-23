package hello.world

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/7
 * Time                 : 20:50
 * Description          :
 */

class Greeter(val name: String) {
    fun greet() {
        println("Hello, $name!")
    }
}

fun main(args: Array<String>) {
    Greeter(args[0]).greet()
}