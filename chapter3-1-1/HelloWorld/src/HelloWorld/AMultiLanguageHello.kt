package HelloWorld

/**
 * Create with IntelliJ IDEA
 * Author               : wangzhenpeng
 * Date                 : 2017/11/7
 * Time                 : 20:45
 * Description          :
 */
fun main(args: Array<String>) {
    val language = if (args.isEmpty()) "EN" else args[0]

    println(when (language) {
        "EN" -> "Hello!"
        "FR" -> "Salut!"
        "IT" -> "Ciao!"
        else -> "Sorry, I can't greet you in $language yet"
    })
}