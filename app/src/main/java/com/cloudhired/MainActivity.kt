package com.cloudhired

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragment_professionals, R.id.fragment_jobs, R.id.fragment_notifications),
                    drawerLayout )

        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)
        setupBottomNavMenu(navController)



        val authInitIntent = Intent(this, AuthInitActivity::class.java)
        startActivity(authInitIntent)

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
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private fun setupActionBar(navController: NavController,
                                appbarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appbarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }

}