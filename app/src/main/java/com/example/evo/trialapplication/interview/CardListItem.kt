package com.example.evo.trialapplication.interview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evo.trialapplication.databinding.CardListItemBinding


class CardListItem : RecyclerView.Adapter<CardListItem.ViewHolder>() {
    private var dataSet = ArrayList<CartListModel>()

    var selectedListener: ((index: Int, itemCount: Boolean) -> Unit)? = null
    var deleteListener: ((index: Int) -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(dataSet[position])

    }


    fun addList(uiModels: List<CartListModel?>?) {
        val newList: List<CartListModel> = uiModels?.filterNotNull() ?: emptyList()
        val diffCallback = ListItemDiffCallback(dataSet, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        dataSet.clear()
        dataSet.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: CardListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(cartListModel: CartListModel) {
            binding.apply {
                content.text = cartListModel.product
                price.text = cartListModel.viewPrice.toString()
                cartCount.text = cartListModel.itemCount.toString()
                binding.delete.setOnClickListener {
                    deleteListener?.invoke(dataSet.indexOf(cartListModel))

                }
                add.setOnClickListener {

                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {

                        selectedListener?.invoke(dataSet.indexOf(cartListModel), true)

                    }
                }
                less.setOnClickListener {

                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        selectedListener?.invoke(dataSet.indexOf(cartListModel), false)


                    }
                }

            }
        }

    }


    companion object {
        private val TAG = CardListItem::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }


    override fun getItemCount(): Int {
        return dataSet.size
    }


}

class ListItemDiffCallback(
    private val oldList: List<CartListModel?>?,
    private val newList: List<CartListModel?>?
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return (oldItem?.itemCount == newItem?.itemCount)

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return oldItem == newItem
    }
}
