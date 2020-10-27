package com.cloudhired

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    println("search clicked")
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    println("more clicked")
                    true
                }
                else -> false
            }
        }
    }
}