package com.cloudhired

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cloudhired.model.ChatRow

class ChatAdapter(private var viewModel: MainViewModel)
    : ListAdapter<ChatRow, ChatAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<ChatRow>() {
        override fun areItemsTheSame(oldItem: ChatRow, newItem: ChatRow): Boolean {
            return oldItem.rowID == newItem.rowID
        }

        override fun areContentsTheSame(oldItem: ChatRow, newItem: ChatRow): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.ownerUid == newItem.ownerUid
                    && oldItem.message == newItem.message
                    && oldItem.pictureUUID == newItem.pictureUUID
                    && oldItem.timeStamp == newItem.timeStamp
        }
    }
    companion object {
        private val iphoneTextBlue = Color.parseColor("#1982FC")
        private val iphoneMessageGreen = ColorDrawable(Color.parseColor("#43CC47"))
        private val dimGrey = Color.parseColor("#C5C5C5")
        private val dateFormat =
            SimpleDateFormat("hh:mm:ss MM-dd-yyyy")
        private val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
    }

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // May the Lord have mercy upon my soul
        private var myUserTV = itemView.findViewById<TextView>(R.id.chatUserTV)
        private var myTimeTV = itemView.findViewById<TextView>(R.id.chatTimeTV)
        private var myTextTV = itemView.findViewById<TextView>(R.id.chatTextTV)
        private var myTextCV = itemView.findViewById<CardView>(R.id.textCV)
        private var myPicIV = itemView.findViewById<ImageView>(R.id.picIV)
        private var otherUserTV = itemView.findViewById<TextView>(R.id.otherChatUserTV)
        private var otherTimeTV = itemView.findViewById<TextView>(R.id.otherChatTimeTV)
        private var otherTextTV = itemView.findViewById<TextView>(R.id.otherChatTextTV)
        private var otherTextCV = itemView.findViewById<CardView>(R.id.otherTextCV)
        private var otherPicIV = itemView.findViewById<ImageView>(R.id.otherPicIV)
        init {
            myTextCV.isLongClickable = true
        }
        private fun goneElements(userTV: TextView, timeTV: TextView, textTV: TextView,
                                 textCV: CardView, picIV: ImageView) {
            userTV.visibility = View.GONE
            timeTV.visibility = View.GONE
            textTV.visibility = View.GONE
            textCV.visibility = View.GONE
            picIV.visibility = View.GONE
        }
        private fun visibleElements(userTV: TextView, timeTV: TextView, textTV: TextView,
                                 textCV: CardView, picIV: ImageView) {
            userTV.visibility = View.GONE
            timeTV.visibility = View.VISIBLE
            textTV.visibility = View.VISIBLE
            textCV.visibility = View.VISIBLE
            picIV.visibility = View.GONE
        }
        private fun bindElements(item: ChatRow, backgroundColor: Int, textColor: Int,
                                 userTV: TextView, timeTV: TextView, textTV: TextView,
                                 textCV: CardView, picIV: ImageView) {
            // Set background on CV, not TV because...layout is weird
            textCV.setCardBackgroundColor(Color.TRANSPARENT)
//            textTV.setTextColor(textColor)
//            userTV.text = item.name
            textTV.text = item.message
//            textCV.setOnLongClickListener {
//                viewModel.deleteChatRow(item)
//                true
//            }
            // XXX Write me, bind picIV using pictureUUID.
//            if (item.pictureUUID != null) {
//                viewModel.glideFetch(item.pictureUUID!!, picIV)
//            } else {
//                picIV.visibility = View.GONE
//            }

            if (item.timeStamp == null) {
                timeTV.text = ""
            } else {
                //Log.d(javaClass.simpleName, "date ${item.timeStamp}") dateFormat.format(item.timeStamp.toDate()).
                val currentTime = item.timeStamp.toDate()
                timeTV.text = "${currentTime.hours}:${currentTime.minutes}"
            }
        }
        fun bind(item: ChatRow?) {
            if (item == null) return
            if (viewModel.myUid() == item.ownerUid) {
                goneElements(otherUserTV, otherTimeTV, otherTextTV, otherTextCV, otherPicIV)
                visibleElements(myUserTV, myTimeTV, myTextTV, myTextCV, myPicIV)
                bindElements(
                    item, iphoneTextBlue, Color.WHITE,
                    myUserTV, myTimeTV, myTextTV, myTextCV, myPicIV)
            } else {
                goneElements(myUserTV, myTimeTV, myTextTV, myTextCV, myPicIV)
                visibleElements(otherUserTV, otherTimeTV, otherTextTV, otherTextCV, otherPicIV)
                bindElements(
                    item, dimGrey, Color.BLACK,
                    otherUserTV, otherTimeTV, otherTextTV, otherTextCV, otherPicIV)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_chat, parent, false)
        //Log.d(MainActivity.TAG, "Create VH")
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        //Log.d(MainActivity.TAG, "Bind pos $position")
        holder.bind(getItem(holder.adapterPosition))
    }
}
