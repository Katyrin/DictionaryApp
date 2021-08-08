package com.katyrin.dictionaryapp.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.dictionaryapp.data.model.DataModel
import com.katyrin.dictionaryapp.databinding.ItemDictionaryBinding
import com.katyrin.dictionaryapp.utils.convertMeaningsToString

class MainAdapter(
    private val onItemClick: (DataModel) -> Unit
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    inner class MainViewHolder(
        private val itemBinding: ItemDictionaryBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(dataModel: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemBinding.headerTextviewRecyclerItem.text = dataModel.text
                itemBinding.descriptionTextviewRecyclerItem.text =
                    //convertMeaningsToString(dataModel.meanings!!)
                    dataModel.meanings?.get(0)?.translation?.translation

                itemBinding.root.setOnClickListener { onItemClick(dataModel) }
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
            ItemDictionaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(dataModels[position])
    }

    override fun getItemCount(): Int = dataModels.size
}