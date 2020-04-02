package cat.tfg.pama

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.json.JSONObject



interface Helper {

    fun getResponseMessage(response: Response): String {
        return getResponseKeyValue(response,"message")
    }

    fun getResponseAccessToken(response: Response): String{
        return getResponseKeyValue(response,"access_token")
    }

    fun hasChild(response: Response): JSONObject{

        var response_string = response.body()?.string();
        val json = JSONObject(response_string)
        var child = json.getJSONObject("child")

        return child;
    }


    fun getResponseKeyValue(response: Response,key: String): String{
/*
        var response_string = response.body()?.string();
        val json = JSONObject(response_string)

        var message = json.getJSONObject("key")
 */

            val response_json = response.body()?.string().toString();
            val gson = Gson()
            val response_map: Map<String, String> =
                gson.fromJson(response_json, object : TypeToken<Map<String, String>>() {}.type)
            return response_map.get(key).toString()

    }



}