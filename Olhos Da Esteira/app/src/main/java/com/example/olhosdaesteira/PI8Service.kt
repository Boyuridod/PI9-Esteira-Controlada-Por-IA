import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ApiService(private val baseUrl: String) {
    private val client = OkHttpClient()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun postSetPointTemperature(temperature: Int, callback: (Boolean, String?) -> Unit) {
        val json = JSONObject().put("temperature", temperature).toString()
        val body = json.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url("$baseUrl/set-point/temperature")
            .post(body)
            .build()
        enqueue(request, callback)
    }

    fun lamp1SwitchOn(state: Boolean, callback: (Boolean, String?) -> Unit) {
        val json = JSONObject().put("state", state).toString()
        val body = json.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url("$baseUrl/lamp1")
            .post(body)
            .build()
        enqueue(request, callback)
    }

    fun getTemperature(callback: (Boolean, String?) -> Unit) {
        val request = Request.Builder()
            .url("$baseUrl/temperature")
            .get()
            .build()
        enqueue(request, callback)
    }

    private fun enqueue(request: Request, callback: (Boolean, String?) -> Unit) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback(false, "HTTP ${it.code}")
                    } else {
                        callback(true, it.body?.string())
                    }
                }
            }
        })
    }
}