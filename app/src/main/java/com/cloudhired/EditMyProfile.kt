package com.cloudhired

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.google.gson.*
import kotlinx.android.synthetic.main.edit_basic.*
import kotlinx.android.synthetic.main.edit_intro.*
import kotlinx.android.synthetic.main.edit_my_profile.*
import kotlinx.android.synthetic.main.edit_skills.*
import kotlinx.android.synthetic.main.view_profile.*
import kotlinx.android.synthetic.main.view_profile.vpSwipe
import org.json.JSONArray

class EditMyProfile : AppCompatActivity() {
    private lateinit var swipe: SwipeRefreshLayout
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_my_profile)
        swipe = vpSwipe
        val id = intent.getStringExtra("iEmail")!!

        viewModel.netMyProfile(id)
        viewModel.observeMyProfile().observe(this, {
            when (intent.getIntExtra("iEdit", 0)) {
                R.id.vmpBasicIV -> {
                    View.inflate(this, R.layout.edit_basic, swipe)
                    epTitle.text = "Edit Basics"
                    epNameTIL.editText?.setText(it.fullname)
                    epTitleTIL.editText?.setText(it.job_title)
                    epCompanyTIL.editText?.setText(it.company)
                    epLocationTIL.editText?.setText(it.current_loc)
                    epSave.setOnClickListener {
                        val info = JsonObject()
                        val setInfo = JsonObject()
                        info.addProperty("fullname", epNameTIL.editText?.text.toString())
                        info.addProperty("job_title", epTitleTIL.editText?.text.toString())
                        info.addProperty("company", epCompanyTIL.editText?.text.toString())
                        info.addProperty("current_loc", epLocationTIL.editText?.text.toString())
                        setInfo.add("setInfo", info)
                        viewModel.updateProfileMV(id, setInfo)
                        viewModel.observeUpdateError().observe(this, {
                            viewModel.netMyProfile(id)
                        })
                    }
                }
                R.id.vmpIntroIV -> {
                    View.inflate(this, R.layout.edit_intro, swipe)
                    epTitle.text = "Edit Introduction"
                    epIntroTIL.editText?.setText(it.intro)
                    epSave.setOnClickListener {
                        val info = JsonObject()
                        val setInfo = JsonObject()
                        info.addProperty("intro", epIntroTIL.editText?.text.toString())
                        setInfo.add("setInfo", info)
                        viewModel.updateProfileMV(id, setInfo)
                        viewModel.observeUpdateError().observe(this, {
                            viewModel.netMyProfile(id)
                        })
                    }
                }
                R.id.vmpSkillsIV -> {
                    var skills = it.skills as ArrayList<String>
                    View.inflate(this, R.layout.edit_skills, swipe)
                    epTitle.text = "Edit Skills"
                    if (skills != null) {
                        skills.forEach { skill ->
                            val chip = Chip(epSkillsChipGroup.context)
                            chip.text = skill
                            epSkillsChipGroup.addView(chip)
                        }
                    }

                    epSkillBtn.setOnClickListener {
                        if (!epSkillTIL.editText?.text.isNullOrBlank()) {
                            skills.add(epSkillTIL.editText?.text.toString())
                            epSkillTIL.editText?.setText("")
                            epSkillsChipGroup.removeAllViews()
                            skills.forEach { skill ->
                                val chip = Chip(epSkillsChipGroup.context)
                                chip.text = skill
                                epSkillsChipGroup.addView(chip)
                            }
                        }
                    }

                    epSave.setOnClickListener {
                        val gson = Gson()
                        val info = JsonObject()
                        val setInfo = JsonObject()

                        info.addProperty("skills", "[tech,rh]")
                        setInfo.add("setInfo", info)
                        viewModel.updateProfileMV(id, setInfo)
                        viewModel.observeUpdateError().observe(this, {
                            epSkillsChipGroup.removeAllViews()
                            viewModel.netMyProfile(id)
                        })
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

