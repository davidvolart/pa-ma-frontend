package cat.tfg.pama.Chat

data class MessageSend(override var sender: String = "", override var content: String = "", override var type: Int? = null, override var url_photo: String? = "", var time: Map<*, *>?) : Message(sender, content, type, url_photo)

/*
class MessageSend : Message {

    private var time: Map<*, *>? = null

    constructor(sender: String = "", content: String = "", type: Int? = null, url_photo: String? = "", time: Map<*, *>?) : super(sender, content, type, url_photo) {
        this.time = time
    }
}
 */