package cat.tfg.pama.Chat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class MessageViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.card_view_mensajes, parent, false))  {

    var nombre: TextView
    var mensaje: TextView
    var hora: TextView
    var fotoMensajePerfil: CircleImageView
    private var fotoMensaje: ImageView

    init {
        nombre = itemView.findViewById(R.id.nombreMensaje)
        mensaje = itemView.findViewById(R.id.mensajeMensaje)
        hora = itemView.findViewById(R.id.horaMensaje)
        fotoMensajePerfil = itemView.findViewById(R.id.fotoPerfilMensaje)
        fotoMensaje = itemView.findViewById(R.id.mensajeFoto) as ImageView
    }

    fun bind(message: Message) {

        nombre.text = message.sender
        mensaje.text = message.content

        //val codigoHora: Long = message.time
        //val d = Date(codigoHora)
        //val sdf = SimpleDateFormat("hh:mm:ss a") //a pm o am
        //hora.text = sdf.format(d)

        hora.text = message.time

    }
}