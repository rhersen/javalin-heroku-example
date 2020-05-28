import io.javalin.Javalin
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val app = Javalin.create().start(herokuAssignedPort)
        app.get("/") { ctx -> ctx.result(athlete()) }
    }

    private fun athlete(): String {
        val url = URL("https://www.strava.com/api/v3/athletes/13317026/stats")
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        con.setRequestProperty("Authorization", "Bearer " + ProcessBuilder().environment()["BEARER"])
        try {
            val content = StringBuilder()
            BufferedReader(InputStreamReader(con.inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    content.append(line)
                    line = reader.readLine()
                }
                return content.toString()
            }
        } catch (e: Exception) {
            return e.message ?: e.toString()
        }
    }

    private val herokuAssignedPort: Int
        get() {
            val processBuilder = ProcessBuilder()
            return if (processBuilder.environment()["PORT"] != null) {
                processBuilder.environment()["PORT"]!!.toInt()
            } else 7000
        }
}
