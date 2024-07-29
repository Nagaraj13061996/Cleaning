package com.example.evo.trialapplication.interview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evo.trialapplication.databinding.CheckBoxLayoutBinding
import com.example.evo.trialapplication.databinding.HeaderItemBinding
import com.example.evo.trialapplication.databinding.ProductViewAdapterBinding


class MultiAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val THE_FIRST_VIEW = 1
        const val THE_SECOND_VIEW = 2
    }



    private inner class HeaderViewHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(CheckBoxModel: CheckBoxModel) {

        }
    }

    private inner class CheckBoxViewHolder(private val binding: CheckBoxLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(CheckBoxModel: CheckBoxModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == THE_FIRST_VIEW) {
            HeaderViewHolder(HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else
            CheckBoxViewHolder(CheckBoxLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return differ.currentList[position].id
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (differ.currentList[position].id=== THE_FIRST_VIEW) {
//            (holder as HeaderViewHolder).bind(differ.currentList[position])
//        } else {
//            (holder as CheckBoxViewHolder).bind(differ.currentList[position])
//        }
    }


    private val callback = object : DiffUtil.ItemCallback<CheckBoxModel>() {
        override fun areItemsTheSame(
            oldItem: CheckBoxModel,
            newItem: CheckBoxModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: CheckBoxModel,
            newItem: CheckBoxModel
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, callback)


}
