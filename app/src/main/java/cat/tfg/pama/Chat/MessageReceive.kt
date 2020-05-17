package cat.tfg.pama.Chat

class MessageReceive : Message {

    var time: Long? = null

    constructor() {}

    constructor(time: Long) {
        this.time = time
    }

    constructor(sender: String = "", content: String = "", type: Int? = null, url_photo: String? = "", time: Long) : super(sender, content, type, url_photo) {
        this.time = time
    }
}