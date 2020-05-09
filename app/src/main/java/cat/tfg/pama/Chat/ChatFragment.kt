package cat.tfg.pama.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.tfg.pama.R
import kotlinx.android.synthetic.main.fragment_chat.*

data class Message(var sender: String, var content: String, var time: String)

class ChatFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Chat")

        var adapter = AdapterMensajes();
        var linearLayoutManager = LinearLayoutManager(context);

        (rvMensajes as RecyclerView).layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        (rvMensajes as RecyclerView).adapter = adapter

        btnEnviar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                adapter.addMensaje(Message("David", txtMensaje.text.toString(), "17:20"))
            }
        })

    }
}