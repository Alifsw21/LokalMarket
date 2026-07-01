package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.R
import com.app.lokalmarket.v1.data.model.Produk
import com.app.lokalmarket.databinding.ItemProdukBinding
import java.text.NumberFormat
import java.util.Locale

class ProdukCardAdapter(
    private val items: MutableList<Produk>,
    private val onItemClick: (Produk, Int) -> Unit
) : RecyclerView.Adapter<ProdukCardAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemProdukBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        fun bind(product: Produk) {
            binding.tvProductName.text = product.namaProduk
            binding.tvPrice.text = rupiah.format(product.harga)

            val context = binding.root.context
            val fileName = product.imageFileName

            var imageId = 0

            if (!fileName.isNullOrEmpty()) {
                val imageId = context.resources.getIdentifier(
                    fileName,
                    "drawable",
                    context.packageName
                )

                if (imageId != 0) {
                    binding.ivProductImage.setImageResource(imageId)
                } else {
                    binding.ivProductImage.setImageResource(R.drawable.ic_launcher_background)
                }
            } else {
                binding.ivProductImage.setImageResource(R.drawable.ic_launcher_background)
            }

            if (product.diskon != null && product.diskon > 0) {
                binding.tvDiscount.visibility = View.VISIBLE
                binding.tvDiscount.text = "Diskon ${product.diskon}%"
            } else {
                binding.tvDiscount.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick(product, imageId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProdukBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Produk>) {
        // Ambil snapshot dulu sebelum clear, untuk menghindari kasus newItems
        // ternyata adalah objek list yang sama dengan items (referensi aliasing)
        val snapshot = newItems.toList()
        items.clear()
        items.addAll(snapshot)
        notifyDataSetChanged()
    }
}