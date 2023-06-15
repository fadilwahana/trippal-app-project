package com.example.trippal.MainUi

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.trippal.Adapter.ChatAdapter
import com.example.trippal.ChatUpdater.ChatUpdateListener
import com.example.trippal.ModelData.ChatModel
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.R
import com.example.trippal.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var chatDb: FirebaseDatabase
    private lateinit var chatUpdateListener: ChatUpdateListener
    private lateinit var chatList : ArrayList<ChatModel>
    private lateinit var sharedPreferences: PreferencesManager

    var receiverRoom : String? =  null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        chatList = ArrayList()
        adapter = ChatAdapter(this,chatList)
        chatDb = FirebaseDatabase.getInstance()
        sharedPreferences = PreferencesManager(this)

        val currUsername = sharedPreferences.username

        val name = intent.getStringExtra("username")
        val avatar = intent.getStringExtra("photoUrl")
        val receiverUid = intent.getStringExtra("id")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = chatDb.getReference()

        senderRoom =  currUsername + name
        receiverRoom = name + currUsername
        binding.rvChat.layoutManager = LinearLayoutManager(this)
        binding.rvChat.adapter = adapter

        binding.tvUsername.text = name
        if (avatar == ""){
            Glide.with(this)
                .load(avatar)
                .placeholder(R.drawable.baseline_photo_profile_24)
                .into(binding.userProfile)

        }
        else{
            Glide.with(this)
                .load(avatar)
                .into(binding.userProfile)
        }

        //Show message to recycler view
        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    chatList.clear()

                    for(postSnapshot in snapshot.children){
                        val chat = postSnapshot.getValue(ChatModel::class.java)
                        chatList.add(chat!!)
                    }
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        binding.sendChat.setOnClickListener {
            val chat = binding.etChat.text.toString()
            val chatObject = ChatModel(chat, senderUid)

            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(chatObject)
                .addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(chatObject)
                }
                .addOnFailureListener { exception->
                    Toast.makeText(this, "failed sent message:${exception.message}", Toast.LENGTH_SHORT).show()
                }

            binding.etChat.setText("")


        }


    }

    fun setChatListener(){
        val currUid = FirebaseAuth.getInstance().uid
        val msgReff = FirebaseDatabase.getInstance().getReference("/")
    }
}