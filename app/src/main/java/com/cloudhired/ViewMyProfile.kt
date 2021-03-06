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

class ViewMyProfile : AppCompatActivity() {
    companion object {
        const val iEdit = "iEdit"
        const val iEmail = "iEmail"
    }
    private lateinit var swipe: SwipeRefreshLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_my_profile)
        swipe = vpSwipe
        swipe.setOnRefreshListener {
            viewModel.netMyProfile(intent.getStringExtra("iEmail")!!)
        }

        initEditProfile(vmpBasicIV)
        initEditProfile(vmpIntroIV)
        initEditProfile(vmpSkillsIV)
        initEditProfile(vmpCertsIV)
        initEditProfile(vmpCoursesIV)

        viewModel.netMyProfile(intent.getStringExtra("iEmail")!!)
        viewModel.observeMyProfile().observe(this, {
            if (swipe.isRefreshing) swipe.isRefreshing = false
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

    override fun onResume() {
        super.onResume()
        viewModel.netMyProfile(intent.getStringExtra("iEmail")!!)
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

    private fun initEditProfile(view: View) {
        view.setOnClickListener {
            val pvIntent = Intent(it.context, EditMyProfile::class.java)
            val pvExtras = Bundle()
            pvExtras.putInt(iEdit, view.id)
            pvExtras.putString(iEmail, intent.getStringExtra("iEmail"))
            pvIntent.putExtras(pvExtras)
            it.context.startActivity(pvIntent)
        }
    }
    
}

