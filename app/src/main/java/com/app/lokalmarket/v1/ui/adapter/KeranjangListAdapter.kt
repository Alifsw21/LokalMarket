package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.R
import com.app.lokalmarket.databinding.ItemKeranjangBinding
import com.app.lokalmarket.v1.data.model.Keranjang
import java.text.NumberFormat
import java.util.Locale

class KeranjangListAdapter(
    private val items: MutableList<Keranjang>,
    private val showDeleteButton: Boolean = true,
    private val onDeleteClick: (Keranjang) -> Unit,
    private val onItemClick: (Keranjang) -> Unit
) : RecyclerView.Adapter<KeranjangListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemKeranjangBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        fun bind(item: Keranjang) {
            binding.tvCartItemName.text = item.namaProduk

            // Tambahkan kuantitas ke tampilan harga (misal: Rp15.000 x 2)
            binding.tvCartItemPrice.text = "${rupiah.format(item.hargaSatuan)} x ${item.jumlahBarang}"

            // Logika untuk menampilkan gambar
            val imageRes = when (item.idProduk) {
                1 -> R.drawable.sample_produk1
                2 -> R.drawable.sample_produk2
                else -> R.drawable.ic_launcher_background
            }
            binding.ivCartItemImage.setImageResource(imageRes)

            // Logika untuk menyembunyikan tombol hapus di Detail Pesanan
            if (showDeleteButton) {
                binding.btnHapusItem.visibility = View.VISIBLE
                binding.btnHapusItem.setOnClickListener {
                    onDeleteClick(item)
                }
            } else {
                binding.btnHapusItem.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                onItemClick(item)
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