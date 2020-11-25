package com.cloudhired

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cloudhired.R
import kotlinx.android.synthetic.main.view_profile.*

class ViewProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_profile)

        vpTitle.text = intent.getStringExtra("iUsername")
//        vpName.text = intent.getStringExtra("iTitle")
//        vpJobCom.text = intent.getStringExtra("iSelfText")
//        vpLocation.text = intent.getStringExtra("iSelfText")
        vpBack.setOnClickListener {
            finish()
        }
    }
}

