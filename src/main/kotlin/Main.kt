import io.javalin.Javalin
import io.javalin.http.Context

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Javalin.create()["/", { ctx: Context -> ctx.result("hello kotlin") }].start(herokuAssignedPort)
    }

    private val herokuAssignedPort: Int
        private get() {
            val processBuilder = ProcessBuilder()
            return if (processBuilder.environment()["PORT"] != null) {
                processBuilder.environment()["PORT"]!!.toInt()
            } else 7000
        }
}
