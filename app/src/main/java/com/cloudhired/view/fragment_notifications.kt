package com.cloudhired.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloudhired.*
import com.cloudhired.model.Notification
import kotlinx.android.synthetic.main.fragment_notifications.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentNotifications : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var auth: Auth
    private var notiList = mutableListOf<Notification>()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Auth(AppCompatActivity())
        swipe = notiSwipe
        swipe.setOnRefreshListener {
            notiList.clear()
            viewModel.netNotifications(auth.getEmail())
        }

        val adapter = NotiRowAdapter(viewModel)
        notiRecyclerView.adapter = adapter
        notiRecyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.netNotifications(auth.getEmail())

        viewModel.observeNotifacations().observe(viewLifecycleOwner, {
            it.forEach {
                val timeStamp = it.timeStamp?.toDate()
                val time = "${timeStamp?.hours}:${timeStamp?.minutes}"
                val userEmail = auth.getEmail()
                if (userEmail == it.ownerEmail) {
                    notiList.add(
                        Notification(
                            _id = it.rowID,
                            fullname = it.receiverName,
                            message = it.message,
                            email = it.receiverEmail,
                            username = "",
                            time = time
                        ))
                } else {
                    notiList.add(
                        Notification(
                            _id = it.rowID,
                            fullname = it.name,
                            message = it.message,
                            email = it.ownerEmail,
                            username = "",
                            time = time
                        ))
                }
            }
            adapter.submitList(notiList)
            adapter.notifyDataSetChanged()
            if (swipe.isRefreshing) swipe.isRefreshing = false
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentNotifications().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}