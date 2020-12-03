package com.cloudhired

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cloudhired.model.Notification

class NotiRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Notification, NotiRowAdapter.VH>(NotiSumDiff()) {
    companion object {
        const val iFullname = "iFullname"
        const val iUsername = "iUsername"
        const val iToEmail = "iToEmail"
    }

    inner class VH(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var nameTV = itemView.findViewById<TextView>(R.id.rnNameTV)
        private var msgTV = itemView.findViewById<TextView>(R.id.rnMsgTV)
        private var timeTV = itemView.findViewById<TextView>(R.id.rnTimeTV)
        private var mainCL = itemView.findViewById<ConstraintLayout>(R.id.rnMainCL)

        init {
            mainCL.setOnClickListener {
                val pvIntent = Intent(it.context, Chat::class.java)
                val pvExtras = Bundle()
                pvExtras.putString(iFullname, getItem(adapterPosition).fullname)
                pvExtras.putString(iToEmail, getItem(adapterPosition).email)
                pvIntent.putExtras(pvExtras)
                it.context.startActivity(pvIntent)
            }
        }

        fun bind(item: Notification) {
            nameTV.text = item.fullname
            msgTV.text = item.message
            timeTV.text = item.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_notification, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class ProSumDiff : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.username == newItem.username
                    && oldItem.fullname == newItem.fullname
                    && oldItem.time == newItem.time
                    && oldItem.message == newItem.message
                    && oldItem.username == newItem.username
        }
    }
}

