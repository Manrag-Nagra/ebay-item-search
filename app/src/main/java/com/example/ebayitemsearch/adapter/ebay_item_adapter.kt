package com.example.ebayitemsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ebayitemsearch.R
import com.example.ebayitemsearch.models.Item
import com.example.ebayitemsearch.models.ItemSummary
import kotlinx.android.synthetic.main.ebay_item_row.view.*

class ebay_item_adapter(private val result: List<ItemSummary>): RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ebay_item_row, parent, false)
        return CustomViewHolder((view))
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = result[position]

        holder.view.itemNameTextView.text = item.title
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
