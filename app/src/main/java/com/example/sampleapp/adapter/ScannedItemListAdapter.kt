package com.example.sampleapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R
import com.example.sampleapp.extensions.convertToReadableString
import com.example.sampleapp.ui.history.ScanHistory
import com.example.sampleapp.vo.ScannedItem
import kotlinx.android.synthetic.main.scanned_item_view.view.*
import javax.inject.Inject

class ScannedItemListAdapter @Inject constructor(private val scanHistory: ScanHistory) :
    RecyclerView.Adapter<ScannedItemListAdapter.ItemHolder>() {

    var scannedItemList: MutableList<ScannedItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.scanned_item_view, parent, false))

    override fun getItemCount(): Int = scannedItemList.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemView.scannedText.text = scannedItemList[position].ScannedString
        holder.itemView.scannedDate.text =
            scannedItemList[position].timeStamp.convertToReadableString(holder.itemView.context)
        holder.itemView.setBackgroundColor(if(scannedItemList[position].isDirty)
            ContextCompat.getColor(holder.itemView.context, R.color.lightGreyColor)
        else  ContextCompat.getColor(holder.itemView.context, android.R.color.white))
    }

    fun updateList(scannedRecords: MutableList<ScannedItem>) {
        scannedItemList = scannedRecords
        notifyDataSetChanged()
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}