package cat.tfg.pama.Chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class AdapterMensajes: RecyclerView.Adapter<MessageViewHolder>() {

    private val listMensaje: MutableList<MessageReceive> = ArrayList()

    fun addMensaje(m: MessageReceive) {
        listMensaje.add(m)
        notifyItemInserted(listMensaje.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MessageViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = listMensaje[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return listMensaje.size
    }
}