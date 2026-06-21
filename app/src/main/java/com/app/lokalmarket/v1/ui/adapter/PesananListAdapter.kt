package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.v1.data.model.Pesanan
import com.app.lokalmarket.databinding.ItemPesananListBinding
import java.text.NumberFormat
import java.util.Locale

class PesananListAdapter(
    private val items: MutableList<Pesanan>,
    private val onDeleteClick: (Pesanan) -> Unit
) : RecyclerView.Adapter<PesananListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemPesananListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        fun bind(item: Pesanan) {
            binding.tvPesananPengguna.text = item.idPengguna.toString()
            binding.tvTanggalPesanan.text = item.tanggalPesanan
            binding.tvAlamat.text = item.alamat
            binding.tvTotalHarga.text = rupiah.format(item.totalHarga)

            binding.btnHapusPesanan.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPesananListBinding.inflate(
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

    fun updateData(newItems: List<Pesanan>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}