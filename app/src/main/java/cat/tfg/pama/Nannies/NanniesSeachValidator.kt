
class NanniesSearchValidator(end_time: String, arrival_time: String, date: String){

    var end_time = end_time
    var arrival_time = arrival_time
    var date = date
/*
    public fun fildsAreValid(): Boolean{

        if(checkDatesAreNonEmpty(date,arrival_time, end_time)){

            val arrival_hour = arrival_time.substring(0,2).toInt()
            val arrival_minutes = arrival_time.substring(3).toInt()

            val end_hour = end_time.substring(0,2).toInt()
            val end_minutes = end_time.substring(3).toInt()

            if(checkTimesAreValid(arrival_hour,arrival_minutes,end_hour,end_minutes)){
                return true
            }
            return false
        }
        return false
    }
*/
    public fun areTimesValid(): Boolean{

        val arrival_hour = arrival_time.substring(0,2).toInt()
        val arrival_minutes = arrival_time.substring(3).toInt()

        val end_hour = end_time.substring(0,2).toInt()
        val end_minutes = end_time.substring(3).toInt()

        if(end_hour < arrival_hour){
            //showMessage("El campo hora de fin ha de ser posterior a la de inicio")
            return false
        }else if(end_hour == arrival_hour && end_minutes < arrival_minutes ){
            //showMessage("El campo hora de fin ha de ser posterior a la de inicio")
            return false
        }
        return true
    }

    public fun areDatesNonEmpty():Boolean{

        if(date.equals("")){
            //showMessage("El campo fecha es obligatorio")
            return false
        }else if(arrival_time.equals("")){
            //showMessage("El campo hora de inicio es obligatorio")
            return false
        }else if(end_time.equals("")){
            //showMessage("El campo hora de fin es obligatorio")
            return false
        }
        return true
    }



}

