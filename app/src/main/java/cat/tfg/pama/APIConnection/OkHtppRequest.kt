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

    fun POSTNannyfy(url: String, parameters: HashMap<String, *>, callback: Callback): Call {

        val mediaType = MediaType.parse("text/plain")
        val gson = Gson()

        val body = RequestBody.create(
            mediaType,
            gson.toJson(parameters).toString()
        )

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImYwYmZlYTA5MjJjZTk1YmMwNzAxMzg2OWQ2MjE1YzdjNTNkZGVkYWU2ZGVkOTk3MGU2MjViMDEyYTFmNzNiYTM4NTg4Y2M3NjExOGFjNWQ1In0.eyJhdWQiOiIxMDQxOTkiLCJqdGkiOiJmMGJmZWEwOTIyY2U5NWJjMDcwMTM4NjlkNjIxNWM3YzUzZGRlZGFlNmRlZDk5NzBlNjI1YjAxMmExZjczYmEzODU4OGNjNzYxMThhYzVkNSIsImlhdCI6MTU4ODQxODE5MiwibmJmIjoxNTg4NDE4MTkyLCJleHAiOjE2MTk5NTQxOTIsInN1YiI6IjUzNDY3Iiwic2NvcGVzIjpbIioiXX0.POn7m3XbMkK_0UJZHNdvKU2ATkqL_bEwAXdgCg09vuhUIcVNbq3Su59fSaqZh2GN7M8jH-kF8tz-HzMMND60CVt9Y1kk5Q8ENOZ4g5_-BtJi78PCf8qbSIegWY0ioeN-YPDdRzM4xYgwHlXHHLg1D79JZHxjX2NeuVuBasBrOK8z_FytkRi0tHarO0iNRh7kgFY-9tYLeRuhuKgqRbDmeuR6ZACWr0eXEwb5px9m3UEq1vtSh83PU4EFgUXJh_k6tyoCI2D3gqwUHowsFdM354ZFnkqXTisYanuvnKcPfFMVTpEkOOy2X35XzQC07jc4cIT1C5n5ZHVI377a_jQu5hMBLVvhXheVeZxo_zcBw6uIAJxmQL6Rg5bnI8ByfwaCxPFZYVjXARpszzaFn868lka3VtmloSlHY-uNW0xS-3r8KchTyUmTqXbCGq2F5DuBWPoY1P5PsIIK84RqXxPaRbmPg_zscNqkNk3J1Pv7kzoKKWpHjEMuQM2HohCuf0PcFpMQ8a70hxm4r35Ma9BzSB5e7rah0pzxXKImdaudaenuLTAe95Ez4RXOXJxFdkMeNrWeLug7igpNFmO_Ie1ZkE1ZMDD-yqhaFct13w88D0WjQOO307SZubL9eugr6ZEVzBbMMgjI3TO9A8QIon18r7WBr06DASuoC4ubm827qXk")
            .addHeader("Content-Type", "text/plain")
            .post(body)
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