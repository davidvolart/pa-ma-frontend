package cat.tfg.pama.APIConnection

import android.content.Context
import cat.tfg.pama.Session.Session
import okhttp3.*
import java.util.HashMap

class OkHttpRequest(var session: Session?) {

    private var client = OkHttpClient()

    companion object {
        private var single_instance: OkHttpRequest? = null
        fun getInstance(cntx: Context?): OkHttpRequest? {
            if (single_instance == null) {
                single_instance = OkHttpRequest(Session.getInstance(cntx))
            }
            return single_instance
        }
    }

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
            .addHeader("Authorization", session?.getAccessToken()!!)
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
            .addHeader("Authorization", session?.getAccessToken()!!)
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
            .addHeader("Authorization", session?.getAccessToken()!!)
            .method("DELETE", null)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }
}