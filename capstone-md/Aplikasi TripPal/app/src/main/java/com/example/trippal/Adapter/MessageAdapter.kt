package com.example.trippal.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trippal.MainUi.ChatActivity
import com.example.trippal.ModelData.MessageModel
import com.example.trippal.ModelData.User
import com.example.trippal.R
import com.example.trippal.databinding.MessageItemBinding

class MessageAdapter(private val listUser: ArrayList<MessageModel>):RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

//    private val listUser = ArrayList<User>()


    inner class ViewHolder(private val binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(message: MessageModel){
            binding.userName.text = message.name
//            binding.recentMessage.text = message.recentMessage

            if(message.photoUrl.isEmpty()){
                Glide.with(itemView.context)
                    .load(message.photoUrl)
                    .placeholder(R.drawable.baseline_photo_profile_24)
                    .into(binding.profilePicture)
            }
            else{
                Glide.with(itemView.context)
                    .load(message.photoUrl)
                    .into(binding.profilePicture)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        val view = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("username", user.name)
            intent.putExtra("photoUrl", user.photoUrl)
            intent.putExtra("id", user.id)
//            intent.putExtra("recentMessage", user.recentMessage)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listUser.size
}