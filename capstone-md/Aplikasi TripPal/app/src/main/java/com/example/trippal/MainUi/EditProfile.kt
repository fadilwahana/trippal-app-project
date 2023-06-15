package com.example.trippal.MainUi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.content.Intent.ACTION_GET_CONTENT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.trippal.Preferences.PreferencesManager
import com.example.trippal.R
import com.example.trippal.Utils.createCustomTempFile
import com.example.trippal.Utils.uriToFile
import com.example.trippal.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var fileImage: File? = null
    private lateinit var db: FirebaseFirestore
    private lateinit var storageReference: FirebaseStorage
    private lateinit var sharedPreferences: PreferencesManager


    //Kode permission if permission not granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS_CODE){
            if (!permissionsGranted()){
                Toast.makeText(this, "Permission not Allowed", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if(!permissionsGranted()){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS_CODE)
        }

        db = FirebaseFirestore.getInstance()
        sharedPreferences = PreferencesManager(this)
        val imageUrl = sharedPreferences.photoProfile


        binding.saveProfile.isEnabled = false
        binding.profilePicture.addOnLayoutChangeListener{_, _, _, _, _, _, _, _, _ ->
            setButtonEnable()
        }

        binding.etChangeName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { setButtonEnable() }
            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.deletePropic.setOnClickListener { deletePP() }
        binding.fromCamera.setOnClickListener { startTakePhoto() }
        binding.fromGallery.setOnClickListener { takeFromGallery() }

        binding.saveProfile.setOnClickListener {
            val newUname = binding.etChangeName.text.toString()

            if(newUname.isNotEmpty() && fileImage == null){
                editUsername()
            }

           else if(fileImage != null && newUname.isEmpty()){
                savePhoto()
            }

            else if (newUname.isEmpty() && fileImage == null){
                Toast.makeText(this, "there is no data to update", Toast.LENGTH_SHORT).show()
            }

            else{
                savePhotoAndUsername()


            }

        }


        if (imageUrl!!.isEmpty()){
            binding.profilePicture.setImageResource(R.drawable.baseline_photo_profile_24)
        }

        else if(imageUrl.isNotEmpty()){
            Glide.with(this)
                .load(imageUrl)
                .into(binding.profilePicture)
        }

    }

    //Fungsi kondisi jika permission diterima
    private fun permissionsGranted() = PERMISSIONS.all{
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    //Take Picture to Camera
    private lateinit var currentPhoto: String
    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhoto)

            myFile.let { file ->
                fileImage = File(currentPhoto)
                binding.profilePicture.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }


    private fun startTakePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@EditProfile,
                "com.example.trippal", it
            )
            currentPhoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launchIntentCamera.launch(intent)
        }
    }

    //Take picture from gallery
    private val pickFromGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if (result.resultCode == RESULT_OK){
            val selectedImage = result.data?.data as Uri
            selectedImage.let { Uri->
                val myfile = uriToFile(Uri, this)
                fileImage = myfile
                binding.profilePicture.setImageURI(Uri)
            }
        }

    }

    private fun takeFromGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val pick = Intent.createChooser(intent, "Select Picture")
        pickFromGallery.launch(pick)

    }


    @SuppressLint("SuspiciousIndentation")
    private fun savePhoto(){

        sharedPreferences = PreferencesManager(this)

        val email = sharedPreferences.email
        storageReference = FirebaseStorage.getInstance()


        if(fileImage != null){
            val storageRef = storageReference.reference
            val photoRef = storageRef.child("profile_pic/${FirebaseAuth.getInstance().currentUser?.uid}")
            val fileUri = Uri.fromFile(fileImage)

            photoRef.putFile(fileUri)
                .addOnSuccessListener {

                    photoRef.downloadUrl
                        .addOnSuccessListener {uri->
                        val imageUrl = uri.toString()
                            if (email != null) {
                                db.collection("USER")
                                    .document(email)
                                    .update("photoUrl", imageUrl)
                                    .addOnSuccessListener {
                                        sharedPreferences.stringPreferences(PreferencesManager.PHOTO_PROFILE, imageUrl)
                                        Toast.makeText(this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, ProfileActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener{ exception->

                                        Toast.makeText(this, "Failed to Update Profile Picture", Toast.LENGTH_SHORT).show()
                                    }
                            }

                    }
                }
                .addOnFailureListener {exception->
                    Toast.makeText(this, "Gagal mengupload foto", Toast.LENGTH_SHORT).show()
                }
        }

//        else{
//            Toast.makeText(this, "choose photo profile first!", Toast.LENGTH_SHORT).show()
//        }


    }

    private fun editUsername(){
        sharedPreferences = PreferencesManager(this)
        val newUname = binding.etChangeName.text.toString()
        val email = sharedPreferences.email.toString()
        val User = db.collection("USER").document(email)
        User.update("username",newUname)
            .addOnSuccessListener {
                    sharedPreferences.stringPreferences(PreferencesManager.USERNAME, newUname)
                    Toast.makeText(this, "Username Updated Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
            }

            .addOnFailureListener {e->
                Toast.makeText(this, "Failed update username:${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun deletePP(){
        val email = sharedPreferences.email.toString()
        val imageUrl = sharedPreferences.photoProfile.toString()
        val urlPhoto = ""


        storageReference = FirebaseStorage.getInstance()

        if(imageUrl.isNotEmpty()){
            val storageRef = storageReference.reference
            val photoRef = storageRef.child("profile_pic/${FirebaseAuth.getInstance().currentUser?.uid}")

            photoRef.delete()
                .addOnSuccessListener {
                    if (email.isNotEmpty()){
                        val User = db.collection("USER").document(email)
                        User.update("photoUrl", urlPhoto)
                            .addOnSuccessListener {
                                sharedPreferences.stringPreferences(PreferencesManager.PHOTO_PROFILE, urlPhoto)
                                Toast.makeText(this, "Profile picture successfully deleted", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, ProfileActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                    }
                    else{
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }

                }
                .addOnFailureListener {exception->
                    Toast.makeText(this, "Failed to delete profile picture: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        else{
            Toast.makeText(this, "There is no profile picture to delete", Toast.LENGTH_SHORT).show()
        }

    }


    private fun savePhotoAndUsername(){
        sharedPreferences = PreferencesManager(this)
        storageReference  = FirebaseStorage.getInstance()
        val newUname = binding.etChangeName.text.toString()
        val email = sharedPreferences.email.toString()

        if (fileImage != null){
            val storageRef = storageReference.reference
            val photoRef = storageRef.child("profile_pic/${FirebaseAuth.getInstance().currentUser?.uid}")
            val fileUri = Uri.fromFile(fileImage)

            photoRef.putFile(fileUri)
                .addOnSuccessListener {

                    photoRef.downloadUrl
                        .addOnSuccessListener { imageUri->
                            val imageUrl = imageUri.toString()
                            val updatedData =  mapOf(
                               "photoUrl" to imageUrl,
                                "username" to newUname
                            )
                            if (email.isNotEmpty()){
                                db.collection("USER")
                                    .document(email)
                                    .update(updatedData)
                                    .addOnSuccessListener {
                                        sharedPreferences.stringPreferences(PreferencesManager.PHOTO_PROFILE, imageUrl)
                                        sharedPreferences.stringPreferences(PreferencesManager.USERNAME, newUname)
                                        Toast.makeText(this, "Photo and Username updated successfully", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, ProfileActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener {exception->
                                        Toast.makeText(this, "Update photo and username failed: ${exception.message}", Toast.LENGTH_SHORT).show()

                                    }

                            }

                            else{
                                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                            }

                        }

                        .addOnFailureListener {exception->
                            Toast.makeText(this, "Download Url Failed: ${exception.message}", Toast.LENGTH_SHORT).show()

                        }
                }

                .addOnFailureListener {exception->
                    Toast.makeText(this, "Failed to putFile: ${exception.message}", Toast.LENGTH_SHORT).show()

                }
        }
    }


    private fun setButtonEnable(){
        val edtUname = binding.etChangeName.text.toString()
        val proPic = binding.profilePicture.drawable
        binding.saveProfile.isEnabled = edtUname.isNotEmpty() ||
                                        proPic != null


    }

    companion object{
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_PERMISSIONS_CODE = 10
    }
}