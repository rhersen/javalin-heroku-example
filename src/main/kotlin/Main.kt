import io.javalin.Javalin
import io.javalin.http.Context

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val app = Javalin.create().start(herokuAssignedPort)
        app.get("/") { ctx -> ctx.result("Hello World") }
    }

    private val herokuAssignedPort: Int
        get() {
            val processBuilder = ProcessBuilder()
            return if (processBuilder.environment()["PORT"] != null) {
                processBuilder.environment()["PORT"]!!.toInt()
            } else 7000
        }
}
