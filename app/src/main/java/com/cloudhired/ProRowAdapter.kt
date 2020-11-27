package com.cloudhired

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cloudhired.model.ProfessionalSummary

class ProRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<ProfessionalSummary, ProRowAdapter.VH>(ProSumDiff()) {
    companion object {
        const val iUsername = "iUsername"
        const val iToEmail = "iToEmail"
    }

    inner class VH(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var nameTV = itemView.findViewById<TextView>(R.id.name)
        private var locationTV = itemView.findViewById<TextView>(R.id.location)
        private var companyTV = itemView.findViewById<TextView>(R.id.company)
        private var certTV = itemView.findViewById<TextView>(R.id.certs_num)
        private var basicLL = itemView.findViewById<LinearLayout>(R.id.basic_info)
        private var msgIV = itemView.findViewById<ImageView>(R.id.message)

        init {
            basicLL.setOnClickListener {
                println("you clicked a LL")
                val pvIntent = Intent(it.context, ViewProfile::class.java)
                val pvExtras = Bundle()
                pvExtras.putString(iUsername, getItem(adapterPosition).username)
                pvIntent.putExtras(pvExtras)
                it.context.startActivity(pvIntent)
            }

            msgIV.setOnClickListener {
                println("clicked msg")
                val pvIntent = Intent(it.context, Chat::class.java)
                val pvExtras = Bundle()
                pvExtras.putString(iToEmail, getItem(adapterPosition).email)
                pvIntent.putExtras(pvExtras)
                it.context.startActivity(pvIntent)
            }
        }

        fun bind(item: ProfessionalSummary) {
//            val favL = viewModel.getFavList()
            nameTV.text = item.fullname
            locationTV.text = item.current_loc
            companyTV.text = "${item.job_title} at ${item.company}"
            certTV.text = item.number_certs
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_professional, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class ProSumDiff : DiffUtil.ItemCallback<ProfessionalSummary>() {
        override fun areItemsTheSame(oldItem: ProfessionalSummary, newItem: ProfessionalSummary): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ProfessionalSummary, newItem: ProfessionalSummary): Boolean {
            return oldItem.username == newItem.username
                    && oldItem.fullname == newItem.fullname
                    && oldItem.job_title == newItem.job_title
                    && oldItem.current_loc == newItem.current_loc
                    && oldItem.company == newItem.company
                    && oldItem.number_certs == oldItem.number_certs
        }
    }
}

