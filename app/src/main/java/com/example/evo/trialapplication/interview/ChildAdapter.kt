package com.example.evo.trialapplication.interview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.evo.trialapplication.databinding.CheckBoxLayoutBinding
import com.example.evo.trialapplication.databinding.RadioButtonItemBinding

class ChildAdapter(val newModel: MutableList<CheckList>) : RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val THE_FIRST_VIEW = 1
        const val THE_SECOND_VIEW = 2
        private val TAG = ChildAdapter::class.java.simpleName
    }

    private var selectedGroupId: String = "-1"
    private var selectedItemPosition: Int = -1
    private var radioSelectedItemPosition: Int = 0
    private var totalPrice: Double = 0.0
    private var finalPrice: Double = 0.0

    var selectedListener: ((newModel: MutableList<CheckList>) -> Unit)? = null
    var totalPriceListener: ((totalPrice: Double, group: String, newModel: CheckList, itemId: Int,name:String) -> Unit)? =
        null

    var pos = -1
    var _itemCount = 0

    private inner class CheckBoxViewHolder(private val binding: CheckBoxLayoutBinding) :
        ViewHolder(binding.root) {
        fun bind(checkList: CheckList) {
            binding.apply {
                binding.checkbox.isChecked = checkList.selected
                checkbox.text = checkList.name
                if (checkList.price > 0.0) {
                    checkboxPrice.text = "₹ ${checkList.price}"

                } else {
                    checkboxPrice.text = ""
                }


                if (checkList.selected) {
                    linear3.isVisible = true
                    cartCount.text = checkList.itemCount.toString()
                } else {
                    linear3.isVisible = false
                }
            }
        }

        init {
            binding.apply {

                checkbox.setOnClickListener {
                    val position = adapterPosition
                    val groupId = differ.currentList[position].groupId

                    _itemCount = 1
                    differ.currentList[position].itemCount = _itemCount
                    finalPrice = differ.currentList[position].price * _itemCount
                    if (position != RecyclerView.NO_POSITION) {

                        if (groupId != selectedGroupId) {
                            updateSelection(position, groupId)

                            totalPrice += differ.currentList[position].price


                        } else {

                            if (selectedItemPosition != position) {


                                if (differ.currentList[selectedItemPosition].selected) {
                                    totalPrice -= differ.currentList[selectedItemPosition].price
                                    totalPrice += differ.currentList[position].price
                                    finalPrice = differ.currentList[position].price * _itemCount

                                } else {
                                    totalPrice += differ.currentList[position].price
                                    finalPrice = differ.currentList[position].price * _itemCount
                                    finalPrice = differ.currentList[position].price * _itemCount
                                }

                                differ.currentList[selectedItemPosition].selected = false
                                differ.currentList[position].selected = true
                            } else {

                                if (differ.currentList[position].selected) {
                                    differ.currentList[position].selected = false
                                    totalPrice -= differ.currentList[position].price
                                    finalPrice = differ.currentList[position].price * 0
                                } else {
                                    differ.currentList[position].selected = true
                                    totalPrice += differ.currentList[position].price
                                    finalPrice = differ.currentList[position].price * _itemCount

                                }

                            }

                            notifyItemChanged(position)
                            notifyItemChanged(selectedItemPosition)
                            selectedGroupId = groupId
                            selectedItemPosition = position

                        }


                    }


                    totalPriceListener?.invoke(
                        finalPrice,
                        groupId,
                        differ.currentList[position],
                        differ.currentList[position].itemId,
                        differ.currentList[position].name


                    )
                    selectedListener?.invoke(differ.currentList)
                    notifyItemChanged(position)

                }

                add.setOnClickListener {

                    val position = adapterPosition
                    val groupId = differ.currentList[position].groupId

                    if (position != RecyclerView.NO_POSITION) {
                        _itemCount++
                        differ.currentList[position].itemCount = _itemCount
                        finalPrice = differ.currentList[position].price * _itemCount
                        notifyItemChanged(position)
                        totalPriceListener?.invoke(
                            finalPrice,
                            groupId,
                            differ.currentList[position],
                            differ.currentList[position].itemId,
                            differ.currentList[position].name

                        )
                        selectedListener?.invoke(differ.currentList)
                    }

                }
                less.setOnClickListener {

                    val position = adapterPosition
                    val groupId = differ.currentList[position].groupId

                    if (position != RecyclerView.NO_POSITION) {
                        _itemCount--
                        if (_itemCount < 1) {
                            differ.currentList[position].itemCount = _itemCount
                            finalPrice = differ.currentList[position].price * 0
                            differ.currentList[position].selected = false
                            notifyItemChanged(position)
                            totalPriceListener?.invoke(
                                finalPrice,
                                groupId,
                                differ.currentList[position],
                                differ.currentList[position].itemId,
                                differ.currentList[position].name

                            )
                            selectedListener?.invoke(differ.currentList)
                        } else {
                            differ.currentList[position].itemCount = _itemCount
                            finalPrice = differ.currentList[position].price * _itemCount
                            notifyItemChanged(position)
                            totalPriceListener?.invoke(
                                finalPrice,
                                groupId,
                                differ.currentList[position],
                                differ.currentList[position].itemId,
                                differ.currentList[position].name

                            )
                            selectedListener?.invoke(differ.currentList)
                        }

                    }

                }
            }
        }
    }

    private inner class RadioButtonViewHolder(private val binding: RadioButtonItemBinding) :
        ViewHolder(binding.root) {
        fun bind(checkList: CheckList) {
            binding.apply {
                radioButton.isChecked = checkList.selected
                radioButton.text = checkList.name
                radioButtonPrice.text = "₹ ${checkList.price}"
            }

        }

        init {

            binding.radioButton.setOnClickListener {
                val position = adapterPosition
                val groupId = differ.currentList[position].groupId
                if (position != RecyclerView.NO_POSITION) {
                    if (position == radioSelectedItemPosition) {
                        differ.currentList[position].selected = true
                        totalPrice = differ.currentList[position].price
                    } else {
                        differ.currentList[position].selected = true
                        differ.currentList[radioSelectedItemPosition].selected = false
                        totalPrice = differ.currentList[position].price
                    }


                }


                totalPriceListener?.invoke(totalPrice, groupId, differ.currentList[position],
                    differ.currentList[position].itemId,
                    differ.currentList[position].name
                )
                selectedListener?.invoke(differ.currentList)
                notifyItemChanged(radioSelectedItemPosition)
                notifyItemChanged(position)
                radioSelectedItemPosition = position
            }
        }
    }

    fun updateSelection(position: Int, groupId: String) {
        // Uncheck the previously selected item in the same group
        if (selectedItemPosition != -1) {
            differ.currentList[selectedItemPosition].selected = false
        }

        // Check the newly selected item
        differ.currentList[position].selected = true


        // Update the selected item's group ID and position
        selectedGroupId = groupId
        selectedItemPosition = position
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == THE_FIRST_VIEW) {
            RadioButtonViewHolder(
                RadioButtonItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else
            CheckBoxViewHolder(
                CheckBoxLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (differ.currentList[position].viewType === THE_FIRST_VIEW) {
            (holder as RadioButtonViewHolder).bind(differ.currentList[position])
        } else {
            (holder as CheckBoxViewHolder).bind(differ.currentList[position])
        }
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val callback = object : DiffUtil.ItemCallback<CheckList>() {
        override fun areItemsTheSame(
            oldItem: CheckList,
            newItem: CheckList
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CheckList,
            newItem: CheckList
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, callback)

}

