package cat.tfg.pama

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject


interface Helper {

    fun getResponseMessage(response: Response): String? {
        var message = getResponseKeyValue(response, "message")
        return message;
    }

    fun getResponseAccessToken(response: Response): String? {
        return getResponseKeyValue(response, "access_token")
    }

    fun getChild(response: Response): JSONObject {

        var response_string = response.body()?.string();
        val json = JSONObject(response_string)
        var child = json.getJSONObject("child")

        return child;
    }

    fun getVaccines(response: Response): JSONArray {
        var response_string = response.body()?.string();
        val json = JSONObject(response_string)
        var vaccines = json.getJSONArray("vaccines")

        return vaccines;
    }

    fun getExpenses(response: Response): JSONArray {
        var response_string = response.body()?.string();
        val json = JSONObject(response_string)
        var expenses = json.getJSONArray("expenses")

        return expenses;
    }

    fun getFamilyCode(response: Response): String? {
        val family_code = getResponseKeyValue(response, "family_code")
        return family_code;
    }

    fun getResponseKeyValue(response: Response, key: String): String? {

        val response_json = response.body()?.string()?.toString();
        val gson = Gson()
        val response_map: Map<String, String> =
            gson.fromJson(response_json, object : TypeToken<Map<String, String>>() {}.type)
        return response_map.get(key)?.toString()
    }
}