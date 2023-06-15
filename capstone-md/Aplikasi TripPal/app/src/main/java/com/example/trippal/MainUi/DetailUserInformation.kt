package com.example.trippal.MainUi

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.R
import com.example.trippal.databinding.ActivityDetailUserInformationBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailUserInformation : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserInformationBinding
    private lateinit var sharedPreferences: PreferencesManager
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferencesManager(this)
        db = FirebaseFirestore.getInstance()
        val email = sharedPreferences.email


        //Enabling or dissable button
        binding.saveDetailInfo.isEnabled = false
        binding.dropdownHobby.addTextChangedListener ( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) { setButtonEnable() }
        })
        binding.dropdownWisata.addTextChangedListener ( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) { setButtonEnable() }
        })
        binding.dropdownGender.addTextChangedListener ( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) { setButtonEnable() }
        })



        //Destination Dropdown
        val itemsDestination = listOf("Wisata Kuliner", "Wisata Alam", "Wisata Seni dan Budaya", "Wisata Sejarah dan Religi")
        val DestinationAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemsDestination)
        binding.dropdownWisata.setAdapter(DestinationAdapter)

        binding.dropdownWisata.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            val itemSelected = adapterView.getItemAtPosition(i) as String
            sharedPreferences.stringPreferences(PreferencesManager.REFERENSI_WISATA, itemSelected)
            db.collection("USER")
                .document(email.toString())
                .update("referensi", itemSelected)
        }


        //Hobby Dropdown
        val itemsHobby = listOf("Membaca", "Menulis", "Bersepeda", "Fotografi", "Memasak", "Berkebun", "Mendaki", "Menyelam")
        val hobbyAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemsHobby)
        binding.dropdownHobby.setAdapter(hobbyAdapter)

        binding.dropdownHobby.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, _, i, _ ->
            val hobbyItemSelected = adapterView.getItemAtPosition(i) as String
            sharedPreferences.stringPreferences(PreferencesManager.HOBBY, hobbyItemSelected)
            db.collection("USER")
                .document(email.toString())
                .update("hobi", hobbyItemSelected)

        }

        //Gender Dropdown
        val itemsGender = listOf("Laki-Laki", "Perempuan")
        val genderAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemsGender)
        binding.dropdownGender.setAdapter(genderAdapter)

        binding.dropdownGender.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            val genderItemSelected = adapterView.getItemAtPosition(i) as String
            sharedPreferences.stringPreferences(PreferencesManager.GENDER, genderItemSelected)
            db.collection("USER")
                .document(email.toString())
                .update("gender", genderItemSelected)


        }

        val ageInput = binding.etAge.text
        sharedPreferences.stringPreferences(PreferencesManager.AGE, ageInput.toString())



        binding.saveDetailInfo.setOnClickListener {
            val age = binding.etAge.text.toString()

            if (age.isEmpty()){
                Toast.makeText(this, "Please fill age field", Toast.LENGTH_SHORT).show()
            }

            else{
                db.collection("USER")
                    .document(email.toString())
                    .update("umur", age)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Successful add aditional data", Toast.LENGTH_SHORT).show()
                        val intent=Intent(this, NewMain::class.java)
                        startActivity(intent)
                        finish()
                    }

                    .addOnFailureListener {error->
                        Toast.makeText(this, "Failed add aditional data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        }


    }

    private fun setButtonEnable(){
        val hobby = binding.dropdownHobby.text
        val gender = binding.dropdownGender.text
        val wisata = binding.dropdownWisata.text
        binding.saveDetailInfo.isEnabled = hobby != null &&
                                           gender != null &&
                                           wisata != null &&
                                           hobby.toString().isNotEmpty() &&
                                           gender.toString().isNotEmpty() &&
                                           wisata.toString().isNotEmpty()
    }
}