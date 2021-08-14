package com.katyrin.dictionaryapp.view.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.dictionaryapp.R
import com.katyrin.model.data.DataModel
import com.katyrin.utils.delegate.viewById

class MainAdapter(
    private val onItemClick: (DataModel) -> Unit
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerTextviewRecyclerItem by
        view.viewById<TextView>(R.id.header_textview_recycler_item)
        private val descriptionTextviewRecyclerItem by
        view.viewById<TextView>(R.id.description_textview_recycler_item)

        fun bind(dataModel: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                headerTextviewRecyclerItem.text = dataModel.text
                descriptionTextviewRecyclerItem.text =
                    dataModel.meanings?.get(0)?.translation?.translation
                itemView.setOnClickListener { onItemClick(dataModel) }
            }
        }
    }

    private var dataModels: List<DataModel> = listOf()

    fun setData(newDataModels: List<DataModel>) {
        dataModels = newDataModels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dictionary, parent, false)
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(dataModels[position])
    }

    override fun getItemCount(): Int = dataModels.size
}