package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.R // Pastikan import R sudah benar
import com.app.lokalmarket.databinding.ItemKeranjangBinding
import com.app.lokalmarket.v1.data.model.Keranjang
import java.text.NumberFormat
import java.util.Locale

class KeranjangListAdapter(
    private val items: MutableList<Keranjang>,
    private val onDeleteClick: (Keranjang) -> Unit
) : RecyclerView.Adapter<KeranjangListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemKeranjangBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        fun bind(item: Keranjang) {
            binding.tvCartItemName.text = item.namaProduk
            binding.tvCartItemPrice.text = rupiah.format(item.hargaSatuan)

            // Logika untuk menampilkan gambar berdasarkan ID produk
            val imageRes = when (item.idProduk) {
                1 -> R.drawable.sample_produk1
                2 -> R.drawable.sample_produk2
                else -> R.drawable.ic_launcher_background
            }
            binding.ivCartItemImage.setImageResource(imageRes)

            binding.btnHapusItem.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKeranjangBinding.inflate(
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

    fun updateData(newItems: List<Keranjang>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}