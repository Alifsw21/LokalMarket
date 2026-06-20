package com.app.lokalmarket.v1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.R
import com.app.lokalmarket.v1.model.ProductItem
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(private val listItem: List<ProductItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_cart_item_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_cart_item_price)
    }

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = listItem[position]
        holder.tvName.text = item.name
        holder.tvPrice.text = rupiah.format(item.price)
    }

    override fun getItemCount(): Int = listItem.size
}