package cat.tfg.pama.Chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class MessageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.message_item, parent, false))  {

    var name: TextView
    var messageText: TextView
    var time: TextView
    var messagePhoto: ImageView

    init {
        name = itemView.findViewById(R.id.messageName)
        messageText = itemView.findViewById(R.id.messageText)
        time = itemView.findViewById(R.id.messageTime)
        messagePhoto = itemView.findViewById(R.id.messagePhoto) as ImageView
    }

    fun bind(message: MessageReceive) {

        name.text = message.sender
        messageText.text = message.content

        if(message.type == 1){
            messagePhoto.visibility= View.GONE
            messageText.visibility = View.VISIBLE
        }else{
            messagePhoto.visibility= View.VISIBLE
            messageText.visibility = View.GONE
            Glide.with(messagePhoto.context).load(message.url_photo).into(messagePhoto)
        }

        val timeCode: Long = message.time!!
        val d = Date(timeCode)
        val sdf = SimpleDateFormat("hh:mm a")
        time.text = sdf.format(d)
    }
}