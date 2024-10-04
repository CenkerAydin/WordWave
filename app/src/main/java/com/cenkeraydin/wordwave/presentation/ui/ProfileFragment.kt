package com.cenkeraydin.wordwave.presentation.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cenkeraydin.wordwave.R
import com.cenkeraydin.wordwave.databinding.FragmentProfileBinding
import com.cenkeraydin.wordwave.presentation.ui.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private val storage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileImage.setOnClickListener {
            openGallery()
        }
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    uploadImageToFirebase(it) // Fotoğraf Firebase'e yüklenecek
                    binding.profileImage.setImageURI(selectedImageUri) // Geçici olarak göster
                }
            }
        }

        auth.currentUser?.let {
            db.collection("users")
                .document(it.uid)
                .get()
                .addOnSuccessListener { document ->
                    val email = document["email"] as? String ?: ""
                    val nickname = document["nickname"] as? String ?: ""
                    val password = document["password"] as? String ?: ""
                    val fullname = document["fullName"] as? String ?: ""
                    val profileImageUrl = document["profileImageUrl"] as? String ?: ""

                    binding.tvEmail.text = email
                    binding.tvNickName.text = nickname
                    binding.tvFullName.text = fullname
                    binding.tvPassword.text = password
                    binding.profileImage.setImageURI(Uri.parse(profileImageUrl))

                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(profileImageUrl)
                            .into(binding.profileImage)
                    }
                }
        }

        binding.tvLogOut.setOnClickListener {
            authViewModel.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }


    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    // Firebase Storage'a fotoğraf yükleme ve Firestore'a URL kaydetme
    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profileImages/$userId.jpg")

        // Firebase Storage'a yükleme
        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            // Yükleme başarılı olduğunda, indirilebilir URL'yi al
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()

                // Firestore'da profil fotoğrafı URL'sini kaydet
                db.collection("users").document(userId)
                    .update("profileImageUrl", downloadUrl)
                    .addOnSuccessListener {
                        Log.d("ProfileFragment", "Fotoğraf URL'si başarıyla kaydedildi")
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileFragment", "Fotoğraf URL'si kaydedilirken hata oluştu", e)
                    }
            }
        }.addOnFailureListener { exception ->
            Log.e("ProfileFragment", "Fotoğraf yüklenirken hata oluştu", exception)
        }
    }
}
