package cat.tfg.pama.APIConnection

import com.google.gson.Gson
import okhttp3.*
import java.util.HashMap

object OkHttpRequest {

    private var client = OkHttpClient()
    private var authorization = "";

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
            .addHeader("Authorization", authorization)
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
            .addHeader("Authorization", authorization)
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
            .addHeader("Authorization", authorization)
            .method("DELETE", null)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    //companion object {
    //    val JSON = MediaType.parse("application/json; charset=utf-8")
    //}

    fun setAccesToken(access_token: String){
        authorization = "Bearer " + access_token
    }
}