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
import cat.tfg.pama.Session
import cat.tfg.pama.R
import cat.tfg.pama.Session2
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

open class Message(open var sender: String = "", open var content: String = "", open var type: Int? = null, open var url_photo: String? = "")
data class MessageSend(override var sender: String = "", override var content: String = "", override var type: Int? = null, override var url_photo: String? = "", var time: Map<*, *>?) : Message(sender, content, type, url_photo)
data class MessageReceive(override var sender: String = "", override var content: String = "", override var type: Int? = null, override var url_photo: String? = "", var time: Long? = null) : Message(sender, content, type, url_photo)

class ChatFragment: Fragment() {

    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null

    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var adapter:MessageAdapter? = null
    private val PHOTO_SEND = 1

    private val MESSAGE_TYPE_TEXT = 1
    private val MESSAGE_TYPE_PHOTO = 2

    private var session: Session2? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity!!.setTitle("Chat")

        session = Session2.getInstance(context)

        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.getReference(session!!.getFirebaseDatabasePath()!!)
        storage = FirebaseStorage.getInstance()

        adapter = MessageAdapter();

        (rvMessages as RecyclerView).layoutManager= LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        (rvMessages as RecyclerView).adapter = adapter

        btnSend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if(!isMessageTextEmpty()){
                    val m = MessageSend(session!!.getUseName()!!, txtMessage.text.toString(), MESSAGE_TYPE_TEXT, null, ServerValue.TIMESTAMP)
                    databaseReference!!.push().setValue(m)
                    txtMessage.setText("")
                }
            }
        })

        btnSendImage.setOnClickListener(object : View.OnClickListener {
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
                val message = dataSnapshot.getValue(MessageReceive::class.java)!!
                adapter!!.addMensaje(message)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {

            val filepath: Uri = data?.data!!
            storageReference = FirebaseStorage.getInstance().reference
            val photoRef = storageReference!!.child("images/" + UUID.randomUUID().toString())

            photoRef.putFile(filepath).addOnSuccessListener(activity!!) {
                photoRef.downloadUrl.addOnCompleteListener () { taskSnapshot ->
                    val url = taskSnapshot.result.toString()
                    val m = MessageSend(session!!.getUseName()!!,"",MESSAGE_TYPE_PHOTO, url,ServerValue.TIMESTAMP)
                    databaseReference!!.push().setValue(m)
                }
            }
        }
    }

    private fun isMessageTextEmpty() = txtMessage.text.toString() == ""

    private fun scrollBar(){
        if(adapter!!.itemCount > 0){
            (rvMessages as RecyclerView).scrollToPosition(adapter!!.itemCount-1);
        }
    }
}