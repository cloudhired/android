package com.cloudhired

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudhired.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.edit_my_profile_basic.*
import kotlinx.android.synthetic.main.view_my_profile.*
import kotlinx.android.synthetic.main.view_profile.*
import kotlinx.android.synthetic.main.view_profile.vpBack
import kotlinx.android.synthetic.main.view_profile.vpCL
import kotlinx.android.synthetic.main.view_profile.vpCertsLL
import kotlinx.android.synthetic.main.view_profile.vpCoursesLL
import kotlinx.android.synthetic.main.view_profile.vpIntro
import kotlinx.android.synthetic.main.view_profile.vpJobCom
import kotlinx.android.synthetic.main.view_profile.vpLocation
import kotlinx.android.synthetic.main.view_profile.vpName
import kotlinx.android.synthetic.main.view_profile.vpSkillsChipGroup
import kotlinx.android.synthetic.main.view_profile.vpSwipe
import kotlinx.android.synthetic.main.view_profile.vpTitle

class EditMyProfile : AppCompatActivity() {
    private lateinit var swipe: SwipeRefreshLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_my_profile_basic)
        swipe = vpSwipe

        viewModel.netMyProfile("gaomengen@gmail.com")
        viewModel.observeMyProfile().observe(this, {
            when (intent.getIntExtra("iEdit", 0)) {
                R.id.vmpBasicIV -> {
                    epTitle.text = "Edit Basics"
                    epNameTIL.editText?.setText(it.fullname)
                    epTitleTIL.editText?.setText(it.job_title)
                    epCompanyTIL.editText?.setText(it.company)
                    epLocationTIL.editText?.setText(it.current_loc)
                    epSave.setOnClickListener {
                        println("saved")
                    }
                }
                else -> {}
            }
        })


        epBack.setOnClickListener {
            finish()
        }
    }
}

