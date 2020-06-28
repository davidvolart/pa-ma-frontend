package cat.tfg.pama.Calendar

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import java.util.*
import kotlin.collections.HashMap

class CalendarProviderClient {

     fun addEventToCalendar(context: Context, parameters: HashMap<String, String>): Long{
        val uri: Uri? = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI.buildUpon().build(), getEvent(parameters))
        val event_id =  uri!!.lastPathSegment!!.toLong()
        return event_id
    }

    fun deleteEvent(context: Context, calendar_provider_event_id: Long){
        val cr: ContentResolver = context.contentResolver
        val deleteUri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, calendar_provider_event_id)
        val rows: Int = cr.delete(deleteUri, null, null)
    }

    fun modifyEvent(context: Context, parameters: HashMap<String, String>, calendar_provider_event_id: Long ){
        val updateUri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, calendar_provider_event_id)
        val rows: Int = context.contentResolver.update(updateUri, getEvent(parameters), null, null)
    }

    private fun getEvent(parameters: HashMap<String, String>): ContentValues{
        return ContentValues().apply {
            put(CalendarContract.Events.DTSTART, getStartInMilis(parameters["date"]))
            put(CalendarContract.Events.DTEND, getStartInMilis(parameters["date"]))
            put(CalendarContract.Events.TITLE, parameters["name"])
            put(CalendarContract.Events.DESCRIPTION, parameters["description"])
            put(CalendarContract.Events.CALENDAR_ID, "3")
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID())
        }
    }

    private fun getStartInMilis(date: String?):Long {
        val parts = date!!.split('/')
        return Calendar.getInstance().run {
            set(parts[2].toInt(), parts[1].toInt()-1, parts[0].toInt(), 7, 30)
            timeInMillis
        }
    }
}