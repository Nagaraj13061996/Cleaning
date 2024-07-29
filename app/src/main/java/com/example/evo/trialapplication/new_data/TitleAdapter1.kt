package com.example.evo.trialapplication.new_data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evo.trialapplication.databinding.HeaderItemBinding
import com.example.evo.trialapplication.interview.CheckBoxModel
import com.example.evo.trialapplication.interview.CheckList
import com.example.evo.trialapplication.interview.ChildAdapter
import com.example.evo.trialapplication.interview.TitleAdapter

class TitleAdapter1 (val context: Context) : RecyclerView.Adapter<TitleAdapter1.MainViewHolder>() {
    companion object {

        private val TAG = TitleAdapter1::class.java.simpleName
    }

    var totalPriceListener : ((totalPrice: Double,group:String,newModel:CheckList,itemId:Int,name:String)->Unit)?=null
    var selectedListener : (( newModel:MutableList<CheckList>)->Unit)?=null

    inner class MainViewHolder(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setViews(checkBoxModel: CheckBoxModel) {
            binding.apply {

                headerTitle.text = checkBoxModel.title
                headerSubTitle.text = checkBoxModel.subTitle
                required.isVisible = checkBoxModel.groupId=="621da754abb8a52242c181d8"


                val adapter = ChildAdapter1(checkBoxModel.checkBoxList)
                binding.childRecycle.adapter = adapter
                binding.childRecycle.layoutManager = LinearLayoutManager(context)
                adapter.differ.submitList(checkBoxModel.checkBoxList)
                adapter.totalPriceListener={_price,_groupId,_selectedItemData,_itemId,_name->
                    totalPriceListener?.invoke(_price,_groupId,_selectedItemData,_itemId,_name)
                }
                adapter.selectedListener={_selectedListener->
                    selectedListener?.invoke(
                        _selectedListener
                    )

                }
            }
        }

        init {
            binding.apply {
                headerTitle.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            HeaderItemBinding.inflate(
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