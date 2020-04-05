package cat.tfg.pama

import java.util.HashMap
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request

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
            .addHeader("Authorization", this.authorization)
            .post(formBody)
            .build()

        if(url.contains("personaldata")){
            System.out.println("aaaa")
        }
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }


    fun GET(url: String, callback: Callback): Call {
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Authorization", this.authorization)
            .build()

        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }


    //companion object {
    //    val JSON = MediaType.parse("application/json; charset=utf-8")
    //}

    fun setAccesToken(access_token: String){
        this.authorization = "Bearer " + access_token
    }
}