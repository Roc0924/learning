package HelloWorld

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/7
 * Time                 : 20:38
 * Description          :
 */
fun main(args: Array<String>) {
    if(args.isEmpty()) {
        println("Please provide a name as a command-line argument")
        return
    }
    println("Hello ${args[0]}")
}