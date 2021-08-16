package com.katyrin.historyscreen.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.historyscreen.R
import com.katyrin.model.data.userdata.DataModel
import com.katyrin.utils.delegate.viewById
import com.katyrin.utils.extensions.toast

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = arrayListOf()

    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder =
        RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_history_recyclerview_item, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerHistoryTextviewRecyclerItem by
        view.viewById<TextView>(R.id.header_history_textview_recycler_item)

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                headerHistoryTextviewRecyclerItem.text = data.text
                itemView.setOnClickListener { it.toast("on click: ${data.text}") }
            }
        }
    }
}