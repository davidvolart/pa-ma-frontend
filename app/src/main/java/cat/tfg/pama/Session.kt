package cat.tfg.pama

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


object Session{
    /*
    var access_token:String = ""
        set(value) {
            System.out.println(value)
        }
    var family_code:String? = null
    var user_name:String? = null
    var user_email:String? = null
    var firebase_database_path:String? = null
    */
/*
    fun getFirebaseDatabasePath(): String {
        if(firebase_database_path == null){
            return generateFirebaseDatabasePath()
        }else{
            return firebase_database_path!!
        }
    }
*/
    /*
    private fun generateFirebaseDatabasePath(): String{
        val filtered = ".#$[]"
        return family_code!!.filterNot { filtered.indexOf(it) > -1 }
    }
     */
}

class Session2 {

    companion object {
        private var prefs: SharedPreferences? = null
        private var single_instance: Session2? = null
        fun getInstance(cntx: Context?): Session2? {
            // TODO Auto-generated constructor stub
            if (single_instance == null) {
                single_instance = Session2()
                prefs = PreferenceManager.getDefaultSharedPreferences(cntx)
            }
            return single_instance
        }
    }

    fun setUseName(usename: String?) {
        prefs!!.edit().putString("usename", usename).commit()
    }

    fun getUseName(): String? {
        return prefs!!.getString("usename", null)
    }

    fun setAccessToken(accesstoken: String?) {
        prefs!!.edit().putString("accesstoken", accesstoken).commit()
    }

    fun getAccessToken(): String? {
        return prefs!!.getString("accesstoken", "")
    }

    fun setFamilyCode(familycode: String?) {
        prefs!!.edit().putString("familycode", familycode).commit()
    }

    fun getFamilyCode(): String? {
        return prefs!!.getString("familycode", null)
    }

    fun setUserEmail(familycode: String?) {
        prefs!!.edit().putString("useremail", familycode).commit()
    }

    fun getUserEmail(): String? {
        return prefs!!.getString("useremail", null)
    }

    fun setFirebasePath(firebasepath: String?) {
        prefs!!.edit().putString("firebasepath", firebasepath).commit()
    }

    fun getFirebaseDatabasePath(): String? {
        val firebasepath = prefs!!.getString("firebasepath", null)
        if(firebasepath == null){
            return generateFirebaseDatabasePath()
        }else{
            return firebasepath
        }
    }

    private fun generateFirebaseDatabasePath(): String{
        val filtered = ".#$[]"
        return getFamilyCode()!!.filterNot { filtered.indexOf(it) > -1 }
    }
}

