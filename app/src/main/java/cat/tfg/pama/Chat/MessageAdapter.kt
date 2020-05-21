package cat.tfg.pama.Chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MessageAdapter: RecyclerView.Adapter<MessageViewHolder>() {

    private val listMenssages: MutableList<MessageReceive> = ArrayList()

    fun addMensaje(m: MessageReceive) {
        listMenssages.add(m)
        notifyItemInserted(listMenssages.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MessageViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = listMenssages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return listMenssages.size
    }
}