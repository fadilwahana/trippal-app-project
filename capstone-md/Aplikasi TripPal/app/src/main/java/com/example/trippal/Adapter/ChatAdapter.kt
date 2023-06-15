package com.example.trippal.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trippal.ModelData.ChatModel
import com.example.trippal.R
import com.example.trippal.databinding.RecomendationItemBinding
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(val context: Context, val chatList: ArrayList<ChatModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == ITEM_RECEIVE){
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive_item, parent, false)
            return RecieveMessageViewHolder(view)
        }

        else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_item, parent, false)
            return SentMessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int  = chatList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currMessage = chatList[position]

        if (holder.javaClass == SentMessageViewHolder::class.java){
            val viewHolder = holder as SentMessageViewHolder

            holder.sentMessage.text = currMessage.Chat
        }

        else{

            val viewHolder = holder as RecieveMessageViewHolder
            holder.recieveMessage.text = currMessage.Chat
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage = chatList[position]
        val currUserAuth = FirebaseAuth.getInstance().currentUser?.uid

        if(currUserAuth == currMessage.senderId){
            return ITEM_SENT
        }

        else{
            return ITEM_RECEIVE
        }
    }

    class SentMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.sent_txt_message)

    }

    class RecieveMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.recieve_txt_message)

    }


}