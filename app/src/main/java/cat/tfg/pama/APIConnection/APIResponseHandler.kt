package cat.tfg.pama.APIConnection

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

interface APIResponseHandler {

    fun getExpensesByMonth(response: Response): JSONObject {
        val response_string = response.body()!!.string();
        val json = JSONObject(response_string)
        return json.getJSONObject("expenses")
    }

    fun getResponseMessage(response: Response): String? {
        return getResponseKeyValue(response, "message")
    }

    fun getResponseAccessToken(response: Response): JSONObject {
        val response_string = response.body()!!.string();
        val json = JSONObject(response_string)
        return json.getJSONObject("response")
    }

    fun getChild(response: Response): JSONObject {
        val response_string = response.body()!!.string();
        val json = JSONObject(response_string)
        return json.getJSONObject("child")
    }

    fun getVaccines(response: Response): JSONArray {
        val response_string = response.body()!!.string();
        val json = JSONObject(response_string)
        return json.getJSONArray("vaccines")
    }

    fun getExpenses(response: Response): JSONArray {
        val response_string = response.body()!!.string();
        val json = JSONObject(response_string)
        return json.getJSONArray("expenses")
    }

    fun getTasks(response: Response): JSONArray {
        val response_string = response.body()!!.string();
        val json = JSONObject(response_string)
        return json.getJSONArray("tasks")
    }

    fun getResponseKeyValue(response: Response, key: String): String? {

        val response_json = response.body()?.string()?.toString();
        val gson = Gson()
        val response_map: Map<String, String> =
            gson.fromJson(response_json, object : TypeToken<Map<String, String>>() {}.type)
        return response_map.get(key)?.toString()
    }
}