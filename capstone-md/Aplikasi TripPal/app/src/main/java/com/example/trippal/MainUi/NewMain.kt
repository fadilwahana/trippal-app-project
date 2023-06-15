package com.example.trippal.MainUi

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trippal.AuthUi.LoginActivity
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.R
import com.example.trippal.databinding.ActivityNewMainBinding
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.appcheck.safetynet.internal.SafetyNetAppCheckProvider

class NewMain : AppCompatActivity() {

    private lateinit var binding: ActivityNewMainBinding
    private lateinit var sharedPreferences: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferencesManager(this)

        val token = sharedPreferences.token
        val hobby = sharedPreferences.userHobby
        val gender = sharedPreferences.userGender
        val referenceDestination = sharedPreferences.userReference
        val age = sharedPreferences.age
        val navView: BottomNavigationView = binding.navView

        //Navigation after login
        if (token!!.isEmpty()){
            val intent = Intent(this@NewMain, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if (hobby!!.isEmpty() && gender!!.isEmpty() && referenceDestination!!.isEmpty() && age!!.isEmpty()){
            val intent = Intent(this@NewMain, DetailUserInformation::class.java)
            startActivity(intent)
            finish()
        }



        val navController = findNavController(R.id.nav_host_fragment_activity_new_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_message,
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.user_profile->{
                Intent(this@NewMain, ProfileActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }

        }
        return onOptionsItemSelected(item)
    }





}