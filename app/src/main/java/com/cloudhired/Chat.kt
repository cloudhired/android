package com.cloudhired

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudhired.R
import com.cloudhired.model.ChatRow
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.view_chat.*

class Chat : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var chatAdapter: ChatAdapter
    private var currentUser: FirebaseUser? = null
    private var fragmentUUID: String? = null

    private fun scrollToEnd() =
            (chatAdapter.itemCount - 1).takeIf { it > 0 }?.let(cvChatRV::smoothScrollToPosition)
    private fun clearCompose() {
        // XXX Write me
        cvComposePreviewIV.visibility = View.GONE
        cvComposeMessageET.setText("")
        fragmentUUID = null
    }
    private fun initAuth() {
        viewModel.observeFirebaseAuthLiveData().observe(this, {
            currentUser = it
        })
    }
    private fun initRecyclerView()  {
        chatAdapter = ChatAdapter(viewModel)
        cvChatRV.adapter = chatAdapter
        cvChatRV.layoutManager = LinearLayoutManager(applicationContext)
        //https://stackoverflow.com/questions/26580723/how-to-scroll-to-the-bottom-of-a-recyclerview-scrolltoposition-doesnt-work
        cvChatRV.viewTreeObserver.addOnGlobalLayoutListener {
            scrollToEnd()
        }
        // Dividers not so nice in chat
    }
    private fun initComposeSendIB() {
        // Send message button
        cvComposeSendIB.setOnClickListener {
            if( cvComposeMessageET.text!!.isNotEmpty()) {
                val chatRow = ChatRow().apply {
                    val cUser = currentUser
                    if(cUser == null) {
                        name = "unknown"
                        ownerUid = "unknown"
                    } else {
                        name = cUser.displayName
                        ownerUid = cUser.uid
                        ownerEmail = FirebaseAuth.getInstance().currentUser?.email
                    }
                    receiverName = intent.getStringExtra("iFullname")!!
                    receiverEmail = intent.getStringExtra("iToEmail")!!
                    message = cvComposeMessageET.text.toString()
                    pictureUUID = fragmentUUID
                    clearCompose()
                }
                viewModel.saveChatRow(intent.getStringExtra("iToEmail")!!, chatRow)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_chat)

        initAuth()
        initComposeSendIB()
        initRecyclerView()

        vcTitle.text = intent.getStringExtra("iFullname")
        viewModel.getChat(intent.getStringExtra("iToEmail")!!)
        viewModel.observeChat().observe(this, {
            chatAdapter.submitList(it)
        })

        cvComposeMessageET.setOnEditorActionListener { /*v*/_, actionId, event ->
            // If user has pressed enter, or if they hit the soft keyboard "send" button
            // (which sends DONE because of the XML)
            if ((event != null
                            &&(event.action == KeyEvent.ACTION_DOWN)
                            &&(event.keyCode == KeyEvent.KEYCODE_ENTER))
                    || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                (requireActivity() as MainActivity).hideKeyboard()
                cvComposeSendIB.callOnClick()
            }
            true
        }

        cvComposePreviewIV.visibility = View.GONE
        vcBack.setOnClickListener {
            finish()
        }
    }

}

