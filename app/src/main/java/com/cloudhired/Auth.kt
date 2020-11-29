package com.cloudhired

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class Auth(activity: AppCompatActivity) {
    companion object {
        const val rcSignIn = 27
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        var user = FirebaseAuth.getInstance().currentUser
    }
    init {
        // add auth state listener to monitor auth status, if signed out, re signIn()
        FirebaseAuth.getInstance().addAuthStateListener(FirebaseAuth.AuthStateListener {
            user = FirebaseAuth.getInstance().currentUser
            if (user == null) signIn(activity)
        })

        // when start app, if not signed in, signIn()
        if (user == null) signIn(activity)
    }

    private fun signIn(activity: AppCompatActivity) {
        // Create and launch sign-in intent
        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                // Was creating problems
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build(),
            rcSignIn
        )
    }

    fun getDisplayName(): String {
        return user?.displayName ?: ""
    }
    fun getEmail(): String {
        return user?.email ?: ""
    }
    fun getUid(): String {
        Log.d(javaClass.simpleName, "getUid user $user uid ${user?.uid}")
        return user?.uid ?: ""
    }

    fun setDisplayNameByEmail() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        if (user.displayName == null || user.displayName!!.isEmpty()) {
            val displayName = user.email?.substringBefore("@")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(javaClass.simpleName, "User profile updated.")
                    }
                }
        }
    }
}