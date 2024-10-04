package com.cenkeraydin.wordwave.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore
) {

    fun register(email: String, password: String, onComplete: (Boolean, String) -> (Unit)) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "")
                } else {
                    onComplete(false, task.exception?.message ?: "An error occurred")
                }
            }
    }

    fun login(email: String, password: String, onComplete: (Boolean, String) -> (Unit)) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "")
                } else {
                    onComplete(false, task.exception?.message ?: "An error occurred")
                }
            }
    }
    fun signOut() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun saveUserData( email: String, password: String,userName: String, fullName: String, onComplete: (Boolean, String) -> (Unit)) {
        val user = hashMapOf(
            "email" to email,
            "nickname" to userName,
            "fullName" to fullName,
            "password" to password
        )

        firestore.collection("users")
            .document(auth.currentUser?.uid ?: "")
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, "")
                } else {
                    onComplete(false, task.exception?.message ?: "An error occurred")
                }
            }
    }

}