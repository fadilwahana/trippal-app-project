package com.example.trippal.AuthUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.trippal.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { setButtonEnable() }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { setButtonEnable() }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etUsername.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { setButtonEnable() }
            override fun afterTextChanged(p0: Editable?) {}
        })



        binding.regisButton.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val photoUrl = ""
            val gender = ""
            val umur = ""
            val hobi = ""
            val preferensiWisata = ""

            //property for add document field to firestore


            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                val Users = db.collection("USER")
                Users.whereEqualTo("email", email).get()
                    .addOnSuccessListener {
                        querySnapshot->
                        if (querySnapshot.isEmpty){
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid

                                        //property for add document field to firestore
                                        val user = mapOf(
                                            "username" to username,
                                            "email" to email,
                                            "photoUrl" to photoUrl,
                                            "gender" to gender,
                                            "umur" to umur,
                                            "hobi" to hobi,
                                            "referensi" to preferensiWisata,
                                            "userId" to userId
                                        )

                                        Users.document(email).set(user)
                                        Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this,LoginActivity::class.java)
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    }

                                    else{
                                        Toast.makeText(this, "failed create user", Toast.LENGTH_SHORT).show()
                                    }
                                }


                        }
                        else{
                            Toast.makeText(this, "Email and username is already exist", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            else{
                Toast.makeText(this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show()
            }

        }


        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setButtonEnable(){
        val email = binding.etEmail.text
        val pass = binding.etPassword.text
        val username = binding.etUsername.text
        binding.regisButton.isEnabled = pass != null &&
                                        email != null &&
                                        username != null &&
                                        email.toString().isNotEmpty() &&
                                        pass.toString().isNotEmpty() &&
                                        username.toString().isNotEmpty()
    }
}