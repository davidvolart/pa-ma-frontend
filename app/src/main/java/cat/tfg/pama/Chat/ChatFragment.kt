package cat.tfg.pama.Chat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

data class Message(var sender: String = "", var content: String = "", var type: Int, var url_photo: String? = null, var time: String = "")

class ChatFragment: Fragment() {

    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null

    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var adapter:AdapterMensajes? = null
    private val PHOTO_SEND = 1

    private val MESSAGE_TYPE_TEXT = 1
    private val MESSAGE_TYPE_PHOTO = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Chat")

        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.getReference("chat")
        storage = FirebaseStorage.getInstance()

        adapter = AdapterMensajes();
        var linearLayoutManager = LinearLayoutManager(context);

        (rvMensajes as RecyclerView).layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        (rvMensajes as RecyclerView).adapter = adapter

        btnEnviar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val m = Message("David", txtMensaje.text.toString(), MESSAGE_TYPE_TEXT,null,"19:30")
                databaseReference!!.push().setValue(m)
                txtMensaje.setText("")
            }
        })

        btnEnviarFoto.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.type = "image/*"
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_SEND)
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

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun scrollBar(){
        if(adapter!!.itemCount > 0){
            (rvMensajes as RecyclerView).scrollToPosition(adapter!!.itemCount-1);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {

            val filepath: Uri = data?.data!!
            storageReference = FirebaseStorage.getInstance().reference
            val fotoReferencia = storageReference!!.child("images/" + UUID.randomUUID().toString())

            fotoReferencia.putFile(filepath).addOnSuccessListener(activity!!) {
                fotoReferencia.downloadUrl.addOnCompleteListener () {taskSnapshot ->
                    var url = taskSnapshot.result.toString()
                    val m = Message("David","David te ha enviado una foto",MESSAGE_TYPE_PHOTO, url,"00:00")
                    databaseReference!!.push().setValue(m)
                }
            }
        }
    }
}