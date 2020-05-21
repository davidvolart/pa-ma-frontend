package cat.tfg.pama.APIConnection

import cat.tfg.pama.CurrentUser
import com.google.gson.Gson
import okhttp3.*
import java.util.HashMap

object OkHttpRequest {

    private var client = OkHttpClient()

    fun POST(url: String, parameters: HashMap<String, *>, callback: Callback): Call {
        val builder = FormBody.Builder()
        val it = parameters.entries.iterator()
        while (it.hasNext()) {
            val pair = it.next() as Map.Entry<*, *>
            builder.add(pair.key.toString(), pair.value.toString())
        }

        val formBody = builder.build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Authorization", CurrentUser.access_token)
            .post(formBody)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Authorization", CurrentUser.access_token)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    fun DELETE(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Authorization", CurrentUser.access_token)
            .method("DELETE", null)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}