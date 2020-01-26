package com.example.ebayitemsearch.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ebayitemsearch.MainActivity
import com.example.ebayitemsearch.R
import com.example.ebayitemsearch.models.Item
import com.example.ebayitemsearch.models.ItemSummary
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ebay_item_row.view.*

class ebay_item_adapter(private val result: List<ItemSummary>, private val context: Context): RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ebay_item_row, parent, false)
        return CustomViewHolder((view))
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = result[position]

        val shipping = result[position].shippingOptions

        holder.view.itemNameTextView.text = item.title

        //price and shipping
        holder.view.priceTextView.text = item.price.value + item.price.currency

        if(shipping != null) {
            for (shippingIndex in shipping) {
                if (shippingIndex.shippingCost != null && shippingIndex.shippingCost.value == "0.00") {
                    val shippingPrice = "Free Shipping"
                    holder.view.shippingTextView.text = shippingPrice
                } else if (shippingIndex.shippingCost == null) {
                    holder.view.shippingTextView.text = ""
                } else {
                    holder.view.shippingTextView.text =
                        "+ " + shippingIndex.shippingCost.value + shippingIndex.shippingCost.currency
                }
            }
        }
        //seller info
        holder.view.sellerTextView.text = item.seller.username
        holder.view.feedbackTextView.text = item.seller.feedbackPercentage + "%"


        if(item.image != null) {
            val iconImage = holder.view.imageView
            Picasso.get().load(item.image.imageUrl).into(iconImage)
        }


        holder.itemView.setOnClickListener {
            val url = item.itemWebUrl
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(context, i, null)
        }
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
