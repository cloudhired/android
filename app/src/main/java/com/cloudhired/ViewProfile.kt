package com.cloudhired

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudhired.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.view_profile.*

class ViewProfile : AppCompatActivity() {
    private lateinit var swipe: SwipeRefreshLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_profile)
        swipe = vpSwipe
        swipe.setOnRefreshListener {
            viewModel.netProfile(intent.getStringExtra("iUsername")!!)
        }

        viewModel.netProfile(intent.getStringExtra("iUsername")!!)
        viewModel.observeProfile().observe(this, {
            if (swipe.isRefreshing) swipe.isRefreshing = false
            vpTitle.text = it.fullname
            vpName.text = it.fullname
            vpJobCom.text = "${it.job_title} at ${it.company}"
            vpLocation.text = it.current_loc

            vpIntro.text = it.intro
            vpSkillsChipGroup.removeAllViews()
            it.skills.forEach {
                val chip = Chip(vpSkillsChipGroup.context)
                chip.text = it
                vpSkillsChipGroup.addView(chip)
            }

            vpCertsLL.addView(createCertLL())

        })

        vpBack.setOnClickListener {
            finish()
        }
    }

    private fun createCertLL(): LinearLayout {
        val lparams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return LinearLayout(vpCertsLL.context).apply {
            layoutParams = lparams
            setPadding(0, 8, 0, 8)
            orientation = LinearLayout.HORIZONTAL
            addView(ImageView(this.context).apply {

            })
            addView(TextView(this.context).apply {
                text = "testing text"
            })
        }
    }
}

