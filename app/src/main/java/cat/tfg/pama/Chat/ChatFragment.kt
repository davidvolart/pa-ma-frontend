package cat.tfg.pama.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import cat.tfg.pama.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chat.*

data class Message(var sender: String = "", var content: String = "", var time: String = "")

class ChatFragment: Fragment() {

    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var adapter:AdapterMensajes? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Chat")

        database = FirebaseDatabase.getInstance();
        databaseReference = database!!.getReference("chat");

        adapter = AdapterMensajes();
        var linearLayoutManager = LinearLayoutManager(context);

        (rvMensajes as RecyclerView).layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        (rvMensajes as RecyclerView).adapter = adapter

        btnEnviar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val m = Message("David", txtMensaje.text.toString(), "19:30")
                databaseReference!!.push().setValue(m)
                txtMensaje.setText("")
            }
        })

        adapter!!.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                scrollBar()
            }
        })

        databaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val message = dataSnapshot.getValue(Message::class.java)!!
                adapter!!.addMensaje(message)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun scrollBar(){
        if(adapter!!.itemCount > 0){
            (rvMensajes as RecyclerView).scrollToPosition(adapter!!.itemCount-1);
        }

    }
}