package com.example.trippal.AuthUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.example.trippal.MainUi.NewMain
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: PreferencesManager
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = PreferencesManager(this)
        db = FirebaseFirestore.getInstance()

        binding.loginButton.isEnabled = false
        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { setButtonEnable() }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { setButtonEnable() }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.loginButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                             db.collection("USER").document(email).get()
                                 .addOnSuccessListener { document->
                                if (document.exists()){
                                    val username = document.getString("username")
                                    val photoProfile = document.getString("photoUrl")
                                    val hobby = document.getString("hobi")
                                    val gender = document.getString("gender")
                                    val destReferences = document.getString("referensi")
                                    val ageUser = document.getString("umur")
                                    val token = firebaseAuth.uid

                                    sharedPreferences.apply {
                                        stringPreferences(PreferencesManager.USERNAME, username.toString())
                                        stringPreferences(PreferencesManager.USER_EMAIL, email)
                                        stringPreferences(PreferencesManager.TOKEN, token.toString())
                                        stringPreferences(PreferencesManager.PHOTO_PROFILE, photoProfile.toString())
                                        stringPreferences(PreferencesManager.HOBBY, hobby.toString())
                                        stringPreferences(PreferencesManager.GENDER, gender.toString())
                                        stringPreferences(PreferencesManager.REFERENSI_WISATA, destReferences.toString())
                                        stringPreferences(PreferencesManager.AGE, ageUser.toString())
                                    }

                                    Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, NewMain::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(this, "Username & Email doesn't exist", Toast.LENGTH_SHORT).show()

                                }

                            }.addOnFailureListener{ exception->
                                    Toast.makeText(this, "Fail to access User data:${exception.message}", Toast.LENGTH_SHORT).show()
                                }


                        }
                        else{
                            Toast.makeText(this, "Email and password doesn't exist please register first", Toast.LENGTH_SHORT).show()
                        }
                    } .addOnFailureListener { exception->
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }



        binding.regisButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setButtonEnable(){
        val email = binding.etEmail.text
        val pass = binding.etPassword.text
        binding.loginButton.isEnabled = pass != null &&
                                        email != null &&
                                        email.toString().isNotEmpty() &&
                                        pass.toString().isNotEmpty()
    }

}