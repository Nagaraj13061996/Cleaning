package com.example.evo.trialapplication.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evo.trialapplication.database.ProductData
import com.example.evo.trialapplication.databinding.ProductViewAdapterBinding

class ProductViewAdapter () : RecyclerView.Adapter<ProductViewAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val binding: ProductViewAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

                fun setViews(goodsReceiptModel: ProductData) {
            binding.apply {

                product.text = goodsReceiptModel.product
                qty.text = goodsReceiptModel.qty
            }
        }

        init {
            binding.apply {
                product.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ProductViewAdapterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val sample = differ.currentList[position]
        holder.setViews(sample)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val callback = object : DiffUtil.ItemCallback<ProductData>() {
        override fun areItemsTheSame(
            oldItem: ProductData,
            newItem: ProductData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductData,
            newItem: ProductData
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, callback)

}