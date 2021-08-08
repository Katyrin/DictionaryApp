package com.katyrin.dictionaryapp.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.databinding.ActivityHistoryRecyclerviewItemBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = arrayListOf()

    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder =
        RecyclerItemViewHolder(
            ActivityHistoryRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(
        private val itemBinding: ActivityHistoryRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemBinding.headerHistoryTextviewRecyclerItem.text = data.text
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "on click: ${data.text}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}