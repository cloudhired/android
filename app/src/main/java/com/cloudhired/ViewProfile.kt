package com.cloudhired

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudhired.R
import kotlinx.android.synthetic.main.view_profile.*

class ViewProfile : AppCompatActivity() {
    private lateinit var swipe: SwipeRefreshLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_profile)
        swipe = vpSwipe

        viewModel.netProfile(intent.getStringExtra("iUsername")!!)
        viewModel.observeProfile().observe(this, {
            if (swipe.isRefreshing) swipe.isRefreshing = false
            vpTitle.text = it.fullname
            vpName.text = it.fullname
            vpJobCom.text = "${it.job_title} at ${it.company}"
            vpLocation.text = it.current_loc
        })

        swipe.setOnRefreshListener {
            viewModel.netProfile(intent.getStringExtra("iUsername")!!)
        }

        vpBack.setOnClickListener {
            finish()
        }
    }
}

