
class NanniesSearchValidator(var end_time: String, var arrival_time: String, var date: String){

    fun areTimesValid(): Boolean{

        val arrival_hour = arrival_time.substring(0,2).toInt()
        val arrival_minutes = arrival_time.substring(3).toInt()

        val end_hour = end_time.substring(0,2).toInt()
        val end_minutes = end_time.substring(3).toInt()

        if(end_hour < arrival_hour || (end_hour == arrival_hour && end_minutes < arrival_minutes)){
            return false
        }
        return true
    }

    fun areDatesNonEmpty():Boolean{
        if(date.equals("") || arrival_time.equals("") || end_time.equals("")){
            return false
        }
        return true
    }
}

