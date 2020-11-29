package com.cloudhired

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
import kotlinx.android.synthetic.main.view_profile.*

class ViewMyProfile : AppCompatActivity() {
    private lateinit var swipe: SwipeRefreshLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_my_profile)
        swipe = vpSwipe
        swipe.setOnRefreshListener {
            viewModel.netMyProfile(intent.getStringExtra("iEmail")!!)
        }

        viewModel.netMyProfile(intent.getStringExtra("iEmail")!!)
        viewModel.observeMyProfile().observe(this, {
            if (swipe.isRefreshing) swipe.isRefreshing = false
            println(it)
            vpTitle.text = it?.fullname ?: intent.getStringExtra("iDisplayName")
            vpName.text = it?.fullname ?: intent.getStringExtra("iDisplayName")
            vpJobCom.text = "${it?.job_title ?: "not set"} at ${it?.company ?: "not set"}"
            vpLocation.text = it?.current_loc ?: "no where"

            vpIntro.text = it?.intro ?: "Nothing special"
            vpSkillsChipGroup.removeAllViews()
            if (it?.skills != null) {
                it.skills.forEach { skill ->
                    val chip = Chip(vpSkillsChipGroup.context)
                    chip.text = skill
                    vpSkillsChipGroup.addView(chip)
                }
            }

            // delete all views except the title TV
            vpCertsLL.removeViews(1, vpCertsLL.childCount - 1)
            if (it?.certs != null) {
                it.certs.forEach { cert ->
                    vpCertsLL.addView(createCertLL(cert.cert_name))
                }
            }

            vpCoursesLL.removeViews(1, vpCoursesLL.childCount - 1)
            if (it.courses != null) {
                it.courses.forEach { course ->
                    vpCoursesLL.addView(createCourseLL(course.name))
                }
            }

            // set whole constraint layout visible to show result at once
            vpCL.visibility = View.VISIBLE
        })

        vpBack.setOnClickListener {
            finish()
        }
    }

    private fun createCertLL(certName: String): LinearLayout {
        val lparams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val TVlparams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        TVlparams.gravity = Gravity.CENTER
        TVlparams.marginStart = 16

        return LinearLayout(vpCertsLL.context).apply {
            layoutParams = lparams
            setPadding(0, 8, 0, 8)
            orientation = LinearLayout.HORIZONTAL
            addView(ImageView(this.context).apply {
                setImageResource(R.drawable.ic_baseline_image_36)
            })
            addView(TextView(this.context).apply {
                text = certName
            }, TVlparams)
        }
    }

    private fun createCourseLL(courseName: String): LinearLayout {
        val lparams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val TVlparams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        TVlparams.gravity = Gravity.CENTER
        TVlparams.marginStart = 16

        return LinearLayout(vpCertsLL.context).apply {
            layoutParams = lparams
            setPadding(0, 8, 0, 8)
            orientation = LinearLayout.HORIZONTAL
            addView(TextView(this.context).apply {
                text = courseName
            }, TVlparams)
        }
    }
    
}

