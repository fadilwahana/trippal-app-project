package com.example.trippal.MainUi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.trippal.AuthUi.LoginActivity
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.R
import com.example.trippal.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferencesManager(this)
        val username = sharedPreferences.username
        val email = sharedPreferences.email
        val imageUrl = sharedPreferences.photoProfile
        val normalMode = sharedPreferences.normalMode
        val profilePic = binding.photoProfile

        binding.tvUsername.text = username
        binding.tvEmail.text = email


        //appearing profile picture
        if (imageUrl!!.isEmpty()){
            binding.photoProfile.setImageResource(R.drawable.baseline_photo_profile_24)
        }
        else if(imageUrl.isNotEmpty()){
            Glide.with(this)
                .load(imageUrl)
                .into(profilePic)
        }


        //edit profile
        binding.editProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            sharedPreferences.clearPreference()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setSupportActionBar(binding.toolbar)



    }
}