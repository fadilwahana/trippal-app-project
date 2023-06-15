package com.example.trippal.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trippal.MainUi.ChatActivity
import com.example.trippal.ModelData.DataItem
import com.example.trippal.ModelData.RecomendationResponse
import com.example.trippal.databinding.RecomendationItemBinding
import com.example.trippal.ModelData.User
import com.example.trippal.R

class RecomendationAdapter(private val listUser: ArrayList<DataItem>): RecyclerView.Adapter<RecomendationAdapter.GridViewHolder>() {



    inner class GridViewHolder(private val binding: RecomendationItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(user: DataItem){
            binding.userName.text = user.name

//            if(user.photoUrl.isEmpty()){
//                Glide.with(itemView)
//                    .load(user.photoUrl)
//                    .placeholder(R.drawable.baseline_photo_profile_24)
//                    .into(binding.photoProfile)
//            }
//            else{
//                Glide.with(itemView)
//                    .load(user.photoUrl)
//                    .into(binding.photoProfile)
//            }

        }

    }

    fun setAllist(user: ArrayList<DataItem>){
        listUser.clear()
        listUser.addAll(user)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendationAdapter.GridViewHolder {
        val view = RecomendationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
//
//            intent.putExtra("username", user.name)
//            intent.putExtra("photoUrl", user.photoUrl)
//            intent.putExtra("id", user.id)
//
//            holder.itemView.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int = listUser.size
}