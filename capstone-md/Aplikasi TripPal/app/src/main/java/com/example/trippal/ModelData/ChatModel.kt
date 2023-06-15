package com.example.trippal.ModelData

class ChatModel {
    var Chat: String? = null
    var senderId: String? = null

    constructor()

    constructor(chat: String?, senderId: String?){
        this.Chat = chat
        this.senderId = senderId
    }
}