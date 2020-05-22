package cat.tfg.pama

object CurrentUser{

    var access_token:String = ""
    var family_code:String? = null
    var user_name:String? = null
    var firebase_database_path:String? = null

    fun getFirebaseDatabasePath(): String {
        if(firebase_database_path == null){
            return generateFirebaseDatabasePath()
        }else{
            return firebase_database_path!!
        }
    }

    private fun generateFirebaseDatabasePath(): String{
        val filtered = ".#$[]"
        return family_code!!.filterNot { filtered.indexOf(it) > -1 }
    }
}