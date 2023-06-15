package com.example.trippal.ChatUpdater

import com.example.trippal.ModelData.ChatModel
import com.example.trippal.ModelData.User

interface ChatUpdateListener {
    fun onChatUpdated(chatList: ArrayList<ChatModel>, userList: ArrayList<User>)

}