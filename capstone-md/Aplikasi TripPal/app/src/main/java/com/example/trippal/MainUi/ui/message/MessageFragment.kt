package com.example.trippal.MainUi.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trippal.Adapter.MessageAdapter
import com.example.trippal.ModelData.MessageModel
import com.example.trippal.ModelData.User
import com.example.trippal.databinding.FragmentMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: MessageAdapter
    private lateinit var messageDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        messageDatabase = FirebaseDatabase.getInstance()
        db = FirebaseFirestore.getInstance()
        val message = FirebaseDatabase.getInstance()
        val messageDatabase = message.getReference("chats/")


        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        displayMessage()



        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayMessage(){
        val currUser = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("USER")
            .whereNotEqualTo("userId", currUser)
            .get()
            .addOnSuccessListener {result->
                val messageList = ArrayList<MessageModel>()

                for(document in result){
                    val userId = document.getString("userId") ?: ""
                    val name = document.getString("username") ?: ""
                    val photoProfile = document.getString("photoUrl") ?:""

                    val message = MessageModel(userId, name , photoProfile)
                    messageList.add(message)
                }

                adapter = MessageAdapter(messageList)
                binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
                binding.rvUser.adapter = adapter
            }
            .addOnFailureListener { exception->
                Toast.makeText(requireContext(), "failed show message:${exception.message}", Toast.LENGTH_SHORT).show()

            }

    }
}



