package com.example.trippal.MainUi.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trippal.Adapter.MessageAdapter
import com.example.trippal.Adapter.RecomendationAdapter
import com.example.trippal.AuthUi.LoginActivity
import com.example.trippal.ModelData.DataItem
import com.example.trippal.ModelData.RecomendationResponse
import com.example.trippal.ModelData.User
import com.example.trippal.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: RecomendationAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db = FirebaseFirestore.getInstance()
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.loadRecommendation()


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currUser = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("USER")
            .whereNotEqualTo("userId",currUser)
            .get()
            .addOnSuccessListener { result->
                val userList = ArrayList<DataItem>()

                for (document in result){
                    val name = document.getString("username") ?: ""

                    val user = DataItem(name)
                    userList.add(user)

                }
                adapter = RecomendationAdapter(userList)
                adapter.notifyDataSetChanged()
                binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
                binding.rvUser.adapter = adapter
                viewModel.getRecommendation().observe(requireActivity(),{
                    if (it !=null){
                        adapter.setAllist(it as ArrayList<DataItem>)
                    }
                })
            }
            .addOnFailureListener { exception->
                Toast.makeText(requireContext(), "failed show data:${exception.message}", Toast.LENGTH_SHORT).show()

            }
//        displayUser()


        return root
    }

//    private fun displayUser(){
//        val currUser = FirebaseAuth.getInstance().currentUser?.uid
//        db.collection("USER")
//            .whereNotEqualTo("userId",currUser)
//            .get()
//            .addOnSuccessListener { result->
//                val userList = ArrayList<User>()
//
//                for (document in result){
//                    val name = document.getString("username") ?: ""
//                    val photoProfile = document.getString("photoUrl") ?:""
//                    val userId = document.getString("userId") ?: ""
//
//                    val user = User(name, userId,  photoProfile)
//                    userList.add(user)
//
//                }
//                adapter = RecomendationAdapter(userList)
//                binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
//                binding.rvUser.adapter = adapter
//            }
//            .addOnFailureListener { exception->
//                Toast.makeText(requireContext(), "failed show data:${exception.message}", Toast.LENGTH_SHORT).show()
//
//            }
//    }

//    private fun displayUser(){
//        val currUser = FirebaseAuth.getInstance().currentUser?.uid
//        db.collection("USER")
//            .whereNotEqualTo("userId",currUser)
//            .get()
//            .addOnSuccessListener { result->
//                val userList = ArrayList<DataItem>()
//
//                for (document in result){
//                    val name = document.getString("username") ?: ""
//
//                    val user = DataItem(name)
//                    userList.add(user)
//
//                }
//                adapter = RecomendationAdapter(userList)
//                binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
//                binding.rvUser.adapter = adapter
//            }
//            .addOnFailureListener { exception->
//                Toast.makeText(requireContext(), "failed show data:${exception.message}", Toast.LENGTH_SHORT).show()
//
//            }
//    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}