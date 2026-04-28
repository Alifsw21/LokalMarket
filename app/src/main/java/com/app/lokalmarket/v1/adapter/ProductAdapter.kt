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
import com.app.lokalmarket.v1.ui.main.DetailActivity

class ProductAdapter(private val listProduct: List<ProductItem>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvDiscount: TextView = itemView.findViewById(R.id.tv_discount)
    }
    private val rupiah =
        NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = listProduct[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = rupiah.format(product.price)
        holder.tvDiscount.text = product.discount

        holder.itemView.setOnClickListener {
            val intent = android.content.Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("EXTRA_NAME", product.name)
            intent.putExtra("EXTRA_PRICE", product.price)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listProduct.size
}