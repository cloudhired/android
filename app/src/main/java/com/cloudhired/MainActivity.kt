package com.cloudhired

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
//    private val viewMdoel: M
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val authInitIntent = Intent(this, AuthInitActivity::class.java)
        startActivity(authInitIntent)


        topAppBar.setNavigationOnClickListener {
            println("you clicked topapp bar")
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    // Handle favorite icon press
                    println("favorite clicked")
                    true
                }
                R.id.search -> {
                    // Handle search icon press
                    println("search clickedfg")
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    FirebaseAuth.getInstance().signOut()
                    println("more clicked")
                    true
                }
                else -> false
            }
        }
    }


}