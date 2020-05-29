import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.javalin.Javalin
import io.javalin.plugin.json.JavalinJackson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.log

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val app = Javalin.create().start(herokuAssignedPort)
        app.get("/") { ctx ->
            try {
                ctx.result(athlete().ytd_run_totals.distance.toString())
            } catch (e: Exception) {
                ctx.result(e.message ?: e.toString())
            }
        }
    }

    private fun athlete(): RootObject {
        val url = URL("https://www.strava.com/api/v3/athletes/13317026/stats")
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        try {
            con.setRequestProperty("Authorization", "Bearer " + ProcessBuilder().environment()["BEARER"])
            val content = StringBuilder()
            BufferedReader(InputStreamReader(con.inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    content.append(line)
                    line = reader.readLine()
                }
                val mapper = ObjectMapper().registerModule(KotlinModule())
                JavalinJackson.configure(mapper)
                return mapper.readValue(content.toString(), RootObject::class.java)
            }
        } finally {
            println(con.responseCode)
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
